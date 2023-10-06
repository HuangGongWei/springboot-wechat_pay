package com.gw.pay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Description: 微信支付配置类
 *
 * @author LinHuiBa-YanAn
 * @date 2023/10/6 10:27
 */
@Data
@Component
@ConfigurationProperties(prefix = "wechat.pay")
public class WechatPayProperties {
    /**
     * 微信公众号或者小程序等的appId
     */
    private String appId;
    /**
     * 微信支付商户号
     */
    private String merchantId;
    /**
     * 商户API私钥
     */
    private String privateKeyPath;
    /**
     * 商户证书序列号
     */
    private String merchantSerialNumber;
    /**
     * 商户APIv3密钥
     */
    private String apiV3key;
    /**
     * 支付回调通知地址
     */
    private String payNotifyUrl;
}
