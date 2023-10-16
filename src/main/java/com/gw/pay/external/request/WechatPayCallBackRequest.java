package com.gw.pay.external.request;

import lombok.Data;

/**
 * Description: 微信回调对象 与WxCallBackHeader对应。
 *
 * @author LinHuiBa-YanAn
 * @date 2023/10/16 20:15
 */
@Data
public class WechatPayCallBackRequest {
    private String body;
    private String nonce;
    private String serial;
    private String signature;
    private String timestamp;
    private String signatureType;
}
