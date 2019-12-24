package com.platon.browser.config;

import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.WebAsyncTask;

import com.platon.browser.enums.I18nEnum;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.exception.HttpRequestException;
import com.platon.browser.util.HttpUtil;
import com.platon.browser.util.I18nUtil;

/**
 * 统一静态方法
 *  @file CommonMethod.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年12月19日
 */
@Service
public class CommonMethod {
	
	@Value("${recaptchaUrl:}")
	private String recaptchaUrl;
	
	@Autowired
	private I18nUtil i18n;
	
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
	
	/**
	 * recaptcha鉴权方法
	 * @method onTimeOut
	 * @param webAsyncTask
	 */
	public void recaptchaAuth(String token) {
		try {
			RecaptchaDto recaptchaDto = HttpUtil.get(String.format(recaptchaUrl, token), RecaptchaDto.class);
			if(!recaptchaDto.getSuccess().booleanValue()) {
				throw new BusinessException(i18n.i(I18nEnum.DOWNLOAD_EXCEPTION));
			}
		} catch (HttpRequestException e1) {
			e1.printStackTrace();
		}
	}
}
