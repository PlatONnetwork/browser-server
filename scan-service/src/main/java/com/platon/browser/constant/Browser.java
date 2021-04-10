package com.platon.browser.constant;

import java.math.BigDecimal;

/**
 * 统一const定义
 *  @file BrowserConst.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class Browser {
	private Browser(){}

	/** 查询缓存最大数量 */
	public static final Integer MAX_NUM = 500000;
	
	/** http头*/
	public static final String HTTP = "http://";
	
	/** https头*/
	public static final String HTTPS = "https://";
	
	/** pre*/
	public static final String WALLET_PRX = "0x";
	
	/** 操作表分隔符*/
	public static final String OPT_SPILT = "\\|";
	
	/** 操作表分隔符*/
	public static final  String PEAD_SPILT = "|";
	
	/** http分隔符*/
	public static final String HTTP_SPILT = ",";
	
	/** pip前缀*/
	public static final String PIP_NAME = "PIP-";
	
	/**账户*/
	public static final String ACCOUNT = "lat1zqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqp7pn3ep,lax1zqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqp7pn3ep";
	
	/**默认提案标题*/
	public static final String INQUIRY = "inquiry";
	
	/**WEB超时时间*/
	public static final Integer WEB_TIME_OUT = 30000;

	/**
	 * lat转换的name名
	 */
	public static final String EXTRA_LAT_PARAM = "stakeThreshold,operatingThreshold,minimumRelease";

	/**
	 * 百分比转换的name名
	 */
	public static final String EXTRA_PECENT_PARAM = "slashFractionDuplicateSign,duplicateSignReportReward";

	/**
	 * 被除参数
	 */
	public static final BigDecimal PERCENTAGE = new BigDecimal(100);

	/**
	 * 设置ERC20的key
	 */
	public static final String ERC_BALANCE_KEY = "erc20";

	/**
	 * erc20参数分隔符
	 */
	public static final String ERC_SPILT = "#";

}
