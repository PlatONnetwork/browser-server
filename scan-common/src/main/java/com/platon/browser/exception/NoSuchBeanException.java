package com.platon.browser.exception;

/**
 * 业务Bean实例创建或更新异常
 * @Auther: Chendongming
 * @Date: 2019/8/15 16:00
 * @Description:
 */
public class NoSuchBeanException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoSuchBeanException(String msg){
        super(msg);
    }
}
