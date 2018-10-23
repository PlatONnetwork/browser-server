package com.platon.browserweb.common.enums;

import java.util.Calendar;

public enum KlineType {
	MIN_1("quote_1_min", Calendar.MINUTE, 0, -1, Calendar.SECOND, Calendar.MILLISECOND),
	MIN_3("quote_3_min", Calendar.MINUTE, 3, -3, Calendar.SECOND, Calendar.MILLISECOND),
	MIN_5("quote_5_min", Calendar.MINUTE, 5, -5, Calendar.SECOND, Calendar.MILLISECOND),
	MIN_10("quote_10_min", Calendar.MINUTE, 10, -10, Calendar.SECOND, Calendar.MILLISECOND),
	MIN_12("quote_12_min", Calendar.MINUTE, 12, -12, Calendar.SECOND, Calendar.MILLISECOND),
	MIN_15("quote_15_min", Calendar.MINUTE, 15, -15, Calendar.SECOND, Calendar.MILLISECOND),
	MIN_30("quote_30_min", Calendar.MINUTE, 30, -30, Calendar.SECOND, Calendar.MILLISECOND),
	HOUR_1("quote_1_hour", Calendar.HOUR_OF_DAY, 0, -1, Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND),
	HOUR_2("quote_2_hour", Calendar.HOUR_OF_DAY, 2, -2, Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND),
	HOUR_4("quote_4_hour", Calendar.HOUR_OF_DAY, 4, -4, Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND),
	HOUR_6("quote_6_hour", Calendar.HOUR_OF_DAY, 6, -6, Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND),
	HOUR_12("quote_12_hour", Calendar.HOUR_OF_DAY, 12, -12, Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND),
	DAILY("quote_daily", Calendar.DAY_OF_MONTH, 0, -1, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND),
	WEEKLY("quote_weekly", Calendar.DAY_OF_WEEK, 0,  -1, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND);

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

	KlineType(String tableName, int identify, int currentDiff, int previousDiff, int... zeros) {
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
