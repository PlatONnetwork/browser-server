package com.platon.browser.controller;

import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.WebAsyncTask;

import com.platon.browser.common.BrowserConst;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.now.service.ExtraService;
import com.platon.browser.res.BaseResp;
import com.platon.browser.res.extra.QueryConfigResp;
import com.platon.browser.util.I18nUtil;

/**
 * controller实现
 *  @file AppDocExtraController.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年11月25日
 */
@RestController
public class AppDocExtraController implements AppDocExtra {

	@Autowired
	private ExtraService extraService;
	
	@Autowired
    private I18nUtil i18n;
	
	@Override
	public WebAsyncTask<BaseResp<QueryConfigResp>> queryConfig() {
		// 5s钟没返回，则认为超时  
        WebAsyncTask<BaseResp<QueryConfigResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, () -> {
        	QueryConfigResp queryConfigResp = extraService.queryConfig();
            return BaseResp.build(RetEnum.RET_SUCCESS.getCode(), i18n.i(I18nEnum.SUCCESS), queryConfigResp);
        });
        webAsyncTask.onCompletion(() -> {
        });
        webAsyncTask.onTimeout(() -> {
            // 超时的时候，直接抛异常，让外层统一处理超时异常
            throw new TimeoutException("System busy!");
        });
        return webAsyncTask;  
	}

}
