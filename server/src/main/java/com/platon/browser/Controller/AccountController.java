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
public class AccountController extends BasicsController{

    private static Logger logger = LoggerFactory.getLogger(AccountController.class);


    /**
     * @api {post} account/accountDetails?cid=:chainId a.账户详情
     * @apiVersion 1.0.0
     * @apiName accountDetails
     * @apiGroup account
     * @apiDescription 账户详情
     * @apiParam {String} cid 链ID.
     * @apiUse CommonHeaderFiled
     * @apiParamExample {json} Request-Example:
     * {
     *      "address": "0xdE41ad9010ED7ae4a7bBc42b55665151dcc8DEf4",// 账户地址(必填)
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
     *           "blockTime": 18080899999,//确认时间(出块时间)
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
     * @api {post} account/download?cid=:chainId a.账户交易/出块记录下载
     * @apiVersion 1.0.0
     * @apiName download
     * @apiGroup account
     * @apiDescription 账户交易/出块记录下载
     * @apiParam {String} cid 链ID.
     * @apiUse CommonHeaderFiled
     * @apiParamExample {json} Request-Example:
     * {
     *      "address": "0xdE41ad9010ED7ae4a7bBc42b55665151dcc8DEf4",// 账户地址(必填)
     *      "date":"2018-10-24 00:00:00" // 数据日期 (必填)
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * 响应为 二进制文件流
     */
}