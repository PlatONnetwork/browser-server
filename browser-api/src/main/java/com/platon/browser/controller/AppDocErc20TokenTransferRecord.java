package com.platon.browser.controller;

import com.platon.browser.req.token.QueryTokenTransferRecordListReq;
import com.platon.browser.res.RespPage;
import com.platon.browser.res.token.QueryTokenTransferRecordListResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.async.WebAsyncTask;

import javax.validation.Valid;

@Api(value = "合约内部交易接口模块", tags = "Token")
public interface AppDocErc20TokenTransferRecord {

    /**
     * @api {post} /token/tokenTransferList c.合约内部交易列表
     * @apiVersion 1.0.0
     * @apiName tokenTransferList
     * @apiGroup token
     * @apiDescription
     * 1. 实现逻辑：<br/>
     * - 查询ES中索引数据
     * - 如果传入合约地址，则返回当前合约内的所有转账记录
     * - 如果传入用户地址，则返回该地址作为转账扣除这或者接收者的代币转账记录
     * - 如果同时存在，则返回地址在当前合约内的转账记录（暂无此场景）
     * @apiParamExample {json} Request-Example:
     * {
     *    "contract":"",               // 合约地址（可选）
     *    "address":"",                // 用户地址（可选，检索时可能为：from或to）
     *    "txHash":"",                 // 交易哈希（可选，如果存在则根据该哈希强检索）
     *    "pageNo":1,                  // 页数(必填)
     *    "pageSize":10                // 页大小(必填)
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *   "errMsg":"",                   // 描述信息
     *   "code":0,                      // 成功（0），失败则由相关失败码
     *   "totalCount":18,               // 总数
     *   "totalPages":1,                // 总页数
     *   "data":[
     *      {
     *          "txHash":"",                // 交易哈希
     *          "blockNumber":1,            // 区块高度
     *          "txFrom":"",                // 交易发起者
     *          "contract":6,               // 合约地址（也是交易to地址）
     *          "transferTo":"",            // 代币转移接收者
     *          "transferValue":1,          // 代币转移金额（精度转换后）
     *          "decimal":1,                // 合约精度
     *          "name":"",                  // 合约名称
     *          "symbol":"",                // 合约符号
     *          "methodSign":"",            // 调用的方法签名
     *          "result":1,                 // 转账结果（1 成功，0 失败）
     *          "blockTimestamp":11,        // 转账时间（时间戳）
     *          "value":11,                 // 交易对应的value值
     *          "type":1,                   // 交易方向（相对于检索条件address: INPUT 进账，OUT 出账，NONE 无方向）
     *      }...{}
     *   ]
     * }
     */
    @ApiOperation(value = "token/tokenTransferList", nickname = "token list",
            notes = "查询合约token转账交易列表", response = QueryTokenTransferRecordListResp.class, tags = {"Token"})
    @PostMapping(value = "token/tokenTransferList", produces = {"application/json"})
    WebAsyncTask<RespPage<QueryTokenTransferRecordListResp>> tokenTransferList(@ApiParam(value = "QueryTokenTransferRecordListReq ", required = true)
                                                         @Valid @RequestBody QueryTokenTransferRecordListReq req);
}
