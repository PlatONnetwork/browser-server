package com.platon.browser.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.platon.browser.config.ChainsConfig;
import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.service.ApiService;
import com.platon.browser.util.I18nUtil;

/**
 * User: dongqile
 * Date: 2019/3/19
 * Time: 11:39
 */


@RestController
@RequestMapping("/node")
public class NodeController {

    private static Logger logger = LoggerFactory.getLogger(NodeController.class);
    @Autowired
    private I18nUtil i18n;
    @Autowired
    private ChainsConfig chainsConfig;
    @Autowired
    private ApiService apiService;

    /**
     * @api {post} node/list a.获取节点列表
     * @apiVersion 1.0.0
     * @apiName node/listAll
     * @apiGroup node
     * @apiDescription 获取节点列表
     * @apiParamExample {json} Request-Example:
     * {
     *      "cid":"",                            //链ID (必填)
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "errMsg":"",                         //描述信息
     *      "code":0,                            //成功（0），失败则由相关失败码
     *      "voteCount":90,                      //已投票
     *      "proportion":86,                     //投票率:小数
     *      "ticketPrice":3.66,                  //票价（单位Energon）
     *      "data":[
     *           {
     *              "nodeId":"0x",               //节点ID
     *              "ranking":1,                 //质押排名
     *              "name":"node-1",             //节点名称
     *              "countryCode":"CN",          //国家代码
     *              "countryEnName":"",          //国家英文名称
     *              "countryCnName":"",          //国家中文名称
     *              "countrySpellName":"",       //国家拼音名称，中文环境下，区域进行排序
     *              "deposit":"1.254555555",     //质押金(单位:Energon)
     *              "rewardRatio":0.02           //投票激励:小数
     *           }
     *       ]
     * }
     */
    @PostMapping("list")
    public List<Transaction> list(){
        return null;
    }

    /**
     * @api {post} node/details b.获取节点详情
     * @apiVersion 1.0.0
     * @apiName node/details
     * @apiGroup node
     * @apiDescription 获取节点详情
     * @apiParamExample {json} Request-Example:
     * {
     *      "cid":"",                            //链ID (必填)
     *      "nodeId":"0x",                       //节点ID
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "errMsg":"",                         //描述信息
     *      "code":0,                            //成功（0），失败则由相关失败码
     *      "data": {
     *           "nodeId":"0x",                  //节点ID
     *           "ranking":1,                    //质押排名
     *           "name":"node-1",                //节点名称
     *           "deposit":"1.254555555",        //质押金(单位:Energon)
     *           "rewardRatio":0.02              //投票激励:小数
     *           "orgName":"",                   //机构名称
     *           "orgWebsite":"",                //机构官网
     *           "intro":"",                     //节点简介
     *           "nodeUrl":"",                   //节点地址
     *           "ticketCount":"",               //得票数
     *           "joinTime":199880011,           //加入时间，单位-毫秒
     *           "nodeType":""                   //竞选状态:
     *                                           nominees—提名节点
     *                                           validator-验证节点
     *                                           candidates—候选节点
     *      }
     * }
     */
    @PostMapping("detail")
    public List<Transaction> detail(){
        return null;
    }

    /**
     * @api {post} node/listUserVoteNode c.获得用户有投票的节点列表
     * @apiVersion 1.0.0
     * @apiName node/listUserVoteNode
     * @apiGroup node
     * @apiDescription 获取投票列表
     * @apiParamExample {json} Request-Example:
     * {
     *      "cid":"",                            //链ID (必填)
     *      "walletAddrs":[                      //地址列表
     *          "address1",
     *          "address2"
     *      ]
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "errMsg":"",                       //描述信息
     *      "code":0,                          //成功（0），失败则由相关失败码
     *      "data":[
     *           {
     *              "nodeId":"0x",             //节点ID
     *              "name":"node-1",           //节点名称
     *              "countryCode":"CN",        //国家代码
     *              "countryEnName":"",        //国家英文名称
     *              "countryCnName":"",        //国家中文名称
     *              "validNum":"50",           //有效票
     *              "totalTicketNum":"100",    //总票数
     *              "locked":"",               //投票锁定,单位Energon
     *              "earnings":"",             //投票收益,单位Energon
     *              "transactionTime":""       //最新投票时间，单位-毫秒
     *           }
     *       ]
     * }
     */
    @PostMapping("listUserVoteNode")
    public List<Transaction> listUserVoteNode(){
        return null;
    }
}
