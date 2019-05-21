package com.platon.browser.controller;

import java.util.List;

import com.platon.browser.dto.app.node.AppNodeDetailDto;
import com.platon.browser.dto.app.node.AppNodeDto;
import com.platon.browser.dto.app.node.AppNodeListWrapper;
import com.platon.browser.dto.app.node.AppUserNodeDto;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.exception.ResponseException;
import com.platon.browser.req.app.AppNodeDetailReq;
import com.platon.browser.req.app.AppUserNodeListReq;
import com.platon.browser.res.BaseResp;
import com.platon.browser.service.app.AppNodeService;
import com.platon.browser.service.app.AppTransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.platon.browser.config.ChainsConfig;
import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.util.I18nUtil;

/**
 * User: dongqile
 * Date: 2019/3/19
 * Time: 11:39
 */


@RestController
@RequestMapping("/node")
public class NodeController extends BaseController{

    private static Logger logger = LoggerFactory.getLogger(NodeController.class);
    @Autowired
    private I18nUtil i18n;
    @Autowired
    private ChainsConfig chainsConfig;
    @Autowired
    private AppNodeService appNodeService;

    /**
     * @api {post} node/list a.获取节点列表
     * @apiVersion 1.0.0
     * @apiName node/list
     * @apiGroup node
     * @apiDescription 获取节点列表
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "errMsg":"",                           //描述信息
     *      "code":0,                              //成功（0），失败则由相关失败码
     *      "data": {
     *          "voteCount":90,                    //已投票
     *          "totalCount":86,                   //总票数
     *          "ticketPrice":"100",               //票价（单位E）
     *          "list":[
     *             {
     *                "nodeId":"0x",               //节点ID
     *                "ranking":1,                 //质押排名
     *                "name":"node-1",             //节点名称
     *                "countryCode":"CN",          //国家代码
     *                "deposit":"100",             //质押金(单位:E)
     *                "reward":"9500"         //投票激励, 9500/10000 = 0.95 = 95%
     *                "orgName":"",                   //机构名称
     *                "orgWebsite":"",                //机构官网
     *                "intro":"",                     //节点简介
     *                "nodeUrl":"",                   //节点地址
     *                "ticketCount":"",               //得票数
     *                "joinTime":"",                  //加入时间，单位-毫秒
     *                "nodeType":""                   //竞选状态:
*                                           nominees—提名节点
*                                           validator-验证节点
*                                           candidates—候选节点
     *             }
     *         ]
     *      }
     * }
     */

    @PostMapping("list")
    public BaseResp list(@RequestHeader(CID) String chainId){
        if(!chainsConfig.isValid(chainId)){
            throw new ResponseException(i18n.i(I18nEnum.CHAIN_ID_ERROR,chainId));
        }
        try{
            AppNodeListWrapper nodes = appNodeService.list(chainId);
            return BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),nodes);
        }catch (Exception be){
            throw new ResponseException(be.getMessage());
        }
    }

    /**
     * @api {post} node/detail b.获取节点详情
     * @apiVersion 1.0.0
     * @apiName node/details
     * @apiGroup node
     * @apiDescription 获取节点详情
     * @apiParamExample {json} Request-Example:
     * {
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
     *           "deposit":"100",                //质押金(单位:E)
     *           "reward":"9500"            //投票激励, 9500/10000 = 0.95 = 95%
     *           "orgName":"",                   //机构名称
     *           "orgWebsite":"",                //机构官网
     *           "intro":"",                     //节点简介
     *           "nodeUrl":"",                   //节点地址
     *           "ticketCount":"",               //得票数
     *           "joinTime":"",                  //加入时间，单位-毫秒
     *           "nodeType":""                   //竞选状态:
     *                                           nominees—提名节点
     *                                           validator-验证节点
     *                                           candidates—候选节点
     *      }
     * }
     */
    @PostMapping("detail")
    public BaseResp detail(@RequestHeader(CID) String chainId, @RequestBody AppNodeDetailReq req){
        if(!chainsConfig.isValid(chainId)){
            throw new ResponseException(i18n.i(I18nEnum.CHAIN_ID_ERROR,chainId));
        }
        try{
            AppNodeDetailDto node = appNodeService.detail(chainId,req.getNodeId());
            return BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),node);
        }catch (BusinessException be){
            throw new ResponseException(be.getMessage());
        }
    }

    /**
     * @api {post} node/listUserVoteNode c.获得用户有投票的节点列表
     * @apiVersion 1.0.0
     * @apiName node/listUserVoteNode
     * @apiGroup node
     * @apiDescription 获取投票列表
     * @apiParamExample {json} Request-Example:
     * {
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
     *              "validNum":"50",           //有效票
     *              "totalTicketNum":"100",    //总票数
     *              "locked":"",               //投票锁定,单位E
     *              "earnings":"",             //投票收益,单位E
     *              "transactionTime":""       //最新投票时间，单位-毫秒
     *              "isValid":""       // 是否有效：0-无效，1-有效
     *           }
     *       ]
     * }
     */
    @PostMapping("listUserVoteNode")
    public BaseResp listUserVoteNode(@RequestHeader(CID) String chainId, @RequestBody AppUserNodeListReq req){
        try{
            List<AppUserNodeDto> nodes = appNodeService.getUserNodeList(chainId,req);
            return BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),nodes);
        }catch (Exception be){
            throw new ResponseException(be.getMessage());
        }
    }
}
