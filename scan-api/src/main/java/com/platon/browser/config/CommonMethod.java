package com.platon.browser.config;

import com.platon.browser.enums.I18nEnum;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.utils.HttpUtil;
import com.platon.browser.utils.I18nUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.WebAsyncTask;

import java.util.concurrent.TimeoutException;

/**
 * 统一静态方法
 *
 * @author zhangrj
 * @file CommonMethod.java
 * @description
 * @data 2019年12月19日
 */
@Slf4j
@Service
public class CommonMethod {

	@Value("${recaptchaUrl:}")
	private String recaptchaUrl;

	@Value("${isRecaptcha:false}")
	private Boolean isRecaptcha;

	@Autowired
	private I18nUtil i18n;

	private CommonMethod() {
	}

	/**
	 * web异步请求超时方法调用
	 *
	 * @param webAsyncTask
	 * @method onTimeOut
	 */
	public static <T> void onTimeOut(WebAsyncTask<T> webAsyncTask) {
		webAsyncTask.onTimeout(() -> {
			// 超时的时候，直接抛异常，让外层统一处理超时异常
			throw new TimeoutException("System busy!");
		});
	}

	/**
	 * recaptcha鉴权方法
	 *
	 * @param token
	 * @method recaptchaAuth
	 */
	public void recaptchaAuth(String token) {
		try {
			if (!this.isRecaptcha) {
				log.debug("not recaptchaAuth ");
				return;
			}
			RecaptchaDto recaptchaDto = HttpUtil.get(String.format(this.recaptchaUrl, token), RecaptchaDto.class);
			log.debug("recaptchaAuth token:{}", token);
			if (!recaptchaDto.getSuccess().booleanValue()) {
				throw new BusinessException(this.i18n.i(I18nEnum.DOWNLOAD_EXCEPTION));
			}
		} catch (Exception e1) {
			log.error("google recaptchaAuth error.", e1);
			throw new BusinessException(this.i18n.i(I18nEnum.DOWNLOAD_EXCEPTION));
		}
	}
}
