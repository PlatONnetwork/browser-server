package com.platon.browser.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * User: dongqile
 * Date: 2018/10/23
 * Time: 9:40
 */
@RestController
@RequestMapping("/browser_api")
public class TransactionController extends BasicsController{

    private static Logger logger = LoggerFactory.getLogger(TransactionController.class);


    /**
     * @api {post} transaction/transactionList a.交易列表
     * @apiVersion 1.0.0
     * @apiName transactionList
     * @apiGroup transaction
     * @apiDescription 交易列表
     * @apiParam {String} cid 链ID.
     * @apiUse CommonHeaderFiled
     * @apiParamExample {json} Request-Example:
     * {
     *      "cid":"", // 链ID (必填)
     *      "pageNo": 1,//页数(必填)
     *      "pageSize": 10,//页大小(必填)
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "errMsg": "",//描述信息
     *      "code": 0,//成功（0），失败则由相关失败码
     *      "totalCount":18,//总数
     *      "totalPages":1,//总页数
     *      "data": [
     *           {
     *           "txHash": "0x234234",//交易hash
     *           "blockHeight": "15566",//交易所在区块高度
     *           "blockTime": 18080899999,//出块时间
     *           "from": "0x667766",//发送方
     *           "to": "0x667766",//接收方
     *           "value": "222",//数额
     *           "actualTxCoast": "22",//交易费用
     *           "txReceiptStatus": 1,//交易状态 -1 pending 1 成功  0 失败
     *           "txType": "", // 交易类型
                        transfer ：转账
                        MPCtransaction ： MPC交易
                        contractCreate ： 合约创建
                        vote ： 投票
                        transactionExecute ： 合约执行
                        authorization ： 权限
     *           "serverTime": 1123123,//服务器时间
     *           "failReason":""//失败原因
     *           }
     *       ]
     * }
     */


    /**
     * @api {post} transaction/transactionDetails b.交易详情
     * @apiVersion 1.0.0
     * @apiName transactionDetails
     * @apiGroup transaction
     * @apiDescription 交易详情
     * @apiParam {String} cid 链ID.
     * @apiUse CommonHeaderFiled
     * @apiParamExample {json} Request-Example:
     * {
     *      "cid":"", // 链ID (必填)
     *      "txHash": "",//交易Hash(必填)
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "errMsg": "",//描述信息
     *      "code": 0,//成功（0），失败则由相关失败码
     *      "data": {
     *           "txHash": "0x234234",//交易hash
     *           "timeStamp": 123123123879,//交易时间
     *           "txReceiptStatus": 1,//交易状态 -1 pending 1 成功  0 失败
     *           "blockHeight": "15566",//交易所在区块高度
     *           "from": "0x667766",//发送者
     *           "to": "0x667766",//接收者
     *           "txType": "", // 交易类型
                    transfer ：转账
                    MPCtransaction ： MPC交易
                    contractCreate ： 合约创建
                    vote ： 投票
                    transactionExecute ： 合约执行
                    authorization ： 权限
     *           "value": "222",//数额
     *           "actualTxCoast": "22",//实际交易手续费
     *           "energonLimit": 232,//能量限制
     *           "energonUsed": 122,//能量消耗
     *           "energonPrice": "123",//能量价格
     *           "inputData": "",//附加输入数据
     *           "expectTime": 12312333, // 预计确认时间
     *           "failReason":""//失败原因
     *           }
     * }
     */


    /**
     * @api {post} transaction/pendingList c.待处理交易列表
     * @apiVersion 1.0.0
     * @apiName pendingList
     * @apiGroup transaction
     * @apiDescription 待处理交易列表
     * @apiParam {String} cid 链ID.
     * @apiUse CommonHeaderFiled
     * @apiParamExample {json} Request-Example:
     * {
     *      "cid":"", // 链ID (必填)
     *      "pageNo": 1,//页数(必填)
     *      "pageSize": 10,//页大小(必填),
     *      "address":"0xdE41ad9010ED7ae4a7bBc42b55665151dcc8DEf4", // 地址(可选)，用于筛选功能
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "errMsg": "",//描述信息
     *      "code": 0,//成功（0），失败则由相关失败码
     *      "totalCount":18,//总数
     *      "totalPages":1,//总页数
     *      "data": [
     *           {
     *           "txHash": "0x234234",//交易hash
     *           "dwellTime": 33,// 停留时间=服务器时间-交易接收时间
     *           "energonLimit": 55555,//能量限制
     *           "energonPrice": 55555,//能量价格
     *           "from": "0x667766",//发送方
     *           "to": "0x667766",//接收方
     *           "value": "222",//数额
     *           "txType": "", // 交易类型
*                         transfer ：转账
*                         MPCtransaction ： MPC交易
*                         contractCreate ： 合约创建
*                         vote ： 投票
*                         transactionExecute ： 合约执行
*                         authorization ： 权限
     *           "serverTime": 1123123,//服务器时间
     *           }
     *       ]
     * }
     */



    /**
     * @api {post} transaction/pendingDetails d.待处理交易详情
     * @apiVersion 1.0.0
     * @apiName pendingDetails
     * @apiGroup transaction
     * @apiDescription 待处理交易详情
     * @apiParam {String} cid 链ID.
     * @apiUse CommonHeaderFiled
     * @apiParamExample {json} Request-Example:
     * {
     *      "cid":"", // 链ID (必填)
     *      "txHash": "",//交易Hash(必填)
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "errMsg": "",//描述信息
     *      "code": 0,//成功（0），失败则由相关失败码
     *      "data": {
     *           "txHash": "0x234234",//交易hash
     *           "timeStamp": 123123123879,//交易接收时间
     *           "txReceiptStatus": 1,//交易状态 -1 pending 1 成功  0 失败
     *           "blockHeight": "15566",//交易所在区块高度
     *           "from": "0x667766",//发送者
     *           "to": "0x667766",//接收者
     *           "txType": "", // 交易类型
                        transfer ：转账
                        MPCtransaction ： MPC交易
                        contractCreate ： 合约创建
                        vote ： 投票
                        transactionExecute ： 合约执行
                        authorization ： 权限
     *           "value": "222",//数额
     *           "actualTxCost": "22",//实际交易手续费
     *           "energonLimit": 232,//能量限制
     *           "energonUsed": 122,//能量消耗
     *           "energonPrice": "123",//能量价格
     *           "inputData": "",//附加输入数据
     *           "expectTime": 12312333, // 预计确认时间
     *           }
     * }
     */


