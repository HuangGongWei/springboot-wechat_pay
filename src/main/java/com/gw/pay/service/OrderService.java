package com.gw.pay.service;

import com.gw.pay.vo.OrderPayVO;
import com.gw.pay.vo.StatusVO;

import javax.servlet.http.HttpServletRequest;

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

    /**
     * 查询订单支付状态
     * @param payLogId 支付记录id
     * @return 订单支付状态
     */
    StatusVO queryOrderPayStatus(Long payLogId);

    /**
     * 查询订单状态
     * @param orderId 订单id
     * @return 订单状态
     */
    StatusVO queryOrderStatus(Long orderId);

    /**
     * 微信支付回调
     * @param request HttpServletRequest
     */
    void payCallBack(HttpServletRequest request) throws Exception;

}
