package com.gw.pay.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Description: 订单请求体
 *
 * @author LinHuiBa-YanAn
 * @date 2023/10/6 17:06
 */
@Data
public class PayLogPO {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 商户支付no 和微信交互 查询订单使用（outTradeNo）
     */
    private String outTradeNo;

    /**
     * 微信交易流水号
     */
    private String transactionId;

    /**
     * 用户userId
     */
    private Long userId;

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

    /**
     * 支付状态 1待支付 2支付成功 3支付失败
     */
    private Integer status;

    /**
     * 支付时间
     */
    private LocalDateTime payTime;
}
