package com.platon.browser.config;

import java.util.concurrent.TimeoutException;

import org.springframework.web.context.request.async.WebAsyncTask;

/**
 * 统一静态方法
 *  @file CommonMethod.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年12月19日
 */
public class CommonMethod {
	private CommonMethod(){}
	/**
	 * web异步请求超时方法调用
	 * @method onTimeOut
	 * @param webAsyncTask
	 */
	public static <T> void onTimeOut(WebAsyncTask<T> webAsyncTask) {
		webAsyncTask.onTimeout(() -> {
			// 超时的时候，直接抛异常，让外层统一处理超时异常
			throw new TimeoutException("System busy!");
		});
	}
}
