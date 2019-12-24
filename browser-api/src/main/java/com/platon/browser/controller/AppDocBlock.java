package com.platon.browser.controller;

import com.platon.browser.req.PageReq;
import com.platon.browser.req.newblock.BlockDetailNavigateReq;
import com.platon.browser.req.newblock.BlockDetailsReq;
import com.platon.browser.req.newblock.BlockListByNodeIdReq;
import com.platon.browser.res.BaseResp;
import com.platon.browser.res.RespPage;
import com.platon.browser.res.block.BlockDetailResp;
import com.platon.browser.res.block.BlockListResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.async.WebAsyncTask;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 	区块模块接口申明集成swagger
 *  @file AppDocBlock.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
@Api(value = "/block", tags = "Block")
public interface AppDocBlock {
	
    /**
     * @api {post} /block/blockList a.区块列表
     * @apiVersion 1.0.0
     * @apiName blockList
     * @apiGroup block
     * @apiDescription
     * 1. 功能：区块列表查询<br/>
     * 2. 实现逻辑：<br/>
     * - 查询redis结构：browser:blocks<br/>
     * @apiParamExample {json} Request-Example:
     * {
     *    "pageNo":1,                  //页数(必填)
     *    "pageSize":10                //页大小(必填)
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
     *          "number":17888,        //区块高度
     *          "timestamp":12,        //出块时间
     *          "serverTime":17,       //当前服务器的时间
     *          "statTxQty":10000,     //交易数
     *          "size":188,            //区块大小
     *          "nodeId":"",           //出块节点id
     *          "nodeName":"",         //出块节点名称
     *          "gasUsed":111,         //燃料用量
     *          "statTxGasLimit":14,   //区块所有交易可提供的gas总和
     *          "blockReward":"1231"   //区块奖励(单位:von)
     *       }
     *    ]
     * }
     */
	@ApiOperation(value = "block/blockList", nickname = "", notes = "", response = BlockListResp.class, tags = { "Block" })
	@PostMapping(value = "block/blockList", produces = { "application/json" })
	WebAsyncTask<RespPage<BlockListResp>> blockList(@ApiParam(value = "PageReq", required = true)@Valid @RequestBody PageReq req);
	
     /**
     *
     * @api {post} /block/blockListByNodeId b.节点的区块列表
     * @apiVersion 1.0.0
     * @apiName blockListByNodeId
     * @apiGroup block
     * @apiDescription
     * 1. 功能：节点产生的区块列表查询<br/>
     * 2. 实现逻辑：<br/>
     * - 查询mysql中block表
     * @apiParamExample {json} Request-Example:
     * {
     *    "pageNo":1,                  //页数(必填)
     *    "pageSize":10,               //页大小(必填)
     *    "nodeId":""                //节点id
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * > 返回值同《区块列表接口》返回值
     */
	@ApiOperation(value = "block/blockListByNodeId", nickname = "", notes = "", response = BlockListResp.class, tags = { "Block" })
	@PostMapping(value = "block/blockListByNodeId", produces = { "application/json" })
	WebAsyncTask<RespPage<BlockListResp>> blockListByNodeId(@ApiParam(value = "BlockListByNodeIdReq", required = true)@Valid @RequestBody BlockListByNodeIdReq req);
	
