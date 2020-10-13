package com.platon.browser.controller;

import com.platon.browser.req.token.QueryTokenDetailReq;
import com.platon.browser.req.token.QueryTokenListReq;
import com.platon.browser.res.BaseResp;
import com.platon.browser.res.RespPage;
import com.platon.browser.res.token.QueryTokenDetailResp;
import com.platon.browser.res.token.QueryTokenListResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.async.WebAsyncTask;

import javax.validation.Valid;

/**
 * Erc20-Token 合约模块
 *
 * @author RJ
 */
@Api(value = "/token", tags = "Token")
public interface AppDocErc20Token {

    /**
     * @api {post}  token/tokenDetail a.查询合约详情
     * @apiVersion 1.0.0
     * @apiName tokenDetail
     * @apiGroup token
     * @apiDescription
     * 1. 功能：查询合约详情<br/>
     * 2. 实现逻辑：<br/>
     * - 查询MySQL数据库读取数据，同时附加附属信息返回
     * @apiParamExample {json} Request-Example:
     * {
     *    "address":"lat..."        // 合约地址（必填项）
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *    "errMsg":"",                 // 描述信息
     *    "code":0,                    // 成功（0），失败则由相关失败码
     *    "data":{
     *       "address":"",              // 合约地址
     *       "name":"",                 // 合约名称
     *       "symbol":"",               // 合约符号
     *       "decimal":6,               // 合约精度
     *       "totalSupply":"",          // 合约发行总量（精度转换后）
     *       "icon":"",                 // 合约图标（base64编码）
     *       "creator":"",              // 合约创建者地址
     *       "txHash":"",               // 创建合约的交易哈希
     *       "webSite":"",              // 合约对应官方站点
     *       "blockTimestamp":10000,    // 合约创建时间（时间戳）
     *       "createTime":11,           // 合约入库时间（时间戳）
     *       "txCount":11,              // 合约内部转账交易数
     *       "abi":"",                  // 合约的ABI描述
     *       "binCode":"",              // 合约的二进制码
     *       "holder":10000,    // 持有人数
     *       "sourceCode":""            // 合约源代码
     *    }
     * }
     */
    @ApiOperation(value = "token/tokenDetail", nickname = "token details",
            notes = "查询合约地址详情", response = QueryTokenDetailResp.class, tags = {"Token"})
    @PostMapping(value = "token/tokenDetail", produces = {"application/json"})
    WebAsyncTask<BaseResp<QueryTokenDetailResp>> tokenDetail(@ApiParam(value = "QueryDetailReq ", required = true)
                                                    @Valid @RequestBody QueryTokenDetailReq req);

    /**
     * @api {post} /token/tokenList b.合约列表
     * @apiVersion 1.0.0
     * @apiName tokenList
     * @apiGroup token
     * @apiDescription
     * 1. 实现逻辑：<br/>
     * - 查询mysql中erc20_contract表
     * - 展示的合约状态为 1（可见）
     * @apiParamExample {json} Request-Example:
     * {
     *    "pageNo":1,                  // 页数(必填)
     *    "pageSize":10                // 页大小(必填)
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *   "errMsg":"",                  // 描述信息
     *   "code":0,                     // 成功（0），失败则由相关失败码
     *   "totalCount":18,              // 总数
     *   "totalPages":1,               // 总页数
     *   "data":[
     *      {
     *          "address":"",              // 合约地址
     *          "name":"",                 // 合约名称
     *          "symbol":"",               // 合约符号
     *          "decimal":6,               // 合约精度
     *          "totalSupply":"",          // 合约发行总量（精度转换后）
     *          "icon":"",                 // 合约图标（base64编码）
     *          "creator":"",              // 合约创建者地址
     *          "txHash":"",               // 创建合约的交易哈希
     *          "webSite":"",              // 合约对应官方站点
     *          "blockTimestamp":10000,    // 合约创建时间（时间戳）
     *          "holder":10000,    // 持有人数
     *          "createTime":11,           // 合约入库时间（时间戳）
     *      }...{}
     *   ]
     * }
     */
    @ApiOperation(value = "token/tokenList", nickname = "token list",
            notes = "查询合约列表", response = QueryTokenListResp.class, tags = {"Token"})
    @PostMapping(value = "token/tokenList", produces = {"application/json"})
    WebAsyncTask<RespPage<QueryTokenListResp>> tokenList(@ApiParam(value = "QueryTokenListReq ", required = true)
                                                         @Valid @RequestBody QueryTokenListReq req);
}
