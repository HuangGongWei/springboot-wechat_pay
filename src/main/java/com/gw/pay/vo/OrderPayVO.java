package com.gw.pay.vo;

import lombok.Data;

/**
 * Description: 创建订单支付响应信息
 *
 * @author LinHuiBa-YanAn
 * @date 2023/10/11 21:35
 */
@Data
public class OrderPayVO {
    /**
     * 时间戳，标准北京时间，时区为东八区，自1970年1月1日 0点0分0秒以来的秒数。
     */
    private String timestamp;
    /**
     * 随机字符串，不长于32位。
     */
    private String nonceStr;
    /**
     * JSAPI下单接口返回的prepay_id参数值，提交格式如：prepay_id=***
     */
    private String packageVal;
    /**
     * 签名类型，默认为RSA，仅支持RSA。
     */
    private String signType;
    /**
     * 签名，使用字段AppID、timeStamp、nonceStr、package计算得出的签名值
     */
    private String paySign;
}
