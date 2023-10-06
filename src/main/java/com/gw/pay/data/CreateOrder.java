package com.gw.pay.data;

import lombok.Data;

/**
 * Description: 创建订单
 *
 * @author LinHuiBa-YanAn
 * @date 2023/10/6 17:06
 */
@Data
public class CreateOrder {
    /**
     * 订单号
     */
    private String orderId;
    /**
     * 订单标题
     */
    private String orderTitle;
    /**
     * 订单总金额，单位为分
     */
    private Integer amountTotal;
    /**
     * 用户在商户appid下的唯一标识
     */
    private String openid;
}
