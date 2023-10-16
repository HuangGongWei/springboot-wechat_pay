package com.gw.pay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Description: Redis配置
 *
 * @author LinHuiBa-YanAn
 * @date 2023/10/6 10:27
 */
@Data
@Component
@ConfigurationProperties(prefix = "redis")
public class RedisProperties {
    /**
     * 主机
     */
    private String host;
    /**
     * 端口
     */
    private String port;
}
