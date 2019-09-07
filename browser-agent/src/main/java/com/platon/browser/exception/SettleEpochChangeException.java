package com.platon.browser.exception;

/**
 * 结算周期切换异常
 * @Auther: Chendongming
 * @Date: 2019/8/17 16:27
 * @Description:
 */
public class SettleEpochChangeException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SettleEpochChangeException(String msg){
        super(msg);
    }
}
