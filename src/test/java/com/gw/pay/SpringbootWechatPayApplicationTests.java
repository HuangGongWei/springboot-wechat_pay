package com.gw.pay;

import com.alibaba.fastjson.JSONObject;
import com.gw.pay.external.WechatPayExternalService;
import com.gw.pay.external.request.CreateOrderRequest;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayWithRequestPaymentResponse;
import com.wechat.pay.java.service.payments.model.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.Resource;
import java.math.BigDecimal;

@ActiveProfiles("local")
@SpringBootTest
class SpringbootWechatPayApplicationTests {

    @Resource
    private WechatPayExternalService wechatPayExternalService;


    @Test
    void prepayWithRequestPayment() {
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        createOrderRequest.setId(1001L);
        createOrderRequest.setOutTradeNo("100000004");
        createOrderRequest.setOpenId("oKwQd5MtFfgnXyLBp7vC6Pe3HAJQ");
        createOrderRequest.setPayMoney(new BigDecimal("0.01"));
        createOrderRequest.setPayContent("商机直租会员续费");
        PrepayWithRequestPaymentResponse response = wechatPayExternalService.prepayWithRequestPayment(createOrderRequest);
        System.out.println(JSONObject.toJSONString(response));
    }

    @Test
    void queryStatus() {
        Transaction result = wechatPayExternalService.queryStatus("100000004");
        System.out.println(JSONObject.toJSONString(result));
        if (Transaction.TradeStateEnum.SUCCESS.equals(result.getTradeState())) {
            System.out.println("支付成功");
        } else {
            System.out.println("支付失败");
        }
    }

    @Test
    void closeOrderV2() {
        wechatPayExternalService.closeOrder("100000004");
    }

}
