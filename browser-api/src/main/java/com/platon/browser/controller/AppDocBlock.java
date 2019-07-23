package com.platon.browser.controller;

public class AppDocBlock {
	
    /**
     *
     * @api {post} block/blockList a.区块列表
     * @apiVersion 1.0.0
     * @apiName blockList
     * @apiGroup block
     * @apiDescription 区块列表
     * @apiParamExample {json} Request-Example:
     * {
     *    "cid":"",                    //链ID (必填)
     *    "pageNo":1,                  //页数(必填)
     *    "pageSize":10,               //页大小(必填)
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *    "errMsg":"",                 //描述信息
     *    "code":0,                    //成功（0），失败则由相关失败码
     *    "displayTotalCount":18,      //显示总数
     *    "totalCount":18,             //小于等于500000记录的总数
     *    "totalPages":1,              //总页数
     *    "data":[
     *       {
     *          "height":17888,        //块高
     *          "timestamp":12,        //出块时间
     *          "serverTime":17,       //服务器时间
     *          "transaction":10000,   //块内交易数
     *          "size":188,            //块大小
     *          "nodeAddr":"",         //出块节点地址
     *          "nodeName":"",         //出块节点名称
     *          "stakingHash":"",      //验证人id
     *          "energonUsed":111,     //区块能量消耗
     *          "energonProvided":14   //交易所提供的能量
     *       }
     *    ]
     * }
     */
	
	
     /**
     *
     * @api {post} block/blockListByStakingId a.区块列表
     * @apiVersion 1.0.0
     * @apiName blockListByStakingId
     * @apiGroup block
     * @apiDescription 区块列表
     * @apiParamExample {json} Request-Example:
     * {
     *    "cid":"",                    //链ID (必填)
     *    "pageNo":1,                  //页数(必填)
     *    "pageSize":10,               //页大小(必填)
     *    "stakingHash":""             //验证人标识
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *    "errMsg":"",                 //描述信息
     *    "code":0,                    //成功（0），失败则由相关失败码
     *    "displayTotalCount":18,      //显示总数
     *    "totalCount":18,             //小于等于500000记录的总数
     *    "totalPages":1,              //总页数
     *    "data":[
     *       {
     *          "height":17888,        //块高
     *          "timestamp":12,        //出块时间
     *          "transaction":10000,   //块内交易数
     *          "blockReward":"1231",  //区块奖励(单位:Energon)
     *       }
     *    ]
     * }
     */
	
	
    /**
     * @api {get} block/blockListByStakingIdDownload?cid=:cid&stakingHash=:stakingHash&date=:date h.导出区块列表
     * @apiVersion 1.0.0
     * @apiName blockListByStakingIdDownload
     * @apiGroup block
     * @apiDescription 导出区块列表
     * @apiParam {String} cid 链ID
     * @apiParam {String} stakingHash 验证人标识
     * @apiParam {String} date 数据结束日期
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * >响应为 二进制文件流
     */	
	
	
    /**
     * @api {post} block/blockDetails b.区块详情
     * @apiVersion 1.0.0
     * @apiName blockDetails
     * @apiGroup block
     * @apiDescription 区块详情
     * @apiParamExample {json} Request-Example:
     * {
     *    "cid":"",                    //链ID (必填)
     *    "height":123                 //区块高度(必填)
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *    "errMsg":"",                 //描述信息
     *    "code": 0,                   //成功（0），失败则由相关失败码
     *    "data": {
     *       "height":19988,           //块高
     *       "timestamp":123123123879, //出块时间
     *       "hash":"0x1238",          //区块hash
     *       "parentHash":"0x234",     //父区块hash
     *       "miner":"",               //出块节点地址
     *       "nodeName":"",            //出块节点名称
     *       "stakingHash":"",         //验证人id
     *       "timeDiff":424234,        //当前块出块时间距离上一个块出块时间之差（毫秒）
     *       "size":123,               //区块大小
     *       "energonLimit":24234,     //能量消耗限制
     *       "energonUsed":2342,       //能量消耗
     *       "energonProvided":14      //交易所提供的能量
     *       "blockReward":"123123",   //区块奖励(单位:Energon)
     *       "extraData":"xxx",        //附加数据
     *       "first":false,            //是否第一条记录
     *       "last":true,              //是否最后一条记录
     *       "transaction":1288,       //块内交易总数
     *       "transferTransaction":11, //块内转账交易总数
     *       "delegateTransaction":11, //块内委托交易总数
     *       "stakingTransaction":11,  //块内验证人交易总数
     *       "proposalTransaction":11  //块内治理交易总数
     *    }
     * }
     */
	

	
	
    /**
     * @api {post} block/blockDetailNavigate c.区块详情前后跳转浏览
     * @apiVersion 1.0.0
     * @apiName blockDetailNavigate
     * @apiGroup block
     * @apiDescription 区块详情前后跳转浏览
     * @apiParamExample {json} Request-Example:
     * {
     *    "cid":"",                    //链ID (必填)
     *    "height":123                 //区块高度(必填)
     *    "direction":"",              //方向：prev-上一个，next-下一个 (必填)
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *    "errMsg":"",                 //描述信息
     *    "code": 0,                   //成功（0），失败则由相关失败码
     *    "data": {
     *       "height":19988,           //块高
     *       "timestamp":123123123879, //出块时间
     *       "hash":"0x1238",          //区块hash
     *       "parentHash":"0x234",     //父区块hash
     *       "miner":"",               //出块节点地址
     *       "nodeName":"",            //出块节点名称
     *       "stakingHash":"",         //验证人id
     *       "timeDiff":424234,        //当前块出块时间距离上一个块出块时间之差（毫秒）
     *       "size":123,               //区块大小
     *       "energonLimit":24234,     //能量消耗限制
     *       "energonUsed":2342,       //能量消耗
     *       "energonProvided":14      //交易所提供的能量
     *       "blockReward":"123123",   //区块奖励(单位:Energon)
     *       "extraData":"xxx",        //附加数据
     *       "first":false,            //是否第一条记录
     *       "last":true,              //是否最后一条记录
     *       "transaction":1288,       //块内交易总数
     *       "baseTransaction":11,     //块内基础交易总数
     *       "delegateTransaction":11, //块内委托交易总数
     *       "stakingTransaction":11,  //块内验证人交易总数
     *       "proposalTransaction":11  //块内治理交易总数
     *    }
     * }
     */
	
	
}
