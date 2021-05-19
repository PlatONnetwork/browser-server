package com.platon.browser.exception;


/**
 * 配置加载异常
 * @Auther: Chendongming
 * @Date: 2019/8/27 14:24
 * @Description:
 */
public class ConfigLoadingException extends Exception {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public ConfigLoadingException(String msg){
		super(msg);
	}
}
