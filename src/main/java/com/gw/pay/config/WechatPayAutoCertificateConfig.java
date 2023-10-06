package com.gw.pay.config;

import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * Description: 初始化 具有自动下载并更新平台证书能力的RSA配置类并托关于spring
 *
 * @author LinHuiBa-YanAn
 * @date 2023/10/6 10:50
 */
@Slf4j
@Configuration
public class WechatPayAutoCertificateConfig {

    @Autowired
    private WechatPayProperties properties;

    @Autowired
    private ResourceLoader resourceLoader;

    private static final String CLASS_PATH = "classpath:";


    /**
     * 初始化商户配置
     * @return RSAAutoCertificateConfig
     */
    @Bean
    public RSAAutoCertificateConfig rsaAutoCertificateConfig() throws IOException {
        String privatePath = CLASS_PATH + properties.getPrivateKeyPath();
        Resource resourcePrivate = resourceLoader.getResource(privatePath);
        String privateKey = inputStreamToString(resourcePrivate.getInputStream());
        log.info("==========加载微信私钥配置:{}", privateKey);
        RSAAutoCertificateConfig config = new RSAAutoCertificateConfig.Builder()
                .merchantId(properties.getMerchantId())
                .privateKey(privateKey)
                .merchantSerialNumber(properties.getMerchantSerialNumber())
                .apiV3Key(properties.getApiV3key())
                .build();
        return config;
    }

    /**
     * 读取私钥文件，将文件流读取成string
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public String inputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        reader.close();
        return stringBuilder.toString();
    }

}
