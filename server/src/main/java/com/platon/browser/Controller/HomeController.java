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
public class HomeController extends BasicsController{

    private static Logger logger = LoggerFactory.getLogger(HomeController.class);

    /**
     * @api {put} /app/node/init a.节点监控图标数据（websocket请求）初始数据
     * @apiVersion 1.0.0
     * @apiName node/init
     * @apiGroup home
     * @apiDescription 初始数据
     * @apiParamExample {json} Request-Example:
     *   {}
     *  @apiSuccessExample {json} Success-Response:
     *   HTTP/1.1 200 OK
     *   {
     *      "errMsg": "",//描述信息
     *      "result": 0,//成功（0），失败则由相关失败码
     *      "data":[
     *           {
     *      	    "longitude": "",//经度
     *      	    "latitude":"",//纬度
     *      	    "nodeType": ,//节点状态：1-共识节点 2-非共识
     *      	    "netState": //节点状态 1 正常 2 异常
     *           }
     *      ]
     *   }
     */


    /**
     * @api {get} /topic/node/new b.节点监控图标数据（websocket请求）增量数据
     * @apiVersion 1.0.0
     * @apiName node/new
     * @apiGroup home
     * @apiDescription 增量数据
     * @apiParamExample {json} Request-Example:
     *   {}
     * @apiSuccessExample  Success-Response:
     *   HTTP/1.1 200 OK
     */


    /**
     * @api {put} /app/index/init c.实时监控指标（websocket请求）初始数据
     * @apiVersion 1.0.0
     * @apiName index/init
     * @apiGroup home
     * @apiDescription 初始数据
     * @apiParamExample {json} Request-Example:
     *   {}
     *  @apiSuccessExample {json} Success-Response:
     *   HTTP/1.1 200 OK
     *   {
     *      "errMsg": "",//描述信息
     *      "result": 0,//成功（0），失败则由相关失败码
     *      "data": {
     *      	    "currentHeight": ,//当前区块高度
     *      	    "node":"",//出块节点
     *      	    "currentTransaction": //当前交易笔数
     *      	    "consensusNodeAmount": //共识节点数
     *      	    "addressAmount": //地址数
     *      	    "voteAmount": //投票数
     *      	    "proportion": //占比
     *      	    "ticketPrice": //票价
     *           }
     *   }
     */


    /**
     * @api {get} /topic/index/new d.实时监控指标（websocket请求）增量数据
     * @apiVersion 1.0.0
     * @apiName index/new
     * @apiGroup home
     * @apiDescription 增量数据
     * @apiSuccessExample  Success-Response:
     *   HTTP/1.1 200 OK
     */


    /**
     * @api {put} /app/statis/init e.出块时间及交易数据（websocket请求）初始数据
     * @apiVersion 1.0.0
     * @apiName statis/init
     * @apiGroup home
     * @apiDescription 初始数据
     * @apiParamExample {json} Request-Example:
     *   {}
     *  @apiSuccessExample {json} Success-Response:
     *   HTTP/1.1 200 OK
     *   {
     *      "errMsg": "",//描述信息
     *      "result": 0,//成功（0），失败则由相关失败码
     *      "data": {
     *      	    "avgTime": ,//平均出块时长
     *      	    "current":"",//当前交易数量
     *      	    "maxTps": //最大交易TPS
     *      	    "avgTransaction": //平均区块交易数
     *      	    "dayTransaction": //过去24小时交易笔数
     *      	    "blockStatisList": [
     *      	    {
     *      	        "height": ,//区块高度
     *      	        "time":, //出块的时间
     *      	        "transaction":  //区块打包数量
     *
     *      	    }
     *      	    ]//投票数
     *
     *           }
     *
     *   }
     */


    /**
     * @api {get} /topic/statis/new f.出块时间及交易数据（websocket请求）增量数据
     * @apiVersion 1.0.0
     * @apiName statis/new
     * @apiGroup home
     * @apiDescription 增量数据
     * @apiSuccessExample  Success-Response:
     *   HTTP/1.1 200 OK
     */



    /**
     * @api {put} /app/block/init g.实时区块列表（websocket请求）初始数据
     * @apiVersion 1.0.0
     * @apiName block/init
     * @apiGroup home
     * @apiDescription 初始数据
     * @apiParamExample {json} Request-Example:
     *   {}
     *  @apiSuccessExample {json} Success-Response:
     *   HTTP/1.1 200 OK
     *   {
     *      "errMsg": "",//描述信息
     *      "result": 0,//成功（0），失败则由相关失败码
     *      "data":[
     *       {
     *      	    "height": ,//区块高度
     *      	    "timeStamp":"",//出块时间
     *      	    "serverTime": //服务器时间
     *      	    "node": ""//出块节点
     *      	    "transaction": //交易数
     *      	    "blockReward": //区块奖励
     *           }
     *      ]
     *   }
     */



    /**
     * @api {get} /topic/block/new h.实时区块列表（websocket请求）增量数据
     * @apiVersion 1.0.0
     * @apiName block/new
     * @apiGroup home
     * @apiDescription 增量数据，可手动开关是否订阅增量数据，开启之后实时接收最新数据推送
     * @apiSuccessExample  Success-Response:
     *   HTTP/1.1 200 OK
     */


    /**
     * @api {put} /app/transaction/init i.实时交易列表（websocket请求）初始数据
     * @apiVersion 1.0.0
     * @apiName transaction/init
     * @apiGroup home
     * @apiDescription 初始数据
     * @apiParamExample {json} Request-Example:
     *   {}
     *  @apiSuccessExample {json} Success-Response:
     *   HTTP/1.1 200 OK
     *   {
     *      "errMsg": "",//描述信息
     *      "result": 0,//成功（0），失败则由相关失败码
     *      "data":[
     *           {
     *      	    "txHash": "",//交易Hash
     *      	    "from":"",//交易发起方地址
     *      	    "to": //交易接收方地址
     *      	    "value": ""//数额
     *      	    "timestamp"：//交易时间
     *           }
     *      ]
     *   }
     */



    /**
     * @api {get} /topic/transaction/new j.实时交易列表（websocket请求）增量数据
     * @apiVersion 1.0.0
     * @apiName transaction/new
     * @apiGroup home
     * @apiDescription 增量数据，可手动开关是否订阅增量数据，开启之后实时接收最新数据推送
     * @apiSuccessExample  Success-Response:
     *   HTTP/1.1 200 OK
     */



    /**
     * @api {post} /home/query k.搜索
     * @apiVersion 1.0.0
     * @apiName query
     * @apiGroup home
     * @apiDescription 根据区块高度，区块hash，交易hash等查询信息
     * @apiParamExample {json} Request-Example:
     *   {
     *       "parameter":""//块高，块hash，交易hash等
     *   }
     *  @apiSuccessExample {json} Success-Response:
     *   HTTP/1.1 200 OK
     *   {
     *      "errMsg": "",//描述信息
     *      "result": 0,//成功（0），失败则由相关失败码
     *      "data":{
     *          "type":"",//区块block，交易transaction，节点node,合约contract,
     *           "struct":{
     *      	        "height": 17888,//块高
     *                  "timeStamp": 1798798798798,//出块时间
     *                  "transaction": 10000,//块内交易数
     *                  "size": 188,//块大小
     *                  "miner": "0x234", // 出块节点
     *                  "energonUsed": 111,//能量消耗
     *                  "energonAverage": 11, //平均能量价值
     *                  "blockReward": "123123",//区块奖励
     *                  "serverTime": 1708098077  //服务器时间
     *           }
     *        }
     *
     *
     *   }
     */

}