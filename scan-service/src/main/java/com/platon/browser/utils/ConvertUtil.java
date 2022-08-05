package com.platon.browser.utils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 数据转换工具类
 *
 * @author zhangrj
 * @file ConvertUtil.java
 * @description
 * @data 2019年8月31日
 */
public class ConvertUtil {

    private ConvertUtil() {
    }

    public static BigInteger hexToBigInteger(String hexStr) {
        hexStr = hexStr.replace("0x", "");
        return new BigInteger(hexStr, 16);
    }

    public static String captureName(String name) {
        if (StringUtils.isBlank(name)) {
            return "";
        }
        char[] cs = name.toCharArray();
        if (cs[0] >= 'a' && cs[0] <= 'z') {
            cs[0] = (char) (cs[0] - 32);
        }
        return String.valueOf(cs);

    }

    /**
     * 精度转换
     *
     * @param value  原值
     * @param factor 精度（多少个0）
     * @return java.math.BigDecimal
     * @date 2021/3/30
     */
    public static BigDecimal convertByFactor(BigDecimal value, int factor) {
        return new BigDecimal(EnergonUtil.format(value.divide(BigDecimal.TEN.pow(factor)).setScale(12, BigDecimal.ROUND_DOWN), 12));
    }

}
