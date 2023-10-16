package com.gw.pay.dao.impl;

import com.gw.pay.dao.PayLogDao;
import com.gw.pay.entity.PayLogPO;
import com.gw.pay.utils.NumberGenerate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Description: 支付记录持久
 *
 * @author LinHuiBa-YanAn
 * @date 2023/10/11 20:29
 */
@Slf4j
@Service
public class PayLogDaoImpl implements PayLogDao {

    private static Map<Long, PayLogPO> dataMap = new HashMap<>();

    @Override
    public PayLogPO getById(Long id) {
        return dataMap.get(id);
    }

    @Override
    public PayLogPO getByOutTradeNo(String outTradeNo) {
        if (Objects.isNull(outTradeNo)) {
            return null;
        }
        return dataMap.values().stream().filter(item -> outTradeNo.equals(item.getOutTradeNo())).findFirst().orElse(null);
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
