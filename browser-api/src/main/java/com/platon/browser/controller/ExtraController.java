package com.platon.browser.controller;

import com.platon.browser.constant.Browser;
import com.platon.browser.config.CommonMethod;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.response.BaseResp;
import com.platon.browser.response.extra.QueryConfigResp;
import com.platon.browser.service.ExtraService;
import com.platon.browser.utils.I18nUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.WebAsyncTask;

import javax.annotation.Resource;

/**
 * controller实现
 *  @file AppDocExtraController.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年11月25日
 */
@RestController
public class ExtraController {

	@Resource
	private ExtraService extraService;
	@Resource
    private I18nUtil i18n;

	@PostMapping(value = "extra/queryConfig")
	public WebAsyncTask<BaseResp<QueryConfigResp>> queryConfig() {
		/**
		 * 异步调用，超时则进入timeout  
		 */
        WebAsyncTask<BaseResp<QueryConfigResp>> webAsyncTask = new WebAsyncTask<>(Browser.WEB_TIME_OUT, () -> {
        	QueryConfigResp queryConfigResp = extraService.queryConfig();
            return BaseResp.build(RetEnum.RET_SUCCESS.getCode(), i18n.i(I18nEnum.SUCCESS), queryConfigResp);
        });
        CommonMethod.onTimeOut(webAsyncTask);
        return webAsyncTask;  
	}

}
