package com.platon.browser.controller;

import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.ticket.Ticket;
import com.platon.browser.req.ticket.TicketListReq;
import com.platon.browser.service.TicketService;
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
public class TicketController {

    private static Logger logger = LoggerFactory.getLogger(TicketController.class);

    @Autowired
    private TicketService ticketService;

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
     *      "txHash":"" // 投票交易的hash (必填)
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "errMsg": "",//描述信息
     *      "code": 0,//成功（0），失败则由相关失败码
     *      "displayTotalCount":18,//显示总数
     *      "totalCount":18,// 记录的总数
     *      "totalPages":1,//总页数
     *      "data": [
     *           {
     *           "ticketId": "0x234234",//选票ID
     *           "txHash": "15566",//交易HASH
     *           "candidateId": 18080899999,//候选人Id（节点Id）（投票给）
     *           "owner": "0x667766",//票的所属者
     *           "blockNumber": "0x667766",//购票时的块高
     *           "rblockNumber": "222",//票被释放时的块高
     *           "state": "22",//选票状态（1->正常，2->被选中，3->过期，4->掉榜）
     *                 1->候选中
     *                 2->中选票
     *                 3->已失效
     *                 4->待确认
     *           "income": 1,//收益
     *           "estimateExpireTime": "", //预计过期时间
     *           "actualExpireTime": "" //实际过期时间 （只有status = 3显示该时间的值，其他情况显示预期时间的值）
     *           }
     *       ]
     * }
     */
    @PostMapping("list")
    public RespPage<Ticket> list (@Valid @RequestBody TicketListReq req) {
        return ticketService.getList(req);
    }
}