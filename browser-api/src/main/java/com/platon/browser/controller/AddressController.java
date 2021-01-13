package com.platon.browser.controller;

import com.platon.browser.constant.Browser;
import com.platon.browser.config.CommonMethod;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.request.address.QueryDetailRequest;
import com.platon.browser.request.address.QueryRPPlanDetailRequest;
import com.platon.browser.response.BaseResp;
import com.platon.browser.response.address.QueryDetailResp;
import com.platon.browser.response.address.QueryRPPlanDetailResp;
import com.platon.browser.service.AddressService;
import com.platon.browser.utils.I18nUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.WebAsyncTask;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.concurrent.Callable;

/**
 *          地址具体实现Controller 提供地址详情页面使用
 *  @file AppDocAddressController.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
@RestController
public class AddressController {

	@Resource
	private AddressService addressService;
    @Resource
    private I18nUtil i18n;

    @PostMapping(value = "address/details")
	public WebAsyncTask<BaseResp<QueryDetailResp>> details(@Valid @RequestBody QueryDetailRequest req) {
		/**
		 * 异步调用，超时则进入timeout  
		 */
        WebAsyncTask<BaseResp<QueryDetailResp>> webAsyncTask = new WebAsyncTask<>(Browser.WEB_TIME_OUT, () -> {
            QueryDetailResp queryDetailResp = addressService.getDetails(req);
            return BaseResp.build(RetEnum.RET_SUCCESS.getCode(), i18n.i(I18nEnum.SUCCESS), queryDetailResp);
        });
        CommonMethod.onTimeOut(webAsyncTask);
        return webAsyncTask;  
	}

    @PostMapping(value = "address/rpplanDetail")
	public WebAsyncTask<BaseResp<QueryRPPlanDetailResp>> rpplanDetail(@Valid @RequestBody QueryRPPlanDetailRequest req) {
		/**
		 * 异步调用，超时则进入timeout  
		 */
        WebAsyncTask<BaseResp<QueryRPPlanDetailResp>> webAsyncTask = new WebAsyncTask<>(Browser.WEB_TIME_OUT, new Callable<BaseResp<QueryRPPlanDetailResp>>() {
            @Override  
            public BaseResp<QueryRPPlanDetailResp> call() throws Exception {  
            	QueryRPPlanDetailResp dueryRPPlanDetailResp = addressService.rpplanDetail(req);
        		return BaseResp.build(RetEnum.RET_SUCCESS.getCode(), i18n.i(I18nEnum.SUCCESS), dueryRPPlanDetailResp);
            }  
        });  
        CommonMethod.onTimeOut(webAsyncTask);
        return webAsyncTask;
	}

}
