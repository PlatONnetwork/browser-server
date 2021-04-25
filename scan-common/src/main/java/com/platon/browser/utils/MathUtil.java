package com.platon.browser.utils;

import cn.hutool.core.util.ObjectUtil;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 数学工具类
 *
 * @author huangyongpeng@matrixelements.com
 * @date 2021/4/13
 */
public class MathUtil {

    /**
     * 值是否为null或0
     *
     * @param value 值
     * @return boolean 是：true，否：false
     * @author huangyongpeng@matrixelements.com
     * @date 2021/4/13
     */
    public static boolean isZeroOrNull(BigDecimal value) {
        if (ObjectUtil.isNull(value) || value.compareTo(BigDecimal.ZERO) == 0) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 判断是否为0
     *
     * @param value 值
     * @return boolean 是：true，否：false
     * @author huangyongpeng@matrixelements.com
     * @date 2021/4/13
     */
    public static boolean isZero(BigDecimal value) {
        if (ObjectUtil.isNotNull(value) && value.compareTo(BigDecimal.ZERO) == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否为0
     *
     * @param value 值
     * @return boolean 是：true，否：false
     * @author huangyongpeng@matrixelements.com
     * @date 2021/4/13
     */
    public static boolean isZero(BigInteger value) {
        if (ObjectUtil.isNotNull(value) && value.compareTo(BigInteger.ZERO) == 0) {
            return true;
        } else {
            return false;
        }
    }

}
