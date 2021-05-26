package com.platon.browser.exception;


/**
 * 合约调用成功，但结果为空
 * @Auther: Chendongming
 * @Date: 2019/8/27 14:24
 * @Description:
 */
public class BlankResponseException extends Exception {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public BlankResponseException(String msg){
		super(msg);
	}
}
