package com.platon.browser.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * User: dongqile
 * Date: 2018/10/23
 * Time: 09:35
 */
@RestController
    @RequestMapping("/browser_api")
public class BlockController extends BasicsController {

    private static Logger logger = LoggerFactory.getLogger(BlockController.class);

    /**
     * @api {post} block/blockList a.
     * @apiVersion 1.0.0
     * @apiName kycAudit
     * @apiGroup audit
     * @apiDescription KYC认证审核
     * @apiUse CommonHeaderFiled
     * @apiParamExample {json} Request-Example:
     * {
     *      "id": 1,//认证记录id
     *      "operation": 1,//操作--1：同意,2：拒绝
     *      "auditRemark": "****",//审核失败备注
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "errMsg": "",//描述信息
     *      "result": 0,//成功（0），失败则由相关失败码
     * }
     */
}