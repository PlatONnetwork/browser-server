package com.platon.browser.controller;

public class AppDocHome {
	
	
    /**
     * @api {post} /home/queryNavigation a.搜索导航
     * @apiVersion 1.0.0
     * @apiName queryNavigation
     * @apiGroup home
     * @apiDescription 
     * 1. 功能：通过用户输入的查询字符串，返回到导航信息<br/>
     * 2. 接口使用说明：通过接口返回的type内容，在调用相应的接口查询详情<br/>
     * - type=block，通过返回的height值查询区块详情接口；<br/>
     * - type=transaction，通过返回的txHash值查询交易详情接口；<br/>
     * - type=address，通过返回的address值查询地址详情接口；<br/>
     * - type=staking，通过返回的nodeId值查询验证人详情接口；<br/>
     * 3. 实现逻辑：<br/>
     * - 当入参为整数类型， 查询mysql中block表<br/>
     * - 当入参为地址类型， 查询mysql中address表<br/>
     * - 当入参为公钥类型， 查询mysql中staking表<br/>
     * - 当入参为hash类型，查询mysql中transaction表，如果没有在查询mysql中block表<br/>
     * - 如果上面没有查询到，查询mysql中staking表通过节点名称查询<br/>
     * @apiParamExample {json} Request-Example:
     * {
     *    "parameter":""               //用户输入
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *    "errMsg": "",                //描述信息
     *    "code": 0,                   //成功（0），失败则由相关失败码
     *    "data":{
     *       "type":"",                //block：区块；  transaction：交易；  address：地址； staking：验证人
     *       "struct":{
     *          "number":17888,        //区块高度
     *          "txHash":"",           //交易hash
     *          "address":"",          //地址
     *          "nodeId":""            //节点地址
     *       }
     *    }
     * }
     */
	
	
    /**
     * @api {subscribe} /topic/block/statistic/new b.出块趋势（websocket）
     * @apiVersion 1.0.0
     * @apiName block/statistic/new
     * @apiGroup home
     * @apiDescription
     * 1. 功能：推送最新的50条出块趋势数据<br/>
     * 2. 实现逻辑：<br/>
     * - 查询redis结构：browser:[应用版本]:[应用运行配置名称]:chain[链ID]:blocks<br/>
     * - 5s全量推送一次
     * @apiSuccessExample  Success-Response:
     * HTTP/1.1 200 OK
     * {
     *    "errMsg":"",                 //描述信息
     *    "code":0,                    //成功（0），失败则由相关失败码
     *    "data":{  
     *       "x":long[],               //区块高度
     *       "ya":double[],            //出块的时间
     *       "yb":long[]               //交易数量数量
     *    }
     * }
     */
	
	
    /**
     * @api {subscribe} /topic/chain/statistic/new c.基础数据（websocket）
     * @apiVersion 1.0.0
     * @apiName topic/chain/statistic/new
     * @apiGroup home
     * @apiDescription
     * 1. 功能：推送区块链基础数据<br/>
     * 2. 实现逻辑：<br/>
     * - 查询redis结构：browser:[应用版本]:[应用运行配置名称]:networkstat<br/>
     * - 5s全量推送一次
     * @apiSuccessExample  Success-Response:
     * HTTP/1.1 200 OK
     * {
     *    "errMsg":"",                 //描述信息
     *    "code":0,                    //成功（0），失败则由相关失败码
     *    "data":{
     *       "currentNumber":111,      //当前区块高度
     *       "nodeAddr":"",            //出块节点地址
     *       "nodeName":"",            //出块节点名称
     *       "txQty":"",               //总的交易数
     *       "currentTps":111,         //当前的TPS
     *       "maxTps":111,             //最大交易TPS
     *       "turnValue":"",           //当前流通量
     *       "issueValue":"",          //当前发行量
     *       "stakingDelegationValue":"",  //当前质押总数=有效的质押+委托
     *       "addressQty":"",          //地址数
     *       "proposalQty":"",         //总提案数
     *       "doingProposalQty":""     //进行中提案数
     *    }
     * }
     */	
	
	
    /**
     * @api {subscribe} /topic/block/list/new d.区块列表（websocket）
     * @apiVersion 1.0.0
     * @apiName topic/block/list/new
     * @apiGroup home
     * @apiDescription
     * 1. 功能：推送最新8条区块信息<br/>
     * 2. 实现逻辑：<br/>
     * - 查询redis结构：browser:[应用版本]:[应用运行配置名称]::blocks<br/>
     * - 5s全量推送一次
     * @apiSuccessExample  Success-Response:
     * HTTP/1.1 200 OK
     * {
     *    "errMsg":"",                 //描述信息
     *    "code":0,                    //成功（0），失败则由相关失败码
     *    "data":[
     *       {
     *          "number":33,           //区块高度
     *          "timestamp":33333,     //出块时间
     *          "serverTime":44444,    //服务器时间
     *          "nodeId":"",           //出块节点id
     *          "nodeName":"",         //出块节点名称
     *          "statTxQty":333        //交易数
     *       }
     *    ]
     * }
     */
	
	
    /**
     * @api {subscribe} /topic/staking/list/new e.验证人列表（websocket）
     * @apiVersion 1.0.0
     * @apiName topic/staking/list/new
     * @apiGroup home
     * @apiDescription
     * 1. 功能：推送最新8条验证人信息<br/>
     * 2. 实现逻辑：<br/>
     * - 查询mysql中node表<br/>
     * - 5s全量推送一次
     * @apiSuccessExample  Success-Response:
     * HTTP/1.1 200 OK
     * {
     *    "errMsg":"",                 //描述信息
     *    "code":0,                    //成功（0），失败则由相关失败码
     *    "data":[
     *       {
     *          "nodeId":"",           //出块节点Id
     *          "nodeName":"",         //出块节点名称
     *          "stakingIcon":"",      //验证人图片
     *          "ranking":333,         //节点排行
     *          "expectedIncome":"",   //预计年收化率（从验证人加入时刻开始计算）
     *          "isInit":true          //是否为初始化的验证人，如果是expectedIncome不显示数值
     *       }
     *    ]
     * }
     */
}
