package com.platon.browser.common;

/**
 * 统一const定义
 *  @file BrowserConst.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class BrowserConst {
	private BrowserConst(){}

	/** 身份标识外部引用url */
	public static final String EX_URL = "https://keybase.io/";
	
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
	
	/** pip前缀*/
	public static final String PIP_NAME = "PIP-";
	
}
