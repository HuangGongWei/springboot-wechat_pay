package com.gw.pay.service.impl;

import cn.hutool.core.convert.Convert;
import com.gw.pay.dao.OrderDao;
import com.gw.pay.dao.PayLogDao;
import com.gw.pay.entity.OrderPO;
import com.gw.pay.entity.PayLogPO;
import com.gw.pay.enums.OrderPayStatusEnum;
import com.gw.pay.enums.OrderStatusEnum;
import com.gw.pay.external.WechatPayExternalService;
import com.gw.pay.external.request.CreateOrderPayRequest;
import com.gw.pay.service.OrderService;
import com.gw.pay.utils.NumberGenerate;
import com.gw.pay.vo.OrderPayVO;
import com.gw.pay.vo.StatusVO;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayWithRequestPaymentResponse;
import com.wechat.pay.java.service.payments.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
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
}
