package com.platon.browserweb.common.enums;

import java.util.Calendar;
import java.util.Date;

/**
 * User: dongqile
 * Date: 2018/7/23
 * Time: 9:59
 */
public enum StatisticsType {
    MIN_5_COUNT("statistics_broker_5_min", Calendar.MINUTE, 5, -5, Calendar.SECOND, Calendar.MILLISECOND),
    MIN_10_COUNT("statistics_broker_10_min", Calendar.MINUTE, 10, -10, Calendar.SECOND, Calendar.MILLISECOND),
    HOUR_1_COUNT("statistics_broker_60_min", Calendar.HOUR_OF_DAY, 0, -1, Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND),
    HOUR_2_COUNT("statistics_broker_120_min", Calendar.HOUR_OF_DAY, 2, -2, Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND),
    HOUR_4_COUNT("statistics_broker_240_min", Calendar.HOUR_OF_DAY, 4, -4, Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND),
    DAILY_COUNT("statistics_broker_1440_min", Calendar.DAY_OF_MONTH, 0, -1, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND);

    /**
     * 对应数据库表名
     */
    private String tableName;
    /**
     * 当前枚举代表的日期位
     */
    private int identify;
    /**
     * 间隔时间，0（维持原值不变）
     */
    private int currentDiff;
    /**
     * 前一个时间差值
     */
    private int previousDiff;
    /**
     * 设置为0的日期位
     */
    private int[] zeros;

    StatisticsType(String tableName, int identify, int currentDiff, int previousDiff, int... zeros) {
        this.tableName = tableName;
        this.identify = identify;
        this.currentDiff = currentDiff;
        this.previousDiff = previousDiff;
        this.zeros = zeros;
    }

    public String getTableName() {
        return tableName;
    }

    public int getIdentify() {
        return identify;
    }

    public int getCurrentDiff() {
        return currentDiff;
    }

    public int getPreviousDiff() {
        return previousDiff;
    }

    public int[] getZeros() {
        return zeros;
    }



}