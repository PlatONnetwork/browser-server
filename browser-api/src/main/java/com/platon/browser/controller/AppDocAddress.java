package com.platon.browser.controller;

import com.platon.browser.req.address.QueryDetailRequest;
import com.platon.browser.req.address.QueryRPPlanDetailRequest;
import com.platon.browser.res.BaseResp;
import com.platon.browser.res.address.QueryDetailResp;
import com.platon.browser.res.address.QueryRPPlanDetailResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.async.WebAsyncTask;

import javax.validation.Valid;

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
     *       "type":"",                //地址类型  1：账号    2：内置合约  3：evm合约  4:wasm合约
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
     *       "delegateReleased":"",    //待赎回委托（LAT）   
     *       "delegateClaim":"",      //待领取奖励（LAT）   
     *       "haveReward":"",      //累积领取委托奖励（LAT）   
     *       "contractName":"",        //合约名称
     *       "contractCreate":"",      //合约创建者地址
     *       "contractCreateHash":"",  //合约创建哈希
     *       "contractBin":"",  //合约bin
     *       "isRestricting":"",  //是否有锁仓交易 0-无  1-有
     *       "isDestroy":"",  //是否被销毁 1是自毁，0是正常
     *       "destroyHash":""  //销毁hash
     *    }
     * }
     */
	@ApiOperation(value = "address/details", nickname = "address details", notes = "", response = QueryDetailResp.class, tags = { "Address" })
	@PostMapping(value = "address/details", produces = { "application/json" })
	WebAsyncTask<BaseResp<QueryDetailResp>> details(@ApiParam(value = "QueryDetailRequest ", required = true)@Valid @RequestBody QueryDetailRequest req);
	
	/**
     * @api {post}  /address/rpplanDetail b.查询地址锁仓详情
     * @apiVersion 1.0.0
     * @apiName rpplanDetail
     * @apiGroup address
     * @apiDescription
     * 1. 功能：查询锁仓详情<br/>
     * 2. 实现逻辑：<br/>
     * - 查询mysql中rpplan表 
     * - 查询链上实时余额
     * @apiParamExample {json} Request-Example:
     * {
     *    "address":"0x",               //账户地址(必填)
     *    "pageNo":"",  //页号
     *    "pageSize":"" //页码
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *    "errMsg":"",                 //描述信息
     *    "code":0,                    //成功（0），失败则由相关失败码
     *    "data":{
     *       "restrictingBalance":"",             //锁仓余额(单位:LAT)
     *       "stakingValue":"",  //锁仓质押\委托(单位:LAT)
     *       "underreleaseValue":"",        //欠释放(单位:LAT)
     *       "totalValue":"",        //锁仓计划总计锁仓(单位:LAT)
     *       "total":"",  //计划总数
     *       "RPPlan":[
     *          {
     *             "epoch":11,         //锁仓周期
     *             "amount":111,       //锁定金额
     *             "blockNumber":11    //锁仓周期对应快高  结束周期 * epoch  
     *             "estimateTime":1000   //预计时间
     *          }
     *       ]
     *    }
     * }
     */
	@ApiOperation(value = "address/rpplanDetail", nickname = "address rpplan details", notes = "", response = QueryDetailResp.class, tags = { "Address" })
	@PostMapping(value = "address/rpplanDetail", produces = { "application/json" })
	WebAsyncTask<BaseResp<QueryRPPlanDetailResp>> rpplanDetail(@ApiParam(value = "QueryDetailRequest ", required = true)@Valid @RequestBody QueryRPPlanDetailRequest req);
}
