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
     *   {
     *      "cid":"",                    //链ID (必填)
     *      "pageNo":1,                  //页数(必填)
     *      "pageSize":10,               //页大小(必填)
     *   }
     * @apiSuccessExample {json} Success-Response:
     *   HTTP/1.1 200 OK
     *   {
     *      "errMsg":"",                 //描述信息
     *      "code":0,                    //成功（0），失败则由相关失败码
     *      "displayTotalCount":18,      //显示总数
     *      "totalCount":18,             //小于等于500000记录的总数
     *      "totalPages":1,              //总页数
     *      "data":[
     *         {
     *            "height":17888,        //块高
     *            "timestamp":12,        //出块时间
     *            "serverTime":17,       //服务器时间
     *            "transaction":10000,   //块内交易数
     *            "size":188,            //块大小
     *            "nodeId":"",           //出块节点地址
     *            "nodeName":"",         //出块节点名称
     *            "stakingHash":"",      //验证人id
     *            "energonUsed":111,     //能量消耗
     *            "energonLimit":24234,  //能量消耗限制  ?
     *            "energonAverage":11,   //平均能量价值(单位:Energon)  ?
     *         }
     *      ]
     *   }
     */
	
	
	
	
}
