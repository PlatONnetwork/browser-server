package com.platon.browser.config;

import java.util.concurrent.TimeoutException;

import org.springframework.web.context.request.async.WebAsyncTask;

public class CommonMethod {
	private CommonMethod(){}
	public static <T> void onTimeOut(WebAsyncTask<T> webAsyncTask) {
		webAsyncTask.onTimeout(() -> {
			// 超时的时候，直接抛异常，让外层统一处理超时异常
			throw new TimeoutException("System busy!");
		});
	}
}
