package com.gw.pay;

import com.alibaba.fastjson.JSONObject;
import com.gw.pay.external.WechatPayExternalService;
import com.gw.pay.external.WechatPayV2ExternalService;
import com.gw.pay.external.request.OrderRequest;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayResponse;
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

    @Resource
    private WechatPayV2ExternalService wechatPayV2ExternalService;

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


    @Test
    void prepayWithRequestPayment() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setId(1001L);
        orderRequest.setOutTradeNo("100000002");
        orderRequest.setOpenId("oKwQd5MtFfgnXyLBp7vC6Pe3HAJQ");
        orderRequest.setPayMoney(new BigDecimal("0.01"));
        orderRequest.setPayContent("商机直租会员续费");
        PrepayWithRequestPaymentResponse response = wechatPayV2ExternalService.prepayWithRequestPayment(orderRequest);
        System.out.println(JSONObject.toJSONString(response));
    }

    @Test
    void queryStatus() {
        Transaction result = wechatPayV2ExternalService.queryStatus("100000002");
        System.out.println(JSONObject.toJSONString(result));
        if (Transaction.TradeStateEnum.SUCCESS.equals(result.getTradeState())) {
            System.out.println("支付成功");
        } else {
            System.out.println("支付失败");
        }
    }

    @Test
    void closeOrderV2() {
        wechatPayV2ExternalService.closeOrder("100000002");
    }

}
