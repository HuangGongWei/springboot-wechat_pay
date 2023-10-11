package com.gw.pay.dao;

import com.gw.pay.entity.PayLogPO;

/**
 * Description: 支付记录持久
 *
 * @author LinHuiBa-YanAn
 * @date 2023/10/11 19:46
 */
public interface PayLogDao {

    /**
     * 根据id查询支付记录
     *
     * @param id 支付记录id
     * @return 支付记录信息
     */
    PayLogPO getById(Long id);

    /**
     * 保存
     *
     * @param payLogPO PayLogPO
     * @return 支付记录id
     */
    Long store(PayLogPO payLogPO);

}
