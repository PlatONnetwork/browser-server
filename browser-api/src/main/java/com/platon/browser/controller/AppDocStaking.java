package com.platon.browser.controller;

public class AppDocStaking {
	
	
    /**
     * @api {post} staking/stakingListByDelegateAddress a.委托人相关的验证人列表
     * @apiVersion 1.0.0
     * @apiName stakingListByDelegateAddress
     * @apiGroup staking
     * @apiDescription 区块交易列表
     * @apiParamExample {json} Request-Example:
     * {
     *    "cid":"",                    //链ID (必填)
     *    "pageNo":1,                  //页数(必填)
     *    "pageSize":10,               //页大小(必填)
     *    "address":500                //委托地址(必填)
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *   "errMsg":"",                  //描述信息
     *   "code":0,                     //成功（0），失败则由相关失败码
     *   "totalCount":18,              //总数
     *   "totalPages":1,               //总页数
     *   "data":[
     *      {
     *         "nodeAddress":"",       //出块节点地址
     *         "stakingName":"",       //验证人名称
     *         "stakingHash":"",       //验证人id
     *         "delegateValue":"",     //委托数量
     *         "delegateHes":"",       //未锁定委托（LAT）
     *         "delegateLocked":"",    //已锁定委托（LAT）
     *         "allDelegateLocked":"", //当前验证人总接收的锁定委托量（LAT）
     *         "delegateReleased":"",  //已解除委托（LAT）   
     *         "delegateReduction":""  //赎回中委托（LAT）   
     *      }
     *   ]
     * }
     */
	
	
	
    /**
     * @api {get} staking/addressStakingDownload?cid=:cid&address=:address&date=:date h.导出地址验证人委托列表
     * @apiVersion 1.0.0
     * @apiName addressStakingDownload
     * @apiGroup staking
     * @apiDescription 导出地址验证人委托列表
     * @apiParam {String} cid 链ID
     * @apiParam {String} address 合约地址
     * @apiParam {String} date 数据结束日期
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * >响应为 二进制文件流
     */
}
