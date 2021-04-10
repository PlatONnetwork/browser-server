package com.platon.browser.exception;

/**
 * 	统一数据返回异常处理
 *  @file ResponseException.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class ResponseException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ResponseException(String msg){
        super(msg);
    }
}
