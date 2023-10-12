package com.gw.pay.external.request;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Description: 订单请求体
 *
 * @author LinHuiBa-YanAn
 * @date 2023/10/6 17:06
 */
@Data
public class CreateOrderPayRequest {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 商户支付no 和微信交互 查询订单使用（outTradeNo）
     */
    private String outTradeNo;

    /**
     * 用户openid
     */
    private String openId;

    /**
     * 支付金额
     */
    private BigDecimal payMoney;

    /**
     * 支付内容
     */
    private String payContent;
}
