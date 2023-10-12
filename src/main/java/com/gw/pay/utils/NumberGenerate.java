package com.gw.pay.utils;

import cn.hutool.core.util.RandomUtil;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Description: 数字生成器
 *
 * @author LinHuiBa-YanAn
 * @date 2023/10/11 20:58
 */
public class NumberGenerate {

    /**
     * 生成 32 位UUID字符串
     *
     * @return 32位字符
     */
    public static String generateUUID32() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "").substring(0, 32);
    }

    /**
     * 随机生成5位数字
     *
     * @return 5位数字
     */
    public static Long generatorRandomNum() {
        ThreadLocalRandom random = RandomUtil.getRandom();
        return random.nextLong(10000, 99999);
    }
}
