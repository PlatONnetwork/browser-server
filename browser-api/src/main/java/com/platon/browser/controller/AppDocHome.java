package com.platon.browser.controller;

import com.platon.browser.req.home.QueryNavigationRequest;
import com.platon.browser.res.BaseResp;
import com.platon.browser.res.home.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.async.WebAsyncTask;

import javax.validation.Valid;

/**
 * 	首页模块接口申明集成swagger
 *  @file AppDocHome.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
@Api(value = "/home", tags = "Home")
public interface AppDocHome {
	
	
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
     *       "type":"",                //block：区块；  transaction：交易；  address：地址； staking：验证人；  contract：合约地址
     *       "struct":{
     *          "number":17888,        //区块高度
     *          "txHash":"",           //交易hash
     *          "address":"",          //地址
     *          "nodeId":""            //节点地址
     *       }
     *    }
     * }
     */
	@ApiOperation(value = "home/queryNavigation", nickname = "", notes = "", response = QueryNavigationResp.class, tags = { "Home" })
	@PostMapping(value = "home/queryNavigation", produces = { "application/json" })
	WebAsyncTask<BaseResp<QueryNavigationResp>> queryNavigation(@ApiParam(value = "QueryNavigationRequest ", required = true)@Valid @RequestBody QueryNavigationRequest req);
	
    /**
     * @api {subscribe} /topic/block/statistic/new b.出块趋势（websocket）
     * @apiVersion 1.0.0
     * @apiName block/statistic/new
     * @apiGroup home
     * @apiDescription
     * 1. 功能：推送最新的50条出块趋势数据<br/>
     * 2. 实现逻辑：<br/>
     * - 查询redis结构：browser:chain[链ID]:blocks<br/>
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
	@ApiOperation(value = "topic/block/statistic/new", nickname = "", notes = "", response = BlockStatisticNewResp.class, tags = { "Home" })
	@SubscribeMapping(value = "topic/block/statistic/new")
	@PostMapping(value = "home/blockStatistic", produces = { "application/json" })
	WebAsyncTask<BaseResp<BlockStatisticNewResp>> blockStatisticNew();
	
    /**
     * @api {subscribe} /topic/chain/statistic/new c.基础数据（websocket）
     * @apiVersion 1.0.0
     * @apiName topic/chain/statistic/new
     * @apiGroup home
     * @apiDescription
     * 1. 功能：推送区块链基础数据<br/>
     * 2. 实现逻辑：<br/>
     * - 查询redis结构：browser:networkstat<br/>
     * - 5s全量推送一次
     * @apiSuccessExample  Success-Response:
     * HTTP/1.1 200 OK
     * {
     *    "errMsg":"",                 //描述信息
     *    "code":0,                    //成功（0），失败则由相关失败码
     *    "data":{
     *       "currentNumber":111,      //当前区块高度
     *       "nodeId":"",            //出块节点id
     *       "nodeName":"",            //出块节点名称
     *       "txQty":"",               //总的交易数
     *       "currentTps":111,         //当前的TPS
     *       "maxTps":111,             //最大交易TPS
     *       "turnValue":"",           //当前流通量
     *       "issueValue":"",          //当前发行量
     *       "stakingDelegationValue":"",  //当前质押总数=有效的质押+委托
     *       "addressQty":"",          //地址数
     *       "proposalQty":"",         //总提案数
     *       "doingProposalQty":"",     //进行中提案数
     *       "availableStaking":"",   //可质押量
     *       "nodeNum":"", // 节点数
     *       "blockList":[
     *       {
     *       	"isRefresh":true // 是否更新,false不更新区块，true更新区块
     *          "number":33,           //区块高度
     *          "timestamp":33333,     //出块时间
     *          "serverTime":44444,    //服务器时间
     *          "nodeId":"",           //出块节点id
     *          "nodeName":"",         //出块节点名称
     *          "statTxQty":333        //交易数
     *       }
     *    ]
     *    }
     * }
     */	
	@ApiOperation(value = "/topic/chain/statistic/new", nickname = "", notes = "", response = ChainStatisticNewResp.class, tags = { "Home" })
	@SubscribeMapping(value = "/topic/chain/statistic/new")
	@PostMapping(value = "home/chainStatistic", produces = { "application/json" })
	WebAsyncTask<BaseResp<ChainStatisticNewResp>> chainStatisticNew();
	
	
    /**
     * @apiDeprecated
     * @api {subscribe} /topic/block/list/new d.区块列表（websocket）
     * @apiVersion 1.0.0
     * @apiName topic/block/list/new
     * @apiGroup home
     * @apiDescription
     * 1. 功能：推送最新8条区块信息<br/>
     * (移到chainStatisticNew接口实现)
     * 2. 实现逻辑：<br/>
     * - 查询redis结构：browser:blocks<br/>
     * - 5s全量推送一次
     * @apiSuccessExample  Success-Response:
     * HTTP/1.1 200 OK
     * {
     *    "errMsg":"",                 //描述信息
     *    "code":0,                    //成功（0），失败则由相关失败码
     *    "data":[
     *       {
     *       	"isRefresh":true // 是否更新,false不更新区块，true更新区块
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
//	@ApiOperation(value = "home/blockList", nickname = "", notes = "", response = BlockListNewResp.class, tags = { "Home" })
//	@SubscribeMapping(value = "topic/block/list/new")
//	@PostMapping(value = "home/blockList", produces = { "application/json" })
//	BaseResp<List<BlockListNewResp>> blockListNew();
	
    /**
     * @api {subscribe} /topic/staking/list/new e.验证人列表（websocket）
     * @apiVersion 1.0.0
     * @apiName topic/staking/list/new
     * @apiGroup home
     * @apiDescription
     * 1. 功能：推送最新8条验证人信息<br/>
     * 2. 实现逻辑：<br/>
     * - 查询mysql中node表<br/>
     * - 根据状态和是否共识验证人标识来查询
     * - 根据一级排序 质押金+委托总额来排序，二级排序根据版本号进行排序，三级排序根据质押索引倒序
     * - 5s全量推送一次
     * @apiSuccessExample  Success-Response:
     * HTTP/1.1 200 OK
     * {
     *    "errMsg":"",                 //描述信息
     *    "code":0,                    //成功（0），失败则由相关失败码
     *    "data":{
     *    "isRefresh":false    //false不用全量刷新、true全量刷新
     *    "dataList":[
     *       {
     *          "nodeId":"",           //出块节点Id
     *          "nodeName":"",         //出块节点名称
     *          "stakingIcon":"",      //验证人图片
     *          "ranking":333,         //节点排行
     *          "expectedIncome":"",   //预计年收化率（从验证人加入时刻开始计算）
     *          "isInit":true          //是否为初始化的验证人，如果是expectedIncome不显示数值
     *          "totalValue":"",        //质押总数=有效的质押+委托
     *       }
     *    ]}
     * }
     */
	@ApiOperation(value = "topic/staking/list/new", nickname = "", notes = "", response = StakingListNewResp.class, tags = { "Home" })
	@SubscribeMapping(value = "topic/staking/list/new")
	@PostMapping(value = "home/stakingList", produces = { "application/json" })
	WebAsyncTask<BaseResp<StakingListNewResp>> stakingListNew();
}
