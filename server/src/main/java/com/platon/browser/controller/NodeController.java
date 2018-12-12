package com.platon.browser.controller;

import com.github.pagehelper.PageHelper;
import com.platon.browser.common.base.BaseResp;
import com.platon.browser.common.enums.RetEnum;
import com.platon.browser.common.exception.BusinessException;
import com.platon.browser.config.ChainsConfig;
import com.platon.browser.dto.block.BlockDownload;
import com.platon.browser.dto.block.BlockItem;
import com.platon.browser.dto.node.NodeDetail;
import com.platon.browser.dto.node.NodeItem;
import com.platon.browser.exception.ResponseException;
import com.platon.browser.req.block.BlockDownloadReq;
import com.platon.browser.req.block.BlockListReq;
import com.platon.browser.req.node.NodeDetailReq;
import com.platon.browser.req.node.NodeListReq;
import com.platon.browser.service.ExportService;
import com.platon.browser.service.NodeService;
import com.platon.browser.util.I18nEnum;
import com.platon.browser.util.I18nUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * User: dongqile
 * Date: 2018/10/23
 * Time: 09:35
 */
@RestController
@RequestMapping("/node")
public class NodeController {

    @Autowired
    private I18nUtil i18n;
    @Autowired
    private ChainsConfig chainsConfig;
    @Autowired
    private NodeService nodeService;
    @Autowired
    private ExportService exportService;
    private static Logger logger = LoggerFactory.getLogger(NodeController.class);

