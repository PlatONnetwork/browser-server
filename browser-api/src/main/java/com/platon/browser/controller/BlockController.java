package com.platon.browser.controller;

import com.platon.browser.config.BrowserConst;
import com.platon.browser.config.CommonMethod;
import com.platon.browser.config.DownFileCommon;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.reqest.PageReq;
import com.platon.browser.reqest.newblock.BlockDetailNavigateReq;
import com.platon.browser.reqest.newblock.BlockDetailsReq;
import com.platon.browser.reqest.newblock.BlockDownload;
import com.platon.browser.reqest.newblock.BlockListByNodeIdReq;
import com.platon.browser.response.BaseResp;
import com.platon.browser.response.RespPage;
import com.platon.browser.response.block.BlockDetailResp;
import com.platon.browser.response.block.BlockListResp;
import com.platon.browser.service.BlockService;
import com.platon.browser.util.I18nUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.WebAsyncTask;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 *     	区块模块具体实现方法
 *  @file AppDocBlockController.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
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

	@PostMapping(value = "block/blockList")
	public WebAsyncTask<RespPage<BlockListResp>> blockList(@Valid @RequestBody PageReq req) {
		/**
		 * 异步调用，超时则进入timeout  
		 */
        WebAsyncTask<RespPage<BlockListResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, () -> blockService.blockList(req));
        CommonMethod.onTimeOut(webAsyncTask);
        return webAsyncTask;  
	}

	@PostMapping(value = "block/blockListByNodeId")
	public WebAsyncTask<RespPage<BlockListResp>> blockListByNodeId(@Valid @RequestBody BlockListByNodeIdReq req) {
		/**
		 * 异步调用，超时则进入timeout  
		 */
        WebAsyncTask<RespPage<BlockListResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, () -> blockService.blockListByNodeId(req));
        CommonMethod.onTimeOut(webAsyncTask);
        return webAsyncTask;  
	}

	@GetMapping(value = "block/blockListByNodeIdDownload")
	public void blockListByNodeIdDownload(
			@RequestParam(value = "nodeId", required = false)String nodeId,
			@RequestParam(value = "date", required = true)Long date,
			@RequestParam(value = "local", required = true)String local,
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

	@PostMapping(value = "block/blockDetails")
	public WebAsyncTask<BaseResp<BlockDetailResp>> blockDetails(@Valid @RequestBody BlockDetailsReq req) {
		/**
		 * 异步调用，超时则进入timeout  
		 */
        WebAsyncTask<BaseResp<BlockDetailResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, () -> {
            BlockDetailResp blockDetailResp = blockService.blockDetails(req);
            return BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),blockDetailResp);
        });
        CommonMethod.onTimeOut(webAsyncTask);
        return webAsyncTask;  
	}

	@PostMapping(value = "block/blockDetailNavigate")
	public WebAsyncTask<BaseResp<BlockDetailResp>> blockDetailNavigate(@Valid @RequestBody BlockDetailNavigateReq req) {
		/**
		 * 异步调用，超时则进入timeout  
		 */
        WebAsyncTask<BaseResp<BlockDetailResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, () -> {
            BlockDetailResp blockDetailResp = blockService.blockDetailNavigate(req);
            return BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),blockDetailResp);
        });
        CommonMethod.onTimeOut(webAsyncTask);
        return webAsyncTask; 
	}
}
