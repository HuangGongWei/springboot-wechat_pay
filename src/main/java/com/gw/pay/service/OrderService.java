package com.gw.pay.service;

import com.gw.pay.vo.OrderPayVO;

/**
 * Description: 订单
 *
 * @author LinHuiBa-YanAn
 * @date 2023/10/11 19:45
 */
public interface OrderService {

    /**
     * 创建订单
     * @return
     */
    Long createOrder();

    /**
     * 订单支付
     * @param orderId 订单id
     * @return 订单支付
     */
    OrderPayVO orderPay(Long orderId);

}
