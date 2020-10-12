package com.platon.browser.controller;

import com.platon.browser.req.token.QueryHolderTokenListReq;
import com.platon.browser.req.token.QueryTokenHolderListReq;
import com.platon.browser.req.token.QueryTokenTransferRecordListReq;
import com.platon.browser.res.RespPage;
import com.platon.browser.res.token.QueryHolderTokenListResp;
import com.platon.browser.res.token.QueryTokenHolderListResp;
import com.platon.browser.res.token.QueryTokenTransferRecordListResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.async.WebAsyncTask;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Api(value = "合约内部交易接口模块", tags = "Token")
public interface AppDocErc20TokenTransferRecord {

    /**
     * @api {post} /token/tokenTransferList a.合约内部交易列表
     * @apiVersion 1.0.0
     * @apiName tokenTransferList
     * @apiGroup token
     * @apiDescription 1. 实现逻辑：<br/>
     * - 查询ES中索引数据
     * - 如果传入合约地址，则返回当前合约内的所有转账记录
     * - 如果传入用户地址，则返回该地址作为转账扣除这或者接收者的代币转账记录
     * - 如果同时存在，则返回地址在当前合约内的转账记录（暂无此场景）
     * @apiParamExample {json} Request-Example:
     * {
     * "contract":"",               // 合约地址（可选）
     * "address":"",                // 用户地址（可选，检索时可能为：from或to）
     * "txHash":"",                 // 交易哈希（可选，如果存在则根据该哈希强检索）
     * "pageNo":1,                  // 页数(必填)
     * "pageSize":10                // 页大小(必填)
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     * "errMsg":"",                   // 描述信息
     * "code":0,                      // 成功（0），失败则由相关失败码
     * "totalCount":18,               // 总数
     * "totalPages":1,                // 总页数
     * "data":[
     * {
     * "contract":6,               // 合约地址（也是交易to地址）
     * "address":"",            // 持有地址
     * "balance":1,          // 余额（精度转换后）
     * "decimal":1,                // 合约精度
     * "name":"",                  // 合约名称
     * "symbol":""               // 合约符号
     * }...{}
     * ]
     * }
     */
    @ApiOperation(value = "token/tokenTransferList", nickname = "token list",
            notes = "查询合约token转账交易列表", response = QueryTokenTransferRecordListResp.class, tags = {"Token"})
    @PostMapping(value = "token/tokenTransferList", produces = {"application/json"})
    WebAsyncTask<RespPage<QueryTokenTransferRecordListResp>> tokenTransferList(@ApiParam(value = "QueryTokenTransferRecordListReq ", required = true)
                                                                               @Valid @RequestBody QueryTokenTransferRecordListReq req);

    /**
     * @api {post} /token/exportTokenTransferList b.导出合约内部交易列表
     * @apiVersion 1.0.0
     * @apiName exportTokenTransferList
     * @apiGroup token
     * @apiDescription 1. 实现逻辑：<br/>
     * - 查询ES中索引数据
     * - 如果传入合约地址，则返回当前合约内的所有转账记录
     * - 如果传入用户地址，则返回该地址作为转账扣除这或者接收者的代币转账记录
     * - 如果同时存在，则返回地址在当前合约内的转账记录（暂无此场景）
     * @apiParam {String} address 合约地址
     * @apiParam {String} date 数据结束日期
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     */
    @ApiOperation(value = "token/exportTokenTransferList", nickname = "token list",
            notes = "导出合约token转账交易列表", response = QueryTokenTransferRecordListResp.class, tags = {"Token"})
    @PostMapping(value = "token/exportTokenTransferList", produces = {"application/json"})
    void exportTokenTransferList(@ApiParam(value = "address ", required = false) @RequestParam(value = "address",
            required = false) String address,
                                 @ApiParam(value = "contract ", required = false) @RequestParam(value = "contract",
                                         required = false) String contract,
                                 @ApiParam(value = "date ", required = true) @RequestParam(value = "date", required = true) Long date,
                                 @ApiParam(value = "local en或者zh-cn", required = true) @RequestParam(value = "local",
                                         required = true) String local,
                                 @ApiParam(value = "time zone", required = true) @RequestParam(value = "timeZone",
                                         required = true) String timeZone,
                                 @ApiParam(value = "token", required = false) @RequestParam(value = "token", required = false) String token,
                                 HttpServletResponse response);


