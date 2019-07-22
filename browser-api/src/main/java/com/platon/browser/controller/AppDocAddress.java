package com.platon.browser.controller;

public class AppDocAddress {
	
	
    /**
     * @api {post}  address/addressDetails a.查询地址详情
     * @apiVersion 1.0.0
     * @apiName addressDetails
     * @apiGroup address
     * @apiDescription 查询地址详情
     * @apiParamExample {json} Request-Example:
     * {
     *    "cid":"",                    //链ID (必填)
     *    "address":"0x"               //账户地址(必填)
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *    "errMsg":"",                 //描述信息
     *    "code":0,                    //成功（0），失败则由相关失败码
     *    "data":{
     *       "balance":"",             //余额(单位:LAT)
     *       "restrictingBalance":"",  //锁仓余额(单位:LAT)
     *       "stakingValue":"",        //质押的金额
     *       "delegateValue":"",       //委托的金额
     *       "redeemedValue":"",       //赎回中的金额
     *       "transaction":1288,       //交易总数
     *       "baseTransaction":11,     //基础交易总数
     *       "delegateTransaction":11, //委托交易总数
     *       "stakingTransaction":11,  //验证人交易总数
     *       "proposalTransaction":11, //治理交易总数
     *       "stakingCount:11,         //已委托验证人
     *       "delegateHes":"",         //未锁定委托（LAT）
     *       "delegateLocked":"",      //已锁定委托（LAT）
     *       "delegateReleased":"",    //已解除委托（LAT）   
     *       "delegateReduction":""    //赎回中委托（LAT）   
     *    }
     * }
     */
	
	
    /**
     * @api {post}  address/contractDetails a.查询合约详情
     * @apiVersion 1.0.0
     * @apiName contractDetails
     * @apiGroup address
     * @apiDescription 查询地址详情
     * @apiParamExample {json} Request-Example:
     * {
     *    "cid":"",                    //链ID (必填)
     *    "address":"0x"               //账户地址(必填)
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *    "errMsg":"",                 //描述信息
     *    "code":0,                    //成功（0），失败则由相关失败码
     *    "data":{
     *       "balance":"",             //余额(单位:LAT)
     *       "contractName":"",        //合约名称
     *       "contractCreate":"",      //合约创建者地址
     *       "contractCreateHash":"",  //合约创建哈希
     *       "transaction":1288,       //交易总数
     *       "baseTransaction":11,     //基础交易总数
     *       "delegateTransaction":11, //委托交易总数
     *       "stakingTransaction":11,  //验证人交易总数
     *       "proposalTransaction":11  //治理交易总数
     *    }
     * }
     */
}
