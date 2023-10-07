package com.gw.pay.external;

import com.gw.pay.external.request.OrderRequest;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayResponse;
import com.wechat.pay.java.service.payments.model.Transaction;

/**
 * Description: 微信支付对接
 *
 * @author LinHuiBa-YanAn
 * @date 2023/10/6 10:57
 */
public interface WechatPayExternalService {
    /**
     * 创建订单
     * @param orderRequest 订单请求体
     * @return预支付交易会话标识
     */
    PrepayResponse createOrder(OrderRequest orderRequest);

    /**
     * 根据商户订单号查询订单
     *
     * @param outTradeNo 对外贸易号（即本业务中的订单号）
     * @return 事项
     */
    Transaction queryOrderByOutTradeNo(String outTradeNo);

    /**
     * 根据商户订单号查询订单(根据回话id，回调中返回)
     *
     * @param transactionId 商户订单号
     * @return 事项
     */
    Transaction queryOrderByTransactionId(String transactionId);

    /**
     * 关闭订单
     *
     * @param outTradeNo 对外贸易号（即本业务中的订单号）
     */
    void closeOrder(String outTradeNo);
}
