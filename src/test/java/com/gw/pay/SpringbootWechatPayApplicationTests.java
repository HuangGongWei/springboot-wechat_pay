package com.gw.pay;

import com.alibaba.fastjson.JSONObject;
import com.gw.pay.external.request.OrderRequest;
import com.gw.pay.external.WechatPayExternalService;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayResponse;
import com.wechat.pay.java.service.payments.model.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.math.BigDecimal;

@SpringBootTest
class SpringbootWechatPayApplicationTests {

    @Resource
    private WechatPayExternalService wechatPayExternalService;

    @Test
    void createOrder() {
        OrderRequest createOrder = new OrderRequest();
        createOrder.setOutTradeNo("100000001");
        createOrder.setPayContent("商机直租会员续费");
        createOrder.setPayMoney(new BigDecimal(100.03));
        createOrder.setOpenId("oKwQd5MtFfgnXyLBp7vC6Pe3HAJQ");
        PrepayResponse prepayResponse = wechatPayExternalService.createOrder(createOrder);
        System.out.println(JSONObject.toJSONString(prepayResponse));
    }

    @Test
    void queryOrder() {
        Transaction result = wechatPayExternalService.queryOrderByOutTradeNo("100000001");
        System.out.println(JSONObject.toJSONString(result));
        if (Transaction.TradeStateEnum.SUCCESS.equals(result.getTradeState())) {
            System.out.println("支付成功");
        } else {
            System.out.println("支付失败");
        }
    }

    @Test
    void closeOrder() {
        wechatPayExternalService.closeOrder("100000001");
    }

}
