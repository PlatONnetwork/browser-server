package com.platon.browser.exception;


/**
 * 配置加载异常
 * @Auther: Chendongming
 * @Date: 2019/8/27 14:24
 * @Description:
 */
public class HttpRequestException extends Exception {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public HttpRequestException(String msg){
		super(msg);
	}
}
