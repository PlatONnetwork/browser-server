package com.platon.browser.exception;


/**
 * 合约调用异常
 * @Auther: Chendongming
 * @Date: 2019/8/27 14:24
 * @Description:
 */
public class ContractInvokeException extends Exception {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public ContractInvokeException(String msg){
		super(msg);
	}
}
