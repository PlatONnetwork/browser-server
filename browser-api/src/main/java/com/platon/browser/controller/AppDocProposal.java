package com.platon.browser.controller;

public class AppDocProposal {
	
	
    /**
     * @api {post} proposal/proposalList a.提案列表
     * @apiVersion 1.0.0
     * @apiName proposalList
     * @apiGroup proposal
     * @apiDescription 验证人列表
     * @apiParamExample {json} Request-Example:
     * {
     *    "cid":"",                    //链ID (必填)
     *    "pageNo":1,                  //页数(必填)
     *    "pageSize":10,               //页大小(必填)
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
     *         "hash":"",              //提案内部标识
     *         "id":"",                //提案id
     *         "topic":"",             //提案标题
     *         "type":"",              //提案类型
     *         "status":"",            //状态 1:投票中 2:已拒绝 3:已通过
     *         "blockHeight":"15566",  //当前快高
     *         "end_voting_block":"",  //投票结算的快高
     *         "timestamp":123123879,  //交易时间
     *      }
     *   ]
     * }
     */	
	
	
    /**
     * @api {post} proposal/proposalDetails b.提案详情
     * @apiVersion 1.0.0
     * @apiName proposalDetails
     * @apiGroup proposal
     * @apiDescription 验证人详情
     * @apiParamExample {json} Request-Example:
     * {
     *    "cid":"",                    //链ID (必填)
     *    "proposalHash":"",           //提案的内部id
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *    "errMsg":"",                 //描述信息
     *    "code": 0,                   //成功（0），失败则由相关失败码
     *    "data": {
     *       "hash":"",                //提案内部标识
     *       "id":"",                  //提案id
     *       "topic":"",               //提案标题
     *       "type":"",                //提案类型
     *       "status":"",              //状态 1:投票中 2:已拒绝 3:已通过
     *       "blockHeight":"15566",    //当前快高
     *       "end_voting_block":"",    //投票结算的快高
     *       "active_block":"",        //（如果投票通过）生效块高
     *       "timestamp":123123879,    //交易时间
     *       "githubId":"",            //提案的github地址
     *       "new_version":"",         //升级提案升级的版本
     *       "param_name":"",          //参数名
     *       "current_value":"",       //参数当前值
     *       "new_value":"",           //参数的新值
     *       "nodeAddr":"",            //出块节点地址
     *       "nodeName":"",            //出块节点名称
     *       "stakingHash":"",         //验证人id
     *       "tallyResult":{
     *          "yeas":11,             //同意的人
     *          "nays":11,             //反对的人
     *          "abstentions":11,      //期权的人
     *          "validators",          //总共的人
     *          "status"               //投票结果
     *       }
     *    }
     * }
     */	
	
    /**
     * @api {post} proposal/voteList a.投票列表
     * @apiVersion 1.0.0
     * @apiName proposalList
     * @apiGroup proposal
     * @apiDescription 验证人列表
     * @apiParamExample {json} Request-Example:
     * {
     *    "cid":"",                    //链ID (必填)
     *    "pageNo":1,                  //页数(必填)
     *    "pageSize":10,               //页大小(必填)
     *    "proposalHash":"",           //提案的内部id
     *    "status":1                   //
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
     *         "nodeAddr":"",          //出块节点地址
     *         "nodeName":"",          //出块节点名称
     *         "stakingHash":"",       //验证人id
     *         "status":"",            //状态 1:投票中 2:已拒绝 3:已通过
     *         "txHash":"15566",       //投票的hash
     *         "txTime":"",            //投票的时间
     *      }
     *   ]
     * }
     */	
}
