package com.platon.browserweb.common.util;

import com.platon.browserweb.common.constant.SystemConstants;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * <p>Calculator.java</p>
 * <p/>
 * <p>Description:</p>
 * <p/>
 * <p>Copyright (c) 2017. juzix.io. All rights reserved.</p>
 * <p/>
 *
 * @version 1.0.0
 * @author: lvxiaoyi
 * <p/>
 * Revision History:
 * 2018/3/24, lvxiaoyi, Initial Version.
 */
public class Calculator {

    /**
     * 计算最小单位整数倍数量，输入0.05，最小价格单位：0.02，，只显示0.04
     * @param value 数量（金额/数量）
     * @param price 价格（最小价格单位/最小交易单位）
     * @return 整数倍数量
     */
    public static BigDecimal calQty(BigDecimal value, BigDecimal price) {
        return value.divide(price, 0, BigDecimal.ROUND_DOWN).multiply(price);
    }

    /**
     * 计算可交易的数量，舍去精度以外的小数
     *
     * @param threshold
     * @param price
     * @return
     */
    public static BigDecimal calTradableVolume(BigDecimal threshold, BigDecimal price) {
        return threshold.divide(price, SystemConstants.DEFAULT_PRECISION, BigDecimal.ROUND_DOWN);
    }

    /**
     * 计算成交金额，结果四舍五入
     *
     * @param price
     * @param volume
     * @return
     */
    public static BigDecimal calTurnover(BigDecimal price, BigDecimal volume) {
        return price.multiply(volume).setScale(SystemConstants.DEFAULT_PRECISION, BigDecimal.ROUND_HALF_UP);
    }


    public static BigDecimal multiply(BigDecimal value, BigDecimal multiplicand) {
        return value.multiply(multiplicand).setScale(SystemConstants.DEFAULT_PRECISION, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal divide(BigDecimal value, BigDecimal divisor) {
        if (BigDecimal.ZERO.compareTo(divisor) == 0) {
            return BigDecimal.ZERO;
        }
        return value.divide(divisor, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 计算均价，结果四舍五入
     *
     * @param turnover
     * @param volume
     * @return
     */
    public static BigDecimal calAvgPrice(BigDecimal turnover, BigDecimal volume) {
        return turnover.divide(volume, SystemConstants.DEFAULT_PRECISION, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 计算手续费，结果四舍五入
     *
     * @param turnover
     * @param feeRate
     * @return
     */
    public static BigDecimal calFee(BigDecimal turnover, BigDecimal feeRate) {
        return turnover.multiply(feeRate).setScale(SystemConstants.DEFAULT_PRECISION, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 根据数量返回冷静期幅度数量
     * @param quoteLimit 格式： [-100)=50|[100-200)=70|[200-)=80
     * @param lastPrice 最新成交价
     * @return 冷静期幅度数量
     */
    public static BigDecimal calQuoteLimit(String quoteLimit, BigDecimal lastPrice) {
        String [] strings = quoteLimit.replaceAll(" ", "").split("\\|");
        for (String string : strings) {
            int kuohao = string.indexOf(")");
            String[] arrays = string.split("-");
            String max = arrays[1].substring(0, kuohao - arrays[0].length() - 1);
            if (StringUtils.isBlank(max)) {
                // 最后一个
                String value = string.substring(kuohao + 2, string.length());
                return FormatUtils.toBigDecimal(value);
            }
            BigDecimal maxBigDecimal = FormatUtils.toBigDecimal(max);
            BigDecimal minBigDecimal = FormatUtils.toBigDecimal(arrays[0].substring(1));
            if (maxBigDecimal.compareTo(lastPrice) > 0 && minBigDecimal.compareTo(lastPrice) <= 0) {
                String value = string.substring(kuohao + 2, string.length());
                return FormatUtils.toBigDecimal(value);
            }
        }
        return BigDecimal.ZERO;
    }
    
    /**
     * 截取掉多余的 0,转换为字符串
     */
    public static String interceptZeros(BigDecimal value){
    	return value.stripTrailingZeros().toPlainString();
    }
    
    /**
     * 将数值转换为百分百给前端显示
     */
    public static BigDecimal parsePercent(BigDecimal value) {
    	return value.multiply(new BigDecimal(100));
    }
    
    /**
     * 将百分百转换为数值插入到数据库
     */
    public static BigDecimal parseNumber(BigDecimal value) {
    	return value.divide(new BigDecimal(100));
    }
}
