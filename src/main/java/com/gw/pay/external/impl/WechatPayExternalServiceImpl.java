package com.gw.pay.external.impl;

import com.gw.pay.config.WechatPayProperties;
import com.gw.pay.external.WechatPayExternalService;
import com.gw.pay.external.request.CreateOrderPayRequest;
import com.wechat.pay.java.core.exception.HttpException;
import com.wechat.pay.java.core.exception.MalformedMessageException;
import com.wechat.pay.java.core.exception.ServiceException;
import com.wechat.pay.java.service.payments.jsapi.JsapiServiceExtension;
import com.wechat.pay.java.service.payments.jsapi.model.*;
import com.wechat.pay.java.service.payments.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Description: 微信支付对接（基于JSAPI 支付的扩展类实现）
 *
 * @author LinHuiBa-YanAn
 * @date 2023/10/7 11:49
 */
@Slf4j
@Service
public class WechatPayExternalServiceImpl implements WechatPayExternalService {

    @Resource
    private WechatPayProperties properties;

    @Resource
    private JsapiServiceExtension jsapiServiceExtension;

    @Override
    public PrepayWithRequestPaymentResponse prepayWithRequestPayment(CreateOrderPayRequest createOrderPay) {
        log.info("prepayWithRequestPayment");
        PrepayRequest request = new PrepayRequest();
        Amount amount = new Amount();
        BigDecimal payMoney = createOrderPay.getPayMoney();
        BigDecimal amountTotal = payMoney.multiply(new BigDecimal("100").setScale(0, RoundingMode.DOWN));
        amount.setTotal(amountTotal.intValue());
        request.setAmount(amount);
        Payer payer = new Payer();
        payer.setOpenid(createOrderPay.getOpenId());
        request.setPayer(payer);
        request.setTimeExpire(getExpiredTimeStr());
        request.setAppid(properties.getAppId());
        request.setMchid(properties.getMerchantId());
        request.setAttach(String.valueOf(createOrderPay.getId()));
        request.setDescription(createOrderPay.getPayContent());
        request.setNotifyUrl(properties.getPayNotifyUrl());
        //这里生成流水号，后续用这个流水号与微信交互，查询订单状态
        request.setOutTradeNo(createOrderPay.getOutTradeNo());
        PrepayWithRequestPaymentResponse result;
        try {
            result = jsapiServiceExtension.prepayWithRequestPayment(request);
        } catch (HttpException e) {
            log.error("微信下单发送HTTP请求失败，错误信息：{}", e.getHttpRequest());
            throw new RuntimeException("微信下单发送HTTP请求失败", e);
        } catch (ServiceException e) {
            // 服务返回状态小于200或大于等于300，例如500
            log.error("微信下单服务状态错误，错误信息：{}", e.getErrorMessage());
            throw new RuntimeException("微信下单服务状态错误", e);
        } catch (MalformedMessageException e) {
            // 服务返回成功，返回体类型不合法，或者解析返回体失败
            log.error("服务返回成功，返回体类型不合法，或者解析返回体失败，错误信息：{}", e.getMessage());
            throw new RuntimeException("服务返回成功，返回体类型不合法，或者解析返回体失败", e);
        }
        log.info("prepayWithRequestPayment end");
        return result;
    }

    @Override
    public Transaction queryStatus(String outTradeNo) {
        QueryOrderByOutTradeNoRequest request = new QueryOrderByOutTradeNoRequest();
        request.setMchid(properties.getMerchantId());
        request.setOutTradeNo(outTradeNo);
        try {
            return jsapiServiceExtension.queryOrderByOutTradeNo(request);
        } catch (ServiceException e) {
            log.error("订单查询失败，返回码：{},返回信息：{}", e.getErrorCode(), e.getErrorMessage());
            throw new RuntimeException("订单查询失败", e);
        }
    }

    @Override
    public void closeOrder(String outTradeNo) {
        log.info("closeOrder");
        CloseOrderRequest closeRequest = new CloseOrderRequest();
        closeRequest.setMchid(properties.getMerchantId());
        closeRequest.setOutTradeNo(outTradeNo);
        try {
            //方法没有返回值，意味着成功时API返回204 No Content
            jsapiServiceExtension.closeOrder(closeRequest);
        } catch (ServiceException e) {
            log.error("订单关闭失败，返回码：{},返回信息：{}", e.getErrorCode(), e.getErrorMessage());
            throw new RuntimeException("订单关闭失败", e);
        }
    }


    /**
     * 获取失效时间
     */
    private String getExpiredTimeStr() {
        //失效时间，10分钟
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiredTime = now.plusMinutes(10);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return formatter.format(expiredTime);
    }
}
