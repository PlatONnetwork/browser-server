package com.platon.browser.controller;

import com.platon.browser.config.ChainsConfig;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.transaction.TransactionListItem;
import com.platon.browser.exception.ResponseException;
import com.platon.browser.req.transaction.TransactionPageReq;
import com.platon.browser.service.*;
import com.platon.browser.util.I18nEnum;
import com.platon.browser.util.I18nUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * User: dongqile
 * Date: 2018/10/23
 * Time: 9:40
 */
@RestController
@RequestMapping("/ticket")
public class VoteTicketController {

    private static Logger logger = LoggerFactory.getLogger(VoteTicketController.class);
    @Autowired
    private I18nUtil i18n;
    @Autowired
    private ChainsConfig chainsConfig;
    @Autowired
    private ExportService exportService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private PendingTxService pendingTxService;
    @Autowired
    private RedisCacheService redisCacheService;
    @Autowired
    private TransactionService transactionService;

    /**
     * @api {post} ticket/list a.选票列表
     * @apiVersion 1.0.0
     * @apiName list
     * @apiGroup ticket
     * @apiDescription 选票列表
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
     *      "displayTotalCount":18,//显示总数
     *      "totalCount":18,// 小于等于500000记录的总数
     *      "totalPages":1,//总页数
     *      "data": [
     *           {
     *           "txHash": "0x234234",//交易hash
     *           "blockHeight": "15566",//交易所在区块高度
     *           "blockTime": 18080899999,//出块时间
     *           "from": "0x667766",//发送方, 必定是钱包地址
     *           "to": "0x667766",//接收方, 此字段存储的可能是钱包地址，也可能是合约地址，需要使用receiveType来进一步区分：
     *                            // 如果receiveType的值为account，则是钱包地址；如果receiveType的值为contract，则是合约地址
     *           "value": "222",//数额(单位:Energon)
     *           "actualTxCost": "22",//交易费用(单位:Energon)
     *           "txReceiptStatus": 1,//交易状态 -1 pending 1 成功  0 失败
     *           "txType": "", // 交易类型
                        transfer ：转账
                        MPCtransaction ： MPC交易
                        contractCreate ： 合约创建
                        vote ： 投票
                        transactionExecute ： 合约执行
                        authorization ： 权限
                        candidateDeposit ： 竞选质押
                        candidateApplyWithdraw ： 减持质押
                        candidateWithdraw ： 提取质押
                        unknown ： 未知
     *           "txInfo": "{
     *                  "functionName":"",//方法名称
     *                  "parameters":{},//参数
     *                  "type":"1"//交易类型
     *                      0：转账
     *                      1：合约发布
     *                      2：合约调用
     *                      4：权限
     *                      5：MPC交易
     *                      1000：投票
     *                      1001：竞选质押
     *                      1002：减持质押
     *                      1003：提取质押
     *                  }"//返回交易解析结构
     *           }
     *           "serverTime": 1123123,//服务器时间
     *           "failReason":"",//失败原因
     *           "receiveType":"account" // 此字段表示的是to字段存储的账户类型：account-钱包地址，contract-合约地址，
     *                                  // 前端页面在点击接收方的地址时，根据此字段来决定是跳转到账户详情还是合约详情
     *           }
     *       ]
     * }
     */
    @PostMapping("list")
    public RespPage<TransactionListItem> getPage (@Valid @RequestBody TransactionPageReq req) {
        if(!chainsConfig.isValid(req.getCid())){
            throw new ResponseException(i18n.i(I18nEnum.CHAIN_ID_ERROR,req.getCid()));
        }
        RespPage<TransactionListItem> page = redisCacheService.getTransactionPage(req.getCid(),req.getPageNo(),req.getPageSize());
        return page;
    }
}