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
     * - type=staking，通过返回的stakingHash值查询验证人详情接口；<br/>
     * - type=contract，通过返回的address值查询合约详情接口；<br/>
     * - type=account，通过返回的address值查询地址详情接口；<br/>
     * 3. 实现逻辑：<br/>
     * - 查询redis结构：browser:[应用版本]:[应用运行配置名称]:chain[链ID]:queryNavigation
     * @apiParamExample {json} Request-Example:
     * {
     *    "cid":"",                    //链ID (必填)
     *    "parameter":""               //用户输入
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *    "errMsg": "",                //描述信息
     *    "code": 0,                   //成功（0），失败则由相关失败码
     *    "data":{
     *       "type":"",                //block：区块；  transaction：交易； staking：验证人； contract：合约； account：地址
     *       "struct":{
     *          "height":17888,        //区块高度
     *          "txHash":"",           //交易hash
     *          "address":"",          //账户或合约地址
     *          "stakingHash":""       //验证人hash
     *       }
     *    }
     * }
     */
	
	
    /**
     * @api {subscribe} /topic/block/statistic/new?cid=:chainId b.出块趋势（websocket）
     * @apiVersion 1.0.0
     * @apiName block/statistic/new
     * @apiGroup home
     * @apiDescription
     * 1. 功能：推送最新的50条出块趋势数据<br/>
     * 2. 实现逻辑：<br/>
     * - 查询redis结构：browser:[应用版本]:[应用运行配置名称]:chain[链ID]:blocks
     * @apiParam {String} cid 链ID.
     * @apiSuccessExample  Success-Response:
     * HTTP/1.1 200 OK
     * {
     *    "errMsg":"",                 //描述信息
     *    "code":0,                    //成功（0），失败则由相关失败码
     *    "data":{  
     *       "graphData":{
     *         "x":long[],             //区块高度
     *         "ya":double[],          //出块的时间
     *         "yb":long[]             //交易数量数量
     *       }
     *    }
     * }
     */
	
	
    /**
     * @api {subscribe} /topic/chain/statistic/new?cid=:chainId c.基础数据（websocket）
     * @apiVersion 1.0.0
     * @apiName topic/chain/statistic/new
     * @apiGroup home
     * @apiDescription 全量数据
     * @apiParam {String} cid 链ID.
     * @apiSuccessExample  Success-Response:
     * HTTP/1.1 200 OK
     * {
     *    "errMsg":"",                 //描述信息
     *    "code":0,                    //成功（0），失败则由相关失败码
     *    "data":{
     *       "currentHeight":111,      //当前区块高度
     *       "nodeAddress":"",         //出块节点地址
     *       "stakingName":"",         //验证人名称
     *       "stakingHash":"",         //验证人id
     *       "currentTransaction":"",  //实时交易笔数
     *       "currentTps":111,         //当前的TPS
     *       "maxTps":111,             //最大交易TPS
     *       "turnAmount":"",          //当前流通量
     *       "issueAmount":"",         //当前发行量
     *       "totalAmount":"",         //当前质押总数=有效的质押+委托
     *       "addressQty":"",          //地址数
     *       "proposalQty":"",         //总提案数
     *       "doingProposalQty":""     //进行中提案数
     *    }
     * }
     */	
	
	
    /**
     * @api {subscribe} /topic/block/list/new?cid=:chainId d.区块列表（websocket请求）全量数据
     * @apiVersion 1.0.0
     * @apiName topic/block/list/new
     * @apiGroup home
     * @apiDescription 全量数据
     * @apiParam {String} cid 链ID.
     * @apiParamExample {json} Request-Example:
     * {}
     * @apiSuccessExample  Success-Response:
     * HTTP/1.1 200 OK
     * {
     *    "errMsg":"",                 //描述信息
     *    "code":0,                    //成功（0），失败则由相关失败码
     *    "data":[
     *       {
     *          "height":33,           //区块高度
     *          "timestamp":33333,     //出块时间
     *          "serverTime":44444,    //服务器时间
     *          "nodeAddress":"",      //出块节点地址
     *          "stakingName":"",      //验证人名称
     *          "stakingHash":"",      //验证人id
     *          "transaction":333      //交易数
     *       }
     *    ]
     * }
     */
	
	
    /**
     * @api {subscribe} /topic/staking/list/new?cid=:chainId e.验证人列表（websocket请求）全量数据
     * @apiVersion 1.0.0
     * @apiName topic/staking/list/new
     * @apiGroup home
     * @apiDescription 全量数据
     * @apiParam {String} cid 链ID.
     * @apiSuccessExample  Success-Response:
     * HTTP/1.1 200 OK
     * {
     *    "errMsg":"",                 //描述信息
     *    "code":0,                    //成功（0），失败则由相关失败码
     *    "data":[
     *       {
     *          "nodeAddress":"",      //出块节点地址
     *          "nodeName":"",         //出块节点名称
     *          "stakingIcon":"",      //验证人图片
     *          "ranking":333,         //节点排行
     *          "expectedIncome":""    //预计年收化率（从验证人加入时刻开始计算）
     *       }
     *    ]
     * }
     */
	
}
