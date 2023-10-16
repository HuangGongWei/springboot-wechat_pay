package com.gw.pay.controller;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gw.pay.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Description: 微信支付回调接口
 *
 * @author LinHuiBa-YanAn
 * @date 2023/10/6 11:18
 */
@Slf4j
@RestController
@RequestMapping("/api/callback/wechat/pay")
public class WechatPayCallbackController {

    @Value("${config.bot.url}")
    private String botUrl;

    @Resource
    private OrderService orderService;

    /**
     * 回调接口
     *
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/callback", method = {RequestMethod.POST, RequestMethod.GET})
    public Map<String, String> payCallback(HttpServletRequest request, HttpServletResponse response) {
        log.info("------收到支付通知------");
        Map<String, String> result = new HashMap<>();
        try {
            orderService.payCallBack(request);
            result.put("code", "SUCCESS");
            result.put("message", "成功");
            return result;
        } catch (Exception e) {
            log.error("支付处理失败,req:{}", request, e);
            alarm();
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.put("code", "FAIL");
            result.put("message", e.getMessage());
            return result;
        }
    }

    /**
     * 企业微信群告警
     */
    private void alarm() {
        JSONObject messageReq = new JSONObject();
        messageReq.put("msgtype", "text");
        JSONObject text = new JSONObject();
        text.put("content", "【商业直租】支付处理失败！" );
        messageReq.put("text", text);
        String url = botUrl;
        String reqStr = JSON.toJSONString(messageReq);
        HttpUtil.post(url, reqStr, 30000);
    }

}
