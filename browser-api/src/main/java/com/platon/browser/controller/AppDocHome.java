package com.platon.browser.controller;

public class AppDocHome {
	
	
    /**
     * @api {post} /home/query a.搜索
     * @apiVersion 1.0.0
     * @apiName query
     * @apiGroup home
     * @apiDescription 根据区块 高度、区块hash查询区块详情；根据交易hash查询交易详情；根据钱包地址查询地址详情；根据合约地址查询合约详情；根据验证人节点地址、验证人节点名称查询验证人详情；
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
     *       "type":"",                //block：区块；  transaction：交易；node：验证人；contract：合约；account：地址
     *       "struct":{
     *          "height":17888,        //块高，type=block存在 
     *          "txHash":"",           //交易hash，type=transaction存在 
     *          "address":"",          //地址，type=contract 或者 type=account存在 
     *          "nodeAddress":"",      //节点地址，type=node存在 
     *          "stakingHash":""       //节点质押标识，type=node存在 
     *       }
     *    }
     * }
     */
	
	
    /**
     * @api {subscribe} /topic/block/statistic/new?cid=:chainId b.出块趋势（websocket请求）全量数据
     * @apiVersion 1.0.0
     * @apiName block/statistic/new
     * @apiGroup home
     * @apiDescription 全量数据
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
     * @api {subscribe} /topic/chain/statistic/new?cid=:chainId c.区块链基础数据（websocket请求）全量数据
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
     *       "turnVolume":"",          //当前流通量
     *       "issueVolume":"",         //当前发行量
     *       "stakingTotalVolume":"",  //当前质押总数=有效的质押+委托
     *       "addressVolume":"",       //地址数
     *       "proposalVolume":"",      //总提案数
     *       "doingProposalVolume":"", //进行中提案数
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
