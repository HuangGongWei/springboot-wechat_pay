package com.gw.pay.dao.impl;

import com.gw.pay.dao.OrderDao;
import com.gw.pay.entity.OrderPO;
import com.gw.pay.utils.NumberGenerate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Description: 订单持久
 *
 * @author LinHuiBa-YanAn
 * @date 2023/10/11 20:26
 */
@Service
public class OrderDaoImpl implements OrderDao {
    private static Map<Long, OrderPO> dataMap = new HashMap<>();
    static {
        dataMap.put(10001L, new OrderPO(10001L, "大娃娃", new BigDecimal("399.00"), 2019112655L));
        dataMap.put(10002L, new OrderPO(10002L, "Apple15", new BigDecimal("5999.00"), 2019112689L));
        dataMap.put(10003L, new OrderPO(10003L, "精品女士二手车", new BigDecimal("20000.00"), 2019112689L));
    }

    @Override
    public OrderPO getById(Long id) {
        return dataMap.get(id);
    }

    @Override
    public Long store(OrderPO orderPO) {
        if (orderPO.getId() == null) {
            Long orderId = NumberGenerate.generatorRandomNum();
            orderPO.setId(orderId);
            dataMap.put(orderId, orderPO);
        } else {
            dataMap.put(orderPO.getId(), orderPO);
        }
        return orderPO.getId();
    }
}
