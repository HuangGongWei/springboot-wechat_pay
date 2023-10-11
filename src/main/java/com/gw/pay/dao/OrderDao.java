package com.gw.pay.dao;

import com.gw.pay.entity.OrderPO;

/**
 * Description: 订单持久
 *
 * @author LinHuiBa-YanAn
 * @date 2023/10/11 19:46
 */
public interface OrderDao {

    /**
     * 根据id查询订单
     *
     * @param id 订单id
     * @return 订单信息
     */
    OrderPO getById(Long id);

    /**
     * 保存
     *
     * @param orderPO OrderPO
     * @return 订单id
     */
    Long store(OrderPO orderPO);

}