    /**
     * @api {post} transaction/addressDetails e.查询地址详情
     * @apiVersion 1.0.0
     * @apiName addressDetails
     * @apiGroup transaction
     * @apiDescription 查询地址详情
     * @apiUse CommonHeaderFiled
     * @apiParamExample {json} Request-Example:
     * {
     *      "cid":"", // 链ID (必填)
     *      "address": "0xdE41ad9010ED7ae4a7bBc42b55665151dcc8DEf4",// 账户地址(必填)
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "errMsg": "",//描述信息
     *      "code": 0,//成功（0），失败则由相关失败码
     *      "data": {
     *         "balance":131,165,156.62618849461651616321, // 余额
     *         "tradeCount":236, // 交易数
     *         "votePledge":131,165,156.62618, // 投票质押
     *         "nodeCount":3, // 投票节点数
     *         "trades":[
     *                {
     *                "txHash": "0x234234",//交易hash
     *                "blockTime": 18080899999,//确认时间(出块时间)
     *                "from": "0x667766",//发送方
     *                "to": "0x667766",//接收方
     *                "value": "222",//数额
     *                "actualTxCoast": "22",//交易费用
     *                "txReceiptStatus": 1,//交易状态 -1 pending 1 成功  0 失败
     *                "txType": "", // 交易类型
                         transfer ：转账
                         MPCtransaction ： MPC交易
                         contractCreate ： 合约创建
                         vote ： 投票
                         transactionExecute ： 合约执行
                         authorization ： 权限
     *                "serverTime": 1123123,//服务器时间
     *                "failReason":""//失败原因
     *                }
     *            ]
     *      }
     * }
     */



    /**
     * @api {post} transaction/addressDownload f.导出地址详情
     * @apiVersion 1.0.0
     * @apiName addressDownload
     * @apiGroup transaction
     * @apiDescription 导出地址详情
     * @apiUse CommonHeaderFiled
     * @apiParamExample {json} Request-Example:
     * {
     *      "cid":"", // 链ID (必填)
     *      "txType":"", // 交易类型
                  transfer ：转账
                  MPCtransaction ： MPC交易
                  contractCreate ： 合约创建
                  vote ： 投票
                  transactionExecute ： 合约执行
                  authorization ： 权限
     *      "address": "0xdE41ad9010ED7ae4a7bBc42b55665151dcc8DEf4",// 账户地址(必填)
     *      "date":"2018-10-24 00:00:00" // 数据日期 (必填)
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * 响应为 二进制文件流
     */



    /**
     * @api {post} transaction/contractDetails g.查询合约详情
     * @apiVersion 1.0.0
     * @apiName contractDetails
     * @apiGroup transaction
     * @apiDescription 查询合约详情
     * @apiUse CommonHeaderFiled
     * @apiParamExample {json} Request-Example:
     * {
     *      "cid":"", // 链ID (必填)
     *      "address": "0xdE41ad9010ED7ae4a7bBc42b55665151dcc8DEf4",// 账户地址(必填)
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "errMsg": "",//描述信息
     *      "code": 0,//成功（0），失败则由相关失败码
     *      "data": {
     *          "balance":131,165,156.62618849461651616321, // 余额
                "tradeCount":236, // 交易数
                "developer":131,165,156.62618, // 合约开发者
                "ownerCount":3, // 合约拥有者
     *          "trades":[
     *                 {
     *                 "txHash": "0x234234",//交易hash
     *                 "blockTime": 18080899999,//确认时间(出块时间)
     *                 "from": "0x667766",//发送方
     *                 "to": "0x667766",//接收方
     *                 "value": "222",//数额
     *                 "actualTxCoast": "22",//交易费用
     *                 "txReceiptStatus": 1,//交易状态 -1 pending 1 成功  0 失败
     *                 "txType": "", // 交易类型
                          transfer ：转账
                          MPCtransaction ： MPC交易
                          contractCreate ： 合约创建
                          vote ： 投票
                          transactionExecute ： 合约执行
                          authorization ： 权限
     *                 "serverTime": 1123123,//服务器时间
     *                 "failReason":""//失败原因
     *                 }
     *             ]
     *      }
     * }
     */



    /**
     * @api {post} transaction/contractDownload h.导出合约详情
     * @apiVersion 1.0.0
     * @apiName contractDownload
     * @apiGroup transaction
     * @apiDescription 导出合约详情
     * @apiUse CommonHeaderFiled
     * @apiParamExample {json} Request-Example:
     * {
     *      "cid":"", // 链ID (必填)
     *      "txType":"", // 交易类型
                transfer ：转账
                MPCtransaction ： MPC交易
                contractCreate ： 合约创建
                vote ： 投票
                transactionExecute ： 合约执行
                authorization ： 权限
     *      "address": "0xdE41ad9010ED7ae4a7bBc42b55665151dcc8DEf4",// 账户地址(必填)
     *      "date":"2018-10-24 00:00:00" // 数据日期 (必填)
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * 响应为 二进制文件流
     */
}