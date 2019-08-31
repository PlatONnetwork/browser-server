package com.platon.browser.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.platon.browser.req.address.QueryDetailRequest;
import com.platon.browser.res.BaseResp;
import com.platon.browser.res.address.QueryDetailResp;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 	地址接口方法统一申明                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
 *  @file AppDocAddress.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
@Api(value = "/address", tags = "Address")
public interface AppDocAddress {
	
	
    /**
     * @api {post}  /address/details a.查询地址详情
     * @apiVersion 1.0.0
     * @apiName addressDetails
     * @apiGroup address
     * @apiDescription
     * 1. 功能：查询地址详情<br/>
     * 2. 实现逻辑：<br/>
     * - 查询mysql中address表
     * @apiParamExample {json} Request-Example:
     * {
     *    "address":"0x"               //账户地址(必填)
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *    "errMsg":"",                 //描述信息
     *    "code":0,                    //成功（0），失败则由相关失败码
     *    "data":{
     *       "type":"",                //地址详情  1：账号   2：合约   3：内置合约
     *       "balance":"",             //余额(单位:LAT)
     *       "restrictingBalance":"",  //锁仓余额(单位:LAT)
     *       "stakingValue":"",        //质押的金额
     *       "delegateValue":"",       //委托的金额
     *       "redeemedValue":"",       //赎回中的金额
     *       "txQty":1288,             //交易总数
     *       "transferQty":11,         //转账交易总数
     *       "delegateQty":11,         //委托交易总数
     *       "stakingQty":11,          //验证人交易总数
     *       "proposalQty":11,         //治理交易总数
     *       "candidateCount":11,      //已委托验证人
     *       "delegateHes":"",         //未锁定委托（LAT）
     *       "delegateLocked":"",      //已锁定委托（LAT）
     *       "delegateUnlock":"",      //已解除委托（LAT）   
     *       "delegateReduction":""    //赎回中委托（LAT）   
     *       "contractName":"",        //合约名称
     *       "contractCreate":"",      //合约创建者地址
     *       "contractCreateHash":"",  //合约创建哈希
     *       "RPPlan":[
     *          {
     *             "epoch":11,         //锁仓周期
     *             "amount":111,       //锁定金额
     *             "blockNumber":11    //锁仓周期对应快高  结束周期 * epoch  
     *             "estimateTime":dfsdf   //预计时间
     *          }
     *       ]
     *    }
     * }
     */
	@ApiOperation(value = "address/details", nickname = "address details", notes = "", response = QueryDetailResp.class, tags = { "Address" })
	@RequestMapping(value = "address/details", produces = { "application/json" }, method = RequestMethod.POST)
	public BaseResp<QueryDetailResp> details(@ApiParam(value = "QueryDetailRequest ", required = true)@Valid @RequestBody QueryDetailRequest req);
}
