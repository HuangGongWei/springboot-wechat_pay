package com.gw.pay.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Description: 订单支付枚举
 *
 * @author LinHuiBa-YanAn
 * @date 2023/10/7 14:09
 */
@Getter
@AllArgsConstructor
public enum OrderPayStatusEnum {
    /**
     * 1 待支付
     */
    TO_PAY(1, "待支付"),
    /**
     * 2 支付成功
     */
    PAY_SUCCESS(2, "支付成功"),
    /**
     * 3 支付失败
     */
    PAY_ERROR(3, "支付失败");

    /**
     * code
     */
    private final Integer code;
    /**
     * 名称
     */
    private final String name;
}
