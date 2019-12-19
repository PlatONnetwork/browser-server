package com.platon.browser.controller;

import com.platon.browser.res.BaseResp;
import com.platon.browser.res.extra.QueryConfigResp;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.async.WebAsyncTask;


/**
 * 	更多模块方法统一申明                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
 *  @file AppDocExtra.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
@Api(value = "/extra", tags = "Extra")
public interface AppDocExtra {
	
	
    /**
     * @api {post}  /extra/queryConfig a.查询配置详情
     * @apiVersion 1.0.0
     * @apiName queryConfig
     * @apiGroup extra
     * @apiDescription
     * 1. 功能：查询配置详情<br/>
     * 2. 实现逻辑：<br/>
     * - 查询mysql中config表
     * @apiParamExample {json} Request-Example:
     * {
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *    "errMsg":"",                 //描述信息
     *    "code":0,                    //成功（0），失败则由相关失败码
     *    "data":{
     *       "config":{					//配置详情
     *       [
     *       	"module":"Staking",  //模块名
     *       	"detail":{
     *       		"name":"",//参数名称
     *       		"initValue":"",//初始值
     *       		"staleValue":"",//旧值
     *       		"value":"",//新值
     *       		"startValue":"",//开始值
     *       		"endValue":"",//结束值
     *       		"start":"[", //是否包含开始
     *       		"end":")" //是否包含结束
     *       	}
     *       ]
     *       }
     *    }
     * }
     */
	@ApiOperation(value = "extra/queryConfig", nickname = "extra queryConfig", notes = "", response = QueryConfigResp.class, tags = { "Extra" })
	@PostMapping(value = "extra/queryConfig", produces = { "application/json" })
	WebAsyncTask<BaseResp<QueryConfigResp>> queryConfig();
	
	
}