    /**
     * @api {post} node/list a.节点列表
     * @apiVersion 1.0.0
     * @apiName list
     * @apiGroup node
     * @apiDescription 节点列表
     * @apiUse CommonHeaderFiled
     * @apiParamExample {json} Request-Example:
     * {
     *      "cid":"", // 链ID (必填)
     *      "keyword": "node-1"// 节点账户名称(可选)，用于节点列表的筛选
     * }
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "errMsg": "",//描述信息
     *      "code": 0,//成功（0），失败则由相关失败码
     *      "data": [
     *           {
     *           "id": "0b9a39c791fdcbda987ff64717ef72f", // 节点ID
     *           "ranking": 1,// 排名
     *           "logo":"", // 节点LOGO，具体形式待定
     *           "name": "node-1",// 账户名称
     *           "electionStatus": 1,// 竞选状态:1-候选前100名,2-出块中,3-验证节点,4-备选前100名
     *           "countryCode":"CN", // 国家代码
     *           "location": "中国广东深圳",// 地理位置
     *           "deposit": "1.254555555", // 质押金，单位-ATP
     *           "blockCount": 252125,// 产生的总区块数
     *           "rewardRatio": 0.02,// 分红比例:小数
     *           "address": "0xsfjl34jfljsl435kd", // 节点地址
     *           }
     *       ]
     * }
     */
    @PostMapping("list")
    public BaseResp list (@Valid @RequestBody NodeListReq req) {
        if(!chainsConfig.isValid(req.getCid())){
            throw new ResponseException(i18n.i(I18nEnum.CHAIN_ID_ERROR,req.getCid()));
        }
        // 取200条记录
        PageHelper.startPage(1,200);
        List<NodeItem> nodes = nodeService.getNodeItemList(req);
        return BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),nodes);
    }


    /**
     * @api {post} node/detail b.节点详情
     * @apiVersion 1.0.0
     * @apiName detail
     * @apiGroup node
     * @apiDescription 节点详情
     * @apiUse CommonHeaderFiled
     * @apiParamExample {json} Request-Example:
     * {
     *      "cid":"", // 链ID (必填)
     *      "id": "0xsfjl34jfljsl435kd", // 节点ID (必填)
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "errMsg": "",//描述信息
     *      "code": 0,//成功（0），失败则由相关失败码
     *      "data": {
     *           "id": "0xsfjl34jfljsl435kd", // 节点ID
     *           "address": "0xsfjl34jfljsl435kd", // 节点地址
     *           "name": "node-1",// 账户名称
     *           "logo":"", // 节点LOGO，具体形式待定
     *           "electionStatus": 1,// 竞选状态:1-候选前100名,2-出块中,3-验证节点,4-备选前100名
     *           "location": "中国广东深圳",// 所属区域
     *           "joinTime": 199880011,// 加入时间，单位-毫秒
     *           "deposit": "1.254555555", // 质押金，单位-ATP
     *           "rewardRatio": 0.02,// 分红比例:小数
     *           "ranking": 1,// 质押排名
     *           "profitAmount": "2.12425451222222",// 累计收益，单位-ATP
     *           "verifyCount": 44554, // 节点验证次数
     *           "blockCount": 252125,// 累计出块数
     *           "avgBlockTime": 1.312, // 平均出块时长,单位-秒
     *           "rewardAmount": "0.12425451222222",// 累计分红，单位-ATP
     *           "nodeUrl":"http://mainnet.abc.cn:10332", // 节点URL地址
     *           "publicKey":"0xdE41ad9010ED7ae4a7bBc42b55665151dcc8DE", // 节点公钥
     *           "wallet":"0xdE41ad9010ED7ae4a7bBc42b55665151dcc8DEf4", // 节点钱包
     *           "intro":"XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", // 节点简介
     *           "orgName":"platon", // 机构名称
     *           "orgWebsite":"https://www.platon.network", // 机构官网
     *           }
     * }
     */
    @PostMapping("detail")
    public BaseResp detail (@Valid @RequestBody NodeDetailReq req) {
        if(!chainsConfig.isValid(req.getCid())){
            throw new ResponseException(i18n.i(I18nEnum.CHAIN_ID_ERROR,req.getCid()));
        }
        try{
            NodeDetail detail = nodeService.getNodeDetail(req);
            return BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),detail);
        }catch (BusinessException be){
            throw new ResponseException(be.getMessage());
        }
    }


    /**
     * @api {post} node/blockList c.节点区块列表(显示最新20条)
     * @apiVersion 1.0.0
     * @apiName blockList
     * @apiGroup node
     * @apiDescription 节点区块列表
     * @apiUse CommonHeaderFiled
     * @apiParamExample {json} Request-Example:
     * {
     *      "cid":"", // 链ID (必填)
     *      "address": "0xsfjl34jfljsl435kd",// 节点地址(必填)
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "errMsg": "",//描述信息
     *      "code": 0,//成功（0），失败则由相关失败码
     *      "data":[{
     *          "height": 17888,//块高
     *          "timestamp": 1798798798798,//出块时间
     *          "transaction": 10000,//块内交易数
     *          "size": 188,//块大小
     *          "miner": "0x234", // 出块节点
     *          "energonUsed": 111,//能量消耗
     *          "energonLimit": 24234,//能量消耗限制
     *          "energonAverage": 11, //平均能量价值
     *          "blockReward": "123123",//区块奖励
     *          "serverTime": 1708098077  //服务器时间
     *       }]
     * }
     */
    @PostMapping("blockList")
    public BaseResp blockList (@Valid @RequestBody BlockListReq req) {
        if(!chainsConfig.isValid(req.getCid())){
            throw new ResponseException(i18n.i(I18nEnum.CHAIN_ID_ERROR,req.getCid()));
        }
        try{
            // 取20条最新记录
            PageHelper.startPage(1,20);
            List<BlockItem> blocks = nodeService.getBlockList(req);
            return BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),blocks);
        }catch (BusinessException be){
            throw new ResponseException(be.getMessage());
        }
    }

    private void download(HttpServletResponse response, String filename,long length, byte [] data){
        response.setHeader("Content-Disposition", "attachment; filename="+filename);
        response.setContentType("application/octet-stream");
        response.setContentLengthLong(length);
        try {
            response.getOutputStream().write(data);
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new ResponseException(i18n.i(I18nEnum.DOWNLOAD_EXCEPTION));
        }
    }

    /**
     * @api {get} node/blockDownload?cid=:cid&address=:address&date=:date d.导出节点区块详情
     * @apiVersion 1.0.0
     * @apiName blockDownload
     * @apiGroup node
     * @apiDescription 导出节点区块详情
     * @apiUse CommonHeaderFiled
     * @apiParam {String} cid 链ID
     * @apiParam {String} address 节点地址
     * @apiParam {String} date 数据起始日期
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * 响应为 二进制文件流
     */
    @GetMapping("blockDownload")
    public void blockDownload(@RequestParam String cid,@RequestParam String address,@RequestParam String date, HttpServletResponse response) {
        BlockDownloadReq req = new BlockDownloadReq();
        req.setCid(cid);
        req.setAddress(address);
        try {
            SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat ymdhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startDate = ymd.parse(date);
            String startStr = ymd.format(startDate);
            req.setStartDate(ymdhms.parse(startStr+" 00:00:00"));
        } catch (ParseException e) {
            e.printStackTrace();
            throw new ResponseException(i18n.i(I18nEnum.FORMAT_DATE_ERROR));
        }
        req.setEndDate(new Date());
        BlockDownload blockDownload = exportService.exportNodeBlockCsv(req);
        download(response,blockDownload.getFilename(),blockDownload.getLength(),blockDownload.getData());
    }
}