package com.platon.browser.exception;

/**
 * 增发周期切换异常
 * @Auther: Chendongming
 * @Date: 2019/8/17 16:27
 * @Description:
 */
public class IssueEpochChangeException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public IssueEpochChangeException(String msg){
        super(msg);
    }

}
