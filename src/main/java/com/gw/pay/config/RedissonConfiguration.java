package com.gw.pay.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * Description: Redisson配置
 *
 * @author LinHuiBa-YanAn
 * @date 2023/10/12 14:46
 */
@Configuration
public class RedissonConfiguration {
    @Resource
    private RedisProperties redisProperties;

    private final static String DEFAULT_ADDRESS_FORMAT = "redis://%s:%s";

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer().setAddress(String.format(DEFAULT_ADDRESS_FORMAT, redisProperties.getHost(), redisProperties.getPort()));
        config.setCodec(new JsonJacksonCodec());
        return Redisson.create(config);
    }
}
