package com.gw.pay.service.impl;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSONObject;
import com.gw.pay.dao.OrderDao;
import com.gw.pay.dao.PayLogDao;
import com.gw.pay.entity.OrderPO;
import com.gw.pay.entity.PayLogPO;
import com.gw.pay.enums.OrderPayStatusEnum;
import com.gw.pay.enums.OrderStatusEnum;
import com.gw.pay.enums.WechatPayCallBackHeaderConstant;
import com.gw.pay.external.WechatPayExternalService;
import com.gw.pay.external.request.CreateOrderPayRequest;
import com.gw.pay.external.request.WechatPayCallBackRequest;
import com.gw.pay.service.OrderService;
import com.gw.pay.utils.NumberGenerate;
import com.gw.pay.vo.OrderPayVO;
import com.gw.pay.vo.StatusVO;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayWithRequestPaymentResponse;
import com.wechat.pay.java.service.payments.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Description: 订单
 *
 * @author LinHuiBa-YanAn
 * @date 2023/10/11 19:46
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Resource
    private OrderDao orderDao;

    @Resource
    private PayLogDao payLogDao;

    @Resource
    private WechatPayExternalService wechatPayExternalService;

    @Resource
    private RedissonClient redissonClient;

    /**
     * 分布式锁
     */
    private final String LOCK_KEY = "WECHAT_PAY_LOCK:";

    @Override
    public Long createOrder() {
        // 省略商品等业务逻辑
        Long userId = 2019112689L;
        OrderPO orderPO = new OrderPO();
        orderPO.setName("VIP升级或续费");
        orderPO.setMoney(new BigDecimal("0.01"));
        orderPO.setUserId(userId);
        orderPO.setStatus(OrderStatusEnum.TO_PAY.getCode());
        return orderDao.store(orderPO);
    }

    @Override
    public OrderPayVO orderPay(Long orderId) {
        OrderPO order;
        if (Objects.isNull(orderId) || Objects.isNull(order = orderDao.getById(orderId))) {
            throw new RuntimeException("未知订单");
        }
        // 从上下文中获取用户id
        Long userId = 2019112689L;
        // 查询user表，获取用户微信小程序 openId
        String openId = "oKwQd5MtFfgnXyLBp7vC6Pe3HAJQ";
        // 记录支付日志
        PayLogPO payLog = new PayLogPO();
        payLog.setOrderId(order.getId());
        payLog.setOutTradeNo(NumberGenerate.generateUUID32());
        payLog.setUserId(userId);
        payLog.setOpenId(openId);
        payLog.setPayMoney(order.getMoney());
        payLog.setPayContent(order.getName());
        payLog.setStatus(OrderPayStatusEnum.TO_PAY.getCode());
        payLogDao.store(payLog);
        // 发起微信预支付
        CreateOrderPayRequest createOrderPayRequest = new CreateOrderPayRequest();
        createOrderPayRequest.setId(payLog.getId());
        createOrderPayRequest.setOutTradeNo(payLog.getOutTradeNo());
        createOrderPayRequest.setOpenId(payLog.getOpenId());
        createOrderPayRequest.setPayMoney(payLog.getPayMoney());
        createOrderPayRequest.setPayContent(payLog.getPayContent());
        PrepayWithRequestPaymentResponse response = wechatPayExternalService.prepayWithRequestPayment(createOrderPayRequest);
        return Convert.convert(OrderPayVO.class, response);
    }

    @Override
    public StatusVO queryOrderPayStatus(Long payLogId) {
        PayLogPO payLog;
        Transaction transaction;
        if (Objects.isNull(payLogId) || Objects.isNull(payLog = payLogDao.getById(payLogId)) || Objects.isNull(transaction = wechatPayExternalService.queryStatus(payLog.getOutTradeNo()))) {
            throw new RuntimeException("未知支付记录");
        }
        StatusVO resultVO = new StatusVO();
        if (Objects.nonNull(transaction) && Transaction.TradeStateEnum.SUCCESS == transaction.getTradeState()) {
            resultVO.setCode(1);
            resultVO.setMessage("支付成功");
        } else {
            resultVO.setCode(2);
            resultVO.setMessage("其他状态");
        }
        return resultVO;
    }

    @Override
    public StatusVO queryOrderStatus(Long orderId) {
        OrderPO order;
        if (Objects.isNull(orderId) || Objects.isNull(order = orderDao.getById(orderId))) {
            throw new RuntimeException("未知订单");
        }
        return OrderStatusEnum.getByCode(order.getStatus()).toStatusVO();
    }

    @Override
    public void payCallBack(HttpServletRequest request) throws Exception {
        BufferedReader reader = request.getReader();
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        WechatPayCallBackRequest callBackRequest = new WechatPayCallBackRequest();
        callBackRequest.setBody(sb.toString());
        callBackRequest.setNonce(request.getHeader(WechatPayCallBackHeaderConstant.NONCE));
        callBackRequest.setSerial(request.getHeader(WechatPayCallBackHeaderConstant.SERIAL));
        callBackRequest.setSignature(request.getHeader(WechatPayCallBackHeaderConstant.SIGNATURE));
        callBackRequest.setSignatureType(request.getHeader(WechatPayCallBackHeaderConstant.SIGNATURE_TYPE));
        callBackRequest.setTimestamp(request.getHeader(WechatPayCallBackHeaderConstant.TIMESTAMP));
        log.info("验签参数{}", JSONObject.toJSONString(callBackRequest));
        Transaction transaction = wechatPayExternalService.payCallBack(callBackRequest, Transaction.class);
        log.info("验签成功！-支付回调结果：{}", transaction.toString());

        String lockKey = LOCK_KEY + transaction.getOutTradeNo();
        RLock lock = redissonClient.getLock(lockKey);
        try {
            boolean isLock = lock.tryLock();
            if (!isLock) {
                throw new RuntimeException("请勿重复操作");
            }
            log.info("开始用户支付后业务处理");
            processTransaction(transaction);
            log.info("用户支付后业务处理成功");
        } catch (Exception e) {
            log.error("用户支付后业务处理错误, e{}", e);
            throw e;
        } finally {
            // 释放锁
            lock.unlock();
        }
    }

    /**
     * 处理回调业务(需要保证事务操作哦)
     * @param transaction Transaction
     */
    private void processTransaction(Transaction transaction) {
        // 修改订单前，主动请求微信查询订单是否支付成功，防止恶意post
        transaction = wechatPayExternalService.queryStatus(transaction.getOutTradeNo());
        if (Transaction.TradeStateEnum.SUCCESS != transaction.getTradeState()) {
            log.info("内部订单号【{}】,微信支付订单号【{}】支付未成功", transaction.getOutTradeNo(), transaction.getTransactionId());
            throw new RuntimeException("订单支付未成功");
        }
        // 修改支付信息
        PayLogPO payLog = payLogDao.getByOutTradeNo(transaction.getOutTradeNo());
        if (OrderPayStatusEnum.PAY_SUCCESS.getCode().equals(payLog.getStatus())) {
            // 若订单状态已为支付成功则不处理
            return;
        }
        payLog.setTransactionId(transaction.getTransactionId());
        if (Objects.nonNull(transaction.getSuccessTime())) {
            payLog.setPayTime(LocalDateTime.parse(transaction.getSuccessTime(), DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        }
        payLog.setStatus(OrderPayStatusEnum.PAY_SUCCESS.getCode());
        payLogDao.store(payLog);
        // 修改订单信息
        OrderPO order = orderDao.getById(payLog.getOrderId());
        order.setStatus(OrderStatusEnum.DELIVER_GOODS.getCode());
        orderDao.store(order);
        // 其他业务操作
    }
}
