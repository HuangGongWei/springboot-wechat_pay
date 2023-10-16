package com.gw.pay.external;

import com.gw.pay.external.request.CreateOrderPayRequest;
import com.gw.pay.external.request.WechatPayCallBackRequest;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayWithRequestPaymentResponse;
import com.wechat.pay.java.service.payments.model.Transaction;

/**
 * Description: 微信支付对接 V2（基于JSAPI 支付的扩展类实现）
 *
 * @author LinHuiBa-YanAn
 * @date 2023/10/7 11:38
 */
public interface WechatPayExternalService {

    /**
     * 提交预支付请求付款
     * @param createOrderPay 订单请求体
     * @return PrepayWithRequestPaymentResponse 预付费与请求付款响应
     */
    PrepayWithRequestPaymentResponse prepayWithRequestPayment(CreateOrderPayRequest createOrderPay);

    /**
     * 查询状态
     *
     * @param outTradeNo 商户支付no
     * @return 状态信息
     */
    Transaction queryStatus(String outTradeNo);

    /**
     * 取消订单
     *
     * @param outTradeNo
     */
    void closeOrder(String outTradeNo);

    /**
     * 回调信息转换 这些都是微信的回调信息，可以封装成对象传入
     * 官网地址：https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay4_1.shtml
     *
     * @param wechatPayCallBackRequest
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T payCallBack(WechatPayCallBackRequest wechatPayCallBackRequest, Class<T> clazz);

}