    /**
     * @api {post} /token/tokenHolderList c.合约持有人信息列表
     * @apiVersion 1.0.0
     * @apiName tokenHolderList
     * @apiGroup token
     * @apiDescription 1. 实现逻辑：<br/>
     * - 查询数据库中数据
     * - 如果传入合约地址，则返回当前合约内的所有持有人记录
     * - 如果传入用户地址，则返回该地址持有的token记录
     * @apiParamExample {json} Request-Example:
     * {
     * "contract":"",               // 合约地址（可选）
     * "pageNo":1,                  // 页数(必填)
     * "pageSize":10                // 页大小(必填)
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     * "errMsg":"",                   // 描述信息
     * "code":0,                      // 成功（0），失败则由相关失败码
     * "totalCount":18,               // 总数
     * "totalPages":1,                // 总页数
     * "data":[
     * {
     * "contract":6,               // 合约地址（也是交易to地址）
     * "address":"",            // 地址
     * "decimal":1,                // 合约精度
     * "name":"",                  // 合约名称
     * "symbol":"",                // 合约符号
     * "balance":11,                 // 交易对应的balance值
     * }...{}
     * ]
     * }
     */
    @ApiOperation(value = "token/tokenHolderList", nickname = "token hodler list",
            notes = "查询合约token持有列表", response = QueryTokenHolderListResp.class, tags = {"Token"})
    @PostMapping(value = "token/tokenHolderList", produces = {"application/json"})
    WebAsyncTask<RespPage<QueryTokenHolderListResp>> tokenHolderList(@ApiParam(value = "QueryTokenHolderListReq", required = true)
                                                                     @Valid @RequestBody QueryTokenHolderListReq req);

    /**
     * @api {post} /token/exportTokenHolderList d.导出合约内部交易列表
     * @apiVersion 1.0.0
     * @apiName exportTokenHolderList
     * @apiGroup token
     * @apiDescription 1. 实现逻辑：<br/>
     * @apiParam {String} address 合约地址
     * @apiParam {String} date 数据结束日期
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     */
    @ApiOperation(value = "token/exportTokenHolderList", nickname = "token list",
            notes = "导出合约token持有人列表", response = QueryTokenTransferRecordListResp.class, tags = {"Token"})
    @PostMapping(value = "token/exportTokenHolderList", produces = {"application/json"})
    void exportTokenHolderList(@ApiParam(value = "contract ", required = false) @RequestParam(value = "contract",
            required = true) String contract,
                               @ApiParam(value = "local en或者zh-cn", required = true) @RequestParam(value = "local",
                                       required = true) String local,
                               @ApiParam(value = "time zone", required = true) @RequestParam(value = "timeZone",
                                       required = true) String timeZone,
                               @ApiParam(value = "token", required = false) @RequestParam(value = "token", required = false) String token,
                               HttpServletResponse response);

    /**
     * @api {post} /token/holderTokenList e.erc20持有信息列表
     * @apiVersion 1.0.0
     * @apiName holderTokenList
     * @apiGroup token
     * @apiDescription 1. 实现逻辑：<br/>
     * - 查询数据库中数据
     * @apiParamExample {json} Request-Example:
     * {
     * "address":"",                // 用户地址（必选）
     * "pageNo":1,                  // 页数(必填)
     * "pageSize":10                // 页大小(必填)
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     * "errMsg":"",                   // 描述信息
     * "code":0,                      // 成功（0），失败则由相关失败码
     * "totalCount":18,               // 总数
     * "totalPages":1,                // 总页数
     * "data":[
     * {
     * "contract":6,               // 合约地址（也是交易to地址）
     * "balance":11,                 // 交易对应的balance值
     * "percent":11%,                 // 持有百分比
     * }...{}
     * ]
     * }
     */
    @ApiOperation(value = "token/holderTokenList", nickname = "holder token list",
            notes = "查询持有token列表", response = QueryTokenHolderListResp.class, tags = {"Token"})
    @PostMapping(value = "token/holderTokenList", produces = {"application/json"})
    WebAsyncTask<RespPage<QueryHolderTokenListResp>> holderTokenList(@ApiParam(value = "QueryHolderTokenListReq", required = true)
                                                                     @Valid @RequestBody QueryHolderTokenListReq req);

    /**
     * @api {post} /token/exportHolderTokenList d.导出持有人对应token列表
     * @apiVersion 1.0.0
     * @apiName exportHolderTokenList
     * @apiGroup token
     * @apiDescription 1. 实现逻辑：<br/>
     * @apiParam {String} address 合约地址
     * @apiParam {String} date 数据结束日期
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     */
    @ApiOperation(value = "token/exportHolderTokenList", nickname = "export holder token list",
            notes = "导出合约token持有人列表", response = QueryTokenTransferRecordListResp.class, tags = {"Token"})
    @PostMapping(value = "token/exportHolderTokenList", produces = {"application/json"})
    void exportHolderTokenList(@ApiParam(value = "address ", required = false) @RequestParam(value = "address",
            required = true) String address,
                               @ApiParam(value = "local en或者zh-cn", required = true) @RequestParam(value = "local",
                                       required = true) String local,
                               @ApiParam(value = "time zone", required = true) @RequestParam(value = "timeZone",
                                       required = true) String timeZone,
                               @ApiParam(value = "token", required = false) @RequestParam(value = "token", required = false) String token,
                               HttpServletResponse response);
}
