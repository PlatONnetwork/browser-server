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
     * @api {post} block/blockList a.区块列表
     * @apiVersion 1.0.0
     * @apiName blockList
     * @apiGroup blcok
     * @apiDescription 区块列表
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
     *                  {
     *                  "height": 17888,//块高
     *                  "timeStamp": 1798798798798,//出块时间
     *                  "transaction": 10000,//块内交易数
     *                  "size": 188,//块大小
     *                  "miner": "0x234", // 出块节点
     *                  "energonUsed": 111,//能量消耗
     *                  "energonLimit": 24234,//能量消耗限制
     *                  "energonAverage": 11, //平均能量价值
     *                  "blockReward": "123123",//区块奖励
     *                  "serverTime": 1708098077  //服务器时间
     *                  }
     *              ]
     * }
     */



    /**
     * @api {post} block/blockDetails b.区块详情
     * @apiVersion 1.0.0
     * @apiName blockDetails
     * @apiGroup blcok
     * @apiDescription 区块详情
     * @apiUse CommonHeaderFiled
     * @apiParamExample {json} Request-Example:
     * {
     *      "cid":"", // 链ID (必填)
     *      "height": 123,//区块高度(必填)
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "errMsg": "",//描述信息
     *      "code": 0,//成功（0），失败则由相关失败码
     *      "data": {
     *           "height": 19988,//块高
     *           "timeStamp": 123123123879,//出块时间
     *           "transaction": 1288,//块内交易总数
     *           "hash": "0x1238",//区块hash
     *           "parentHash": "0x234",//父区块hash
     *           "miner": "0x234", // 出块节点（多少时间内）
     *           "size": 123,//区块大小
     *           "energonLimit": 24234,//能量消耗限制
     *           "energonUsed": 2342,//能量消耗
     *           "blockReward": "123123",//区块奖励
     *           "extraData": "xxx"//附加数据
     *           }
     * }
     */

    /**
     * @api {post} block/blockDetailNavigate c.区块详情前后跳转浏览
     * @apiVersion 1.0.0
     * @apiName blockDetailNavigate
     * @apiGroup blcok
     * @apiDescription 区块详情前后跳转浏览
     * @apiUse CommonHeaderFiled
     * @apiParamExample {json} Request-Example:
     * {
     *      "cid":"", // 链ID (必填)
     *      "direction":"", // 方向：prev-上一个，next-下一个 (必填)
     *      "height": 123,//区块高度(必填)
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "errMsg": "",//描述信息
     *      "code": 0,//成功（0），失败则由相关失败码
     *      "data": {
     *           "height": 19988,//块高
     *           "timeStamp": 123123123879,//出块时间
     *           "transaction": 1288,//块内交易总数
     *           "hash": "0x1238",//区块hash
     *           "parentHash": "0x234",//父区块hash
     *           "miner": "0x234", // 出块节点（多少时间内）
     *           "size": 123,//区块大小
     *           "energonLimit": 24234,//能量消耗限制
     *           "energonUsed": 2342,//能量消耗
     *           "blockReward": "123123",//区块奖励
     *           "extraData": "xxx"//附加数据
     *           }
     * }
     */
}