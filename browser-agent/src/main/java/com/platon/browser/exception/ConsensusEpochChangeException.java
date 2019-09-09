package com.platon.browser.exception;

/**
 *
 * @Auther: Chendongming
 * @Date: 2019/8/17 16:27
 * @Description: 共识周期切换异常
 */
public class ConsensusEpochChangeException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ConsensusEpochChangeException(String msg){
        super(msg);
    }
}
