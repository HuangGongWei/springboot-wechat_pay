package com.gw.pay.enums;

import com.gw.pay.vo.StatusVO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * Description: 订单支付枚举
 *
 * @author LinHuiBa-YanAn
 * @date 2023/10/7 14:09
 */
@Getter
@AllArgsConstructor
public enum OrderStatusEnum {
    /**
     * 1 待支付
     */
    TO_PAY(1, "待支付"),
    /**
     * 2 待发货
     */
    DELIVER_GOODS(2, "待发货"),
    /**
     * 3 运送
     */
    TRANSPORT(3, "运送"),
    /**
     * 4 签收
     */
    SIGN_FOR(4, "签收"),
    /**
     * 5 订单完成
     */
    OVER(5, "订单完成"),
    /**
     * 6 订单关闭
     */
    CLOSE(6, "订单关闭");

    /**
     * code
     */
    private final Integer code;
    /**
     * 名称
     */
    private final String name;

    public static OrderStatusEnum getByCode(Integer code) {
        if (code == null) {
            return OrderStatusEnum.TO_PAY;
        }
        return Arrays.stream(values()).filter(item -> item.getCode().equals(code)).findFirst().orElse(OrderStatusEnum.TO_PAY);
    }

    public StatusVO toStatusVO() {
        StatusVO statusVO = new StatusVO();
        statusVO.setCode(this.code);
        statusVO.setMessage(this.name);
        return statusVO;
    }
}
