package com.platon.browser.old.exception;

/**
 *
 * @Auther: Chendongming
 * @Date: 2019/8/17 16:27
 * @Description: 选举周期切换异常
 */
public class ElectionEpochChangeException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ElectionEpochChangeException(String msg){
        super(msg);
    }
}