    /**
     * @api {get} /block/blockListByNodeIdDownload?nodeId=:nodeId&date=:date&local=:en c.导出节点的区块列表
     * @apiVersion 1.0.0
     * @apiName blockListByNodeIdDownload
     * @apiGroup block
     * @apiDescription
     * 1. 功能：导出节点产生的区块列表查询<br/>
     * 2. 实现逻辑：<br/>
     * - 查询mysql中block表
     * @apiParam {String} nodeId 节点id
     * @apiParam {String} date 数据结束日期
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * >响应为 二进制文件流
     */
	@ApiOperation(value = "block/blockListByNodeIdDownload", nickname = "", notes = "", tags = { "Block" })
	@GetMapping(value = "block/blockListByNodeIdDownload", produces = { "application/json" })
    void blockListByNodeIdDownload(@ApiParam(value = "nodeId ", required = true)@RequestParam(value = "nodeId", required = false)String nodeId,
    		@ApiParam(value = "date ", required = true)@RequestParam(value = "date", required = true)Long date,
    		@ApiParam(value = "local en或者zh-cn", required = true)@RequestParam(value = "local", required = true)String local,
    		@ApiParam(value = "time zone", required = true)@RequestParam(value = "timeZone", required = true) String timeZone,
    		@ApiParam(value = "token", required = false)@RequestParam(value = "token", required = false) String token,
    		HttpServletResponse response);
	
	
    /**
     * @api {post} /block/blockDetails d.区块详情
     * @apiVersion 1.0.0
     * @apiName blockDetails
     * @apiGroup block
     * @apiDescription 
     * 1. 功能：区块详情查询<br/>
     * 2. 实现逻辑：<br/>
     * - 查询mysql中block表
     * @apiParamExample {json} Request-Example:
     * {
     *    "number":123                 //区块高度(必填)
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *    "errMsg":"",                 //描述信息
     *    "code": 0,                   //成功（0），失败则由相关失败码
     *    "data": {
     *       "number":19988,           //区块高度
     *       "timestamp":123123123879, //出块时间
     *       "txQty":1288,         //块内交易总数
     *       "hash":"0x1238",          //区块hash
     *       "parentHash":"0x234",     //父区块hash
     *       "nodeId":"",              //出块节点id
     *       "nodeName":"",            //出块节点名称
     *       "timeDiff":424234,        //当前块出块时间距离上一个块出块时间之差（毫秒）
     *       "size":123,               //区块大小
     *       "gasLimit":24234,         //能量消耗限制
     *       "gasUsed":2342,           //能量消耗
     *       "statTxGasLimit":14,      //区块所有交易可提供的gas总和
     *       "blockReward":"123123",   //区块奖励(单位:von)
     *       "extraData":"xxx",        //附加数据
     *       "first":false,            //是否第一条记录
     *       "last":true,              //是否最后一条记录
     *       "transferQty":11,     //块内转账交易总数
     *       "delegateQty":11,     //块内委托交易总数
     *       "stakingQty":11,      //块内验证人交易总数
     *       "proposalQty":11,      //块内治理交易总数
     *       "serverTime":17       //当前服务器的时间
     *    }
     * }
     */
	@ApiOperation(value = "block/blockDetails", nickname = "", notes = "", response = BlockDetailResp.class, tags = { "Block" })
	@PostMapping(value = "block/blockDetails", produces = { "application/json" })
	WebAsyncTask<BaseResp<BlockDetailResp>> blockDetails(@ApiParam(value = "BlockDetailsReq", required = true)@Valid @RequestBody BlockDetailsReq req);
	
	
    /**
     * @apiDeprecated
     * @api {post} /block/blockDetailNavigate e.区块详情前后跳转浏览
     * @apiVersion 1.0.0
     * @apiName blockDetailNavigate
     * @apiGroup block
     * @apiDescription 同 《区块详情接口》
     * @apiParamExample {json} Request-Example:
     * {
     *    "number":123,                //当前所在页面的区块高度(必填)
     *    "direction":""               //方向：prev-上一个，next-下一个 (必填)
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * > 返回值同《区块详情接口》返回值
     */
	@ApiOperation(value = "block/blockDetails", nickname = "", notes = "", response = BlockDetailResp.class, tags = { "Block" })
	@PostMapping(value = "block/blockDetailNavigate", produces = { "application/json" })
	WebAsyncTask<BaseResp<BlockDetailResp>> blockDetailNavigate(@ApiParam(value = "BlockDetailNavigateReq", required = true)@Valid @RequestBody BlockDetailNavigateReq req);
}
