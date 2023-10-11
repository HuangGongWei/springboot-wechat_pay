package com.gw.pay;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.payments.jsapi.JsapiService;
import com.wechat.pay.java.service.payments.jsapi.model.Amount;
import com.wechat.pay.java.service.payments.jsapi.model.Payer;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayRequest;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayResponse;

/**
 * Description: 快速开始
 *
 * @author LinHuiBa-YanAn
 * @date 2023/10/6 09:20
 */
public class QuickStart {
    /** 商户号 */
    public static String merchantId = "1593325251";
    /** 商户API私钥路径 */
    public static String privateKeyPath = "/Users/gwh/Documents/yanAn/学习资料/pay/微信支付/code/springboot-wechat_pay/src/main/resources/apiclient_key.pem";
    /** 商户证书序列号 */
    public static String merchantSerialNumber = "609A195D957DD8C742EC0630146A7A6E11AE7D20";
    /** 商户APIV3密钥 */
    public static String apiV3Key = "NQdGz12xxyTDgZ5HExLCFSN0VqGPBC82";


    public static void main(String[] args) {
        // 使用自动更新平台证书的RSA配置
        // 一个商户号只能初始化一个配置，否则会因为重复的下载任务报错
        Config config =
                new RSAAutoCertificateConfig.Builder()
                        .merchantId(merchantId)
                        .privateKeyFromPath(privateKeyPath)
                        .merchantSerialNumber(merchantSerialNumber)
                        .apiV3Key(apiV3Key)
                        .build();
        JsapiService service = new JsapiService.Builder().config(config).build();
        // request.setXxx(val)设置所需参数，具体参数可见Request定义
        PrepayRequest request = new PrepayRequest();
        Amount amount = new Amount();
        amount.setTotal(100);
        request.setAmount(amount);
        request.setAppid("wx207d04e11b36865e");
        request.setMchid("1593325251");
        request.setDescription("测试商品标题");
        request.setNotifyUrl("https://notify_url");
        request.setOutTradeNo("out_trade_no_001");
        Payer payer = new Payer();
        payer.setOpenid("osgfR5MY1Xl39-LvwbU2bc2Kma4U");
        request.setPayer(payer);
        PrepayResponse response = service.prepay(request);
        System.out.println(response.getPrepayId());
    }
}
