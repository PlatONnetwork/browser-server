package com.platon.browser.controller;

import com.platon.browser.config.CommonMethod;
import com.platon.browser.config.DownFileCommon;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.request.PageReq;
import com.platon.browser.request.newblock.BlockDetailNavigateReq;
import com.platon.browser.request.newblock.BlockDetailsReq;
import com.platon.browser.request.newblock.BlockDownload;
import com.platon.browser.request.newblock.BlockListByNodeIdReq;
import com.platon.browser.response.BaseResp;
import com.platon.browser.response.RespPage;
import com.platon.browser.response.block.BlockDetailResp;
import com.platon.browser.response.block.BlockListResp;
import com.platon.browser.service.BlockService;
import com.platon.browser.utils.I18nUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 区块模块具体实现方法
 *
 * @author zhangrj
 * @file AppDocBlockController.java
 * @description
 * @data 2019年8月31日
 */
@Slf4j
@RestController
public class BlockController {

    private final Logger logger = LoggerFactory.getLogger(BlockController.class);

    @Resource
    private BlockService blockService;

    @Resource
    private I18nUtil i18n;

    @Resource
    private DownFileCommon downFileCommon;

    @Resource
    private CommonMethod commonMethod;

    /**
     * 区块列表
     *
     * @param req
     * @return reactor.core.publisher.Mono<com.platon.browser.response.RespPage < com.platon.browser.response.block.BlockListResp>>
     * @date 2021/5/25
     */
    @PostMapping("block/blockList")
    public Mono<RespPage<BlockListResp>> blockList(@Valid @RequestBody PageReq req) {
        return Mono.just(blockService.blockList(req));
    }

    /**
     * 节点的区块列表
     *
     * @param req
     * @return reactor.core.publisher.Mono<com.platon.browser.response.RespPage < com.platon.browser.response.block.BlockListResp>>
     * @date 2021/5/25
     */
    @PostMapping("block/blockListByNodeId")
    public Mono<RespPage<BlockListResp>> blockListByNodeId(@Valid @RequestBody BlockListByNodeIdReq req) {
        return Mono.just(blockService.blockListByNodeId(req));
    }

    /**
     * 导出节点的区块列表
     *
     * @param nodeId
     * @param date
     * @param local
     * @param timeZone
     * @param token
     * @param response
     * @return void
     * @date 2021/5/25
     */
    @GetMapping("block/blockListByNodeIdDownload")
    public void blockListByNodeIdDownload(
            @RequestParam(value = "nodeId", required = false) String nodeId,
            @RequestParam(value = "date", required = true) Long date,
            @RequestParam(value = "local", required = true) String local,
            @RequestParam(value = "timeZone", required = true) String timeZone,
            @RequestParam(value = "token", required = false) String token,
            HttpServletResponse response
    ) {
        /**
         * 鉴权
         */
        commonMethod.recaptchaAuth(token);
        BlockDownload blockDownload = blockService.blockListByNodeIdDownload(nodeId, date, local, timeZone);
        try {
            downFileCommon.download(response, blockDownload.getFilename(), blockDownload.getLength(), blockDownload.getData());
        } catch (Exception e) {
            logger.error("download error", e);
            throw new BusinessException(i18n.i(I18nEnum.DOWNLOAD_EXCEPTION));
        }
    }

    /**
     * 区块详情
     *
     * @param req
     * @return reactor.core.publisher.Mono<com.platon.browser.response.BaseResp < com.platon.browser.response.block.BlockDetailResp>>
     * @date 2021/5/25
     */
    @PostMapping("block/blockDetails")
    public Mono<BaseResp<BlockDetailResp>> blockDetails(@Valid @RequestBody BlockDetailsReq req) {
        return Mono.create(sink -> {
            BlockDetailResp resp = blockService.blockDetails(req);
            sink.success(BaseResp.build(RetEnum.RET_SUCCESS.getCode(), i18n.i(I18nEnum.SUCCESS), resp));
        });
    }

    /**
     * 区块详情前后跳转浏览
     *
     * @param req
     * @return reactor.core.publisher.Mono<com.platon.browser.response.BaseResp < com.platon.browser.response.block.BlockDetailResp>>
     * @date 2021/5/25
     */
    @PostMapping("block/blockDetailNavigate")
    public Mono<BaseResp<BlockDetailResp>> blockDetailNavigate(@Valid @RequestBody BlockDetailNavigateReq req) {
        return Mono.create(sink -> {
            BlockDetailResp resp = blockService.blockDetailNavigate(req);
            sink.success(BaseResp.build(RetEnum.RET_SUCCESS.getCode(), i18n.i(I18nEnum.SUCCESS), resp));
        });
    }

}
