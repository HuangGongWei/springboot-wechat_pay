package com.gw.pay.external.impl;

import com.gw.pay.config.WechatPayProperties;
import com.gw.pay.data.CreateOrder;
import com.gw.pay.external.WechatPayExternalService;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.core.exception.HttpException;
import com.wechat.pay.java.core.exception.MalformedMessageException;
import com.wechat.pay.java.core.exception.ServiceException;
import com.wechat.pay.java.service.payments.jsapi.JsapiService;
import com.wechat.pay.java.service.payments.jsapi.model.*;
import com.wechat.pay.java.service.payments.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Description: 微信支付对接
 *
 * @author LinHuiBa-YanAn
 * @date 2023/10/6 10:58
 */
@Slf4j
@Service
public class WechatPayExternalServiceImpl implements WechatPayExternalService {

    @Resource
    private RSAAutoCertificateConfig rsaAutoCertificateConfig;

    @Resource
    private WechatPayProperties properties;

    @Override
    public PrepayResponse createOrder(CreateOrder createOrder) {
        PrepayRequest request = new PrepayRequest();
        request.setAppid(properties.getAppId());
        request.setMchid(properties.getMerchantId());
        request.setDescription(createOrder.getOrderTitle());
        request.setOutTradeNo(createOrder.getOrderId());
        request.setNotifyUrl(properties.getPayNotifyUrl());
        Amount amount = new Amount();
        amount.setTotal(createOrder.getAmountTotal());
        request.setAmount(amount);
        Payer payer = new Payer();
        payer.setOpenid(createOrder.getOpenid());
        request.setPayer(payer);
        PrepayResponse result;
        try {
            JsapiService service = new JsapiService.Builder().config(rsaAutoCertificateConfig).build();
            result = service.prepay(request);
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
        return result;
    }

    @Override
    public Transaction queryOrderByOutTradeNo(String outTradeNo) {
        QueryOrderByOutTradeNoRequest queryRequest = new QueryOrderByOutTradeNoRequest();
        queryRequest.setMchid(properties.getMerchantId());
        queryRequest.setOutTradeNo(outTradeNo);
        Transaction result;
        try {
            JsapiService service = new JsapiService.Builder().config(rsaAutoCertificateConfig).build();
            result = service.queryOrderByOutTradeNo(queryRequest);
        } catch (ServiceException e) {
            log.error("订单查询失败，返回码：{},返回信息：{}", e.getErrorCode(), e.getErrorMessage());
            throw new RuntimeException("订单查询失败", e);
        }
        return result;
    }

    @Override
    public Transaction queryOrderByTransactionId(String transactionId) {
        QueryOrderByIdRequest queryRequest = new QueryOrderByIdRequest();
        queryRequest.setMchid(properties.getMerchantId());
        queryRequest.setTransactionId(transactionId);
        Transaction result;
        try {
            JsapiService service = new JsapiService.Builder().config(rsaAutoCertificateConfig).build();
            result = service.queryOrderById(queryRequest);
        } catch (ServiceException e) {
            log.error("订单查询失败，返回码：{},返回信息：{}", e.getErrorCode(), e.getErrorMessage());
            throw new RuntimeException("订单查询失败", e);
        }
        return result;
    }

    @Override
    public void closeOrder(String outTradeNo) {
        CloseOrderRequest closeOrderRequest = new CloseOrderRequest();
        closeOrderRequest.setMchid(properties.getMerchantId());
        closeOrderRequest.setOutTradeNo(outTradeNo);
        try {
            JsapiService service = new JsapiService.Builder().config(rsaAutoCertificateConfig).build();
            service.closeOrder(closeOrderRequest);
        } catch (ServiceException e) {
            log.error("订单关闭失败，返回码：{},返回信息：{}", e.getErrorCode(), e.getErrorMessage());
            throw new RuntimeException("订单关闭失败", e);
        }
    }
}
