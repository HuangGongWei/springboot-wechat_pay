package com.gw.pay.enums;

/**
 * Description: 官网：https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay4_1.shtml
 *
 * @author LinHuiBa-YanAn
 * @date 2023/10/7 15:44
 */
public class WechatPayCallBackHeaderConstant {
    /**
     * HTTP头Wechatpay-Nonce 中的应答随机串。
     */
    public static final String NONCE = "Wechatpay-Nonce";
    /**
     * 微信支付的平台证书序列号位于HTTP头Wechatpay-Serial
     */
    public static final String SERIAL = "Wechatpay-Serial";
    /**
     * 微信支付的应答签名通过HTTP头Wechatpay-Signature传递
     */
    public static final String SIGNATURE = "Wechatpay-Signature";
    /**
     * 微信支付 签名类型
     */
    public static final String SIGNATURE_TYPE = "Wechatpay-Signature-Type";

    /**
     * HTTP头Wechatpay-Timestamp 中的应答时间戳。
     */
    public static final String TIMESTAMP = "Wechatpay-Timestamp";
}
