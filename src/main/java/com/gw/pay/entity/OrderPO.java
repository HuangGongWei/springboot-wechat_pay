package com.gw.pay.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Description: 订单表
 *
 * @author LinHuiBa-YanAn
 * @date 2023/10/11 19:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPO {
    /**
     * 订单id
     */
    private Long id;
    /**
     * 订单名称
     */
    private String name;
    /**
     * 订单金额
     */
    private BigDecimal money;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 订单状态
     */
    private Integer status;
    // 省略其他信息
}
