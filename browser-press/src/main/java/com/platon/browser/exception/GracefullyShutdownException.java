package com.platon.browser.exception;

/**
 * @Auther: Chendongming
 * @Date: 2019/9/27 20:09
 * @Description: 优雅停机异常
 */
public class GracefullyShutdownException extends Exception {
    /**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public GracefullyShutdownException(String msg){
        super(msg);
    }
}