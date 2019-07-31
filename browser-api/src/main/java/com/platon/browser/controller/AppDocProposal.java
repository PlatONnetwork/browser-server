package com.platon.browser.controller;

public class AppDocProposal {
	
    /**
     * @api {post} /proposal/proposalList a.提案列表
     * @apiVersion 1.0.0
     * @apiName proposalList
     * @apiGroup proposal
     * @apiDescription 
     * 1. 实现逻辑：<br/>
     * - 查询mysql中proposal表
     * @apiParamExample {json} Request-Example:
     * {
     *    "pageNo":1,                  //页数(必填)
     *    "pageSize":10                //页大小(必填)
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
     *         "proposalHash":"",      //提案内部标识
     *         "githubId":"",          //PIP编号   eip-100
     *         "url":"",               //github地址  https://github.com/ethereum/EIPs/blob/master/EIPS/eip-100.md
     *         "topic":"",             //提案标题 
     *         "type":"",              //提案类型   1：文本提案； 2：升级提案；  3参数提案。
     *         "status":"",            //状态  1：投票中  2：通过  3：失败   4：预升级  5：升级完成    已通过=2 或4 或 5
     *         "curBlock":"15566",     //当前块高
     *         "endVotingBlock":"",    //投票结算的快高
     *         "timestamp":123123879   //提案时间
     *      }
     *   ]
     * }
     */	
	
	
    /**
     * @api {post} /proposal/proposalDetails b.提案详情
     * @apiVersion 1.0.0
     * @apiName proposalDetails
     * @apiGroup proposal
     * @apiDescription
     * 1. 实现逻辑：<br/>
     * - 查询mysql中proposal表
     * @apiParamExample {json} Request-Example:
     * {
     *    "proposalHash":""            //提案的内部id
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *    "errMsg":"",                 //描述信息
     *    "code": 0,                   //成功（0），失败则由相关失败码
     *    "data": {
     *       "proposalHash":"",        //提案内部标识
     *       "githubId":"",            //PIP编号   eip-100
     *       "url":"",                 //github地址  https://github.com/ethereum/EIPs/blob/master/EIPS/eip-100.md
     *       "topic":"",               //提案标题 
     *       "type":"",                //提案类型   1：文本提案； 2：升级提案；  3参数提案。
     *       "status":"",              //状态  1：投票中  2：通过  3：失败   4：预升级  5：升级完成    已通过=2 或4 或 5
     *       "curBlock":"15566",       //当前块高
     *       "endVotingBlock":"",      //投票结算的快高
     *       "timestamp":123123879     //提案时间
     *       "activeBlock":"",         //（如果投票通过）生效块高
     *       "newVersion":"",          //升级提案升级的版本
     *       "paramName":"",           //参数名
     *       "currentValue":"",        //参数当前值
     *       "newValue":"",            //参数的新值
     *       "nodeName":"",            //发起提案的节点名称
     *       "nodeId":"",              //发起提案的节点id
     *       "yeas":11,                //同意的人
     *       "nays":11,                //反对的人
     *       "abstentions":11,         //弃权的人
     *       "accuVerifiers":""        //总人数
     *    }
     * }
     */	
	
    /**
     * @api {post} /proposal/voteList c.投票列表
     * @apiVersion 1.0.0
     * @apiName voteList
     * @apiGroup proposal
     * @apiDescription 
     * 1. 实现逻辑：<br/>
     * - 查询mysql中vote表
     * @apiParamExample {json} Request-Example:
     * {
     *    "pageNo":1,                  //页数(必填)
     *    "pageSize":10,               //页大小(必填)
     *    "proposalHash":"",           //提案的内部id
     *    "option":""                  //投票选型  1：支持；  2：反对；  3弃权
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
     *         "nodeName":"",          //投票验证人的节点名称
     *         "nodeId":"",            //投票验证人的节点id
     *         "option":"",            //投票选型  1：支持；  2：反对；  3弃权
     *         "txHash":"15566",       //投票的hash
     *         "timestamp":"",         //投票的时间
     *      }
     *   ]
     * }
     */	
}
