package com.gw.pay.dao.impl;

import com.gw.pay.dao.PayLogDao;
import com.gw.pay.entity.PayLogPO;
import com.gw.pay.utils.NumberGenerate;

import java.util.HashMap;
import java.util.Map;

/**
 * Description: 支付记录持久
 *
 * @author LinHuiBa-YanAn
 * @date 2023/10/11 20:29
 */
public class PayLogDaoImpl implements PayLogDao {

    private static Map<Long, PayLogPO> dataMap = new HashMap<>();

    @Override
    public PayLogPO getById(Long id) {
        return dataMap.get(id);
    }

    @Override
    public Long store(PayLogPO payLogPO) {
        if (payLogPO.getId() == null) {
            Long id = NumberGenerate.generatorRandomNum();
            payLogPO.setId(id);
            dataMap.put(id, payLogPO);
        } else {
            dataMap.put(payLogPO.getId(), payLogPO);
        }
        return payLogPO.getId();
    }
}
