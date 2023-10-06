package com.gw.pay.controller;

import com.alibaba.fastjson.JSONObject;
import com.gw.pay.utils.HttpServletUtils;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.partnerpayments.app.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Description: 微信支付回调接口
 *
 * @author LinHuiBa-YanAn
 * @date 2023/10/6 11:18
 */
@RequestMapping("/api/v1/wechat/pay")
@Slf4j
public class WechatPayCallbackController {

    @Resource
    private RSAAutoCertificateConfig rsaAutoCertificateConfig;

    /**
     * 回调接口
     *
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/callback")
    public synchronized String callback(HttpServletRequest request) throws IOException {
        log.info("------收到支付通知------");
        // 请求头Wechatpay-Signature
        String signature = request.getHeader("Wechatpay-Signature");
        // 请求头Wechatpay-nonce
        String nonce = request.getHeader("Wechatpay-Nonce");
        // 请求头Wechatpay-Timestamp
        String timestamp = request.getHeader("Wechatpay-Timestamp");
        // 微信支付证书序列号
        String serial = request.getHeader("Wechatpay-Serial");
        // 签名方式
        String signType = request.getHeader("Wechatpay-Signature-Type");
        // 构造 RequestParam
        RequestParam requestParam = new RequestParam.Builder()
                .serialNumber(serial)
                .nonce(nonce)
                .signature(signature)
                .timestamp(timestamp)
                .signType(signType)
                .body(HttpServletUtils.getRequestBody(request))
                .build();

        // 初始化 NotificationParser
        NotificationParser parser = new NotificationParser(rsaAutoCertificateConfig);
        // 以支付通知回调为例，验签、解密并转换成 Transaction
        log.info("验签参数：{}", requestParam);
        Transaction transaction = parser.parse(requestParam, Transaction.class);
        log.info("验签成功！-支付回调结果：{}", transaction.toString());

        Map<String, String> returnMap = new HashMap<>(2);
        returnMap.put("code", "FAIL");
        returnMap.put("message", "失败");
        if (Transaction.TradeStateEnum.SUCCESS != transaction.getTradeState()) {
            log.info("内部订单号【{}】,微信支付订单号【{}】支付未成功", transaction.getOutTradeNo(), transaction.getTransactionId());
            return JSONObject.toJSONString(returnMap);
        }
        //todo 修改订单前，建议主动请求微信查询订单是否支付成功，防止恶意post
        //todo 修改订单信息
        returnMap.put("code", "SUCCESS");
        returnMap.put("message", "成功");
        return JSONObject.toJSONString(returnMap);
    }

}
