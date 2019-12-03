package com.platon.browser.controller;

import com.platon.browser.common.BrowserConst;
import com.platon.browser.common.DownFileCommon;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.now.service.BlockService;
import com.platon.browser.req.PageReq;
import com.platon.browser.req.newblock.BlockDetailNavigateReq;
import com.platon.browser.req.newblock.BlockDetailsReq;
import com.platon.browser.req.newblock.BlockDownload;
import com.platon.browser.req.newblock.BlockListByNodeIdReq;
import com.platon.browser.res.BaseResp;
import com.platon.browser.res.RespPage;
import com.platon.browser.res.block.BlockDetailResp;
import com.platon.browser.res.block.BlockListResp;
import com.platon.browser.util.I18nUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.WebAsyncTask;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.concurrent.TimeoutException;

/**
 *     	区块模块具体实现方法
 *  @file AppDocBlockController.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
@RestController
public class AppDocBlockController implements AppDocBlock {

	private final Logger logger = LoggerFactory.getLogger(AppDocBlockController.class);
	
	@Autowired
	private BlockService blockService;
	
	@Autowired
	private I18nUtil i18n;
	
	@Autowired
	private DownFileCommon downFileCommon;
	
	@Override
	public WebAsyncTask<RespPage<BlockListResp>> blockList(@Valid PageReq req) {
		// 5s钟没返回，则认为超时  
        WebAsyncTask<RespPage<BlockListResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, () -> {
            RespPage<BlockListResp> blockListResps = blockService.blockList(req);
            return blockListResps;
        });
        webAsyncTask.onCompletion(() -> {
        });
        webAsyncTask.onTimeout(() -> {
            // 超时的时候，直接抛异常，让外层统一处理超时异常
            throw new TimeoutException("System busy!");
        });
        return webAsyncTask;  
	}

	@Override
	public WebAsyncTask<RespPage<BlockListResp>> blockListByNodeId(@Valid BlockListByNodeIdReq req) {
		// 5s钟没返回，则认为超时  
        WebAsyncTask<RespPage<BlockListResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, () -> {
            RespPage<BlockListResp> blockListResps = blockService.blockListByNodeId(req);
            return blockListResps;
        });
        webAsyncTask.onCompletion(() -> {
        });
        webAsyncTask.onTimeout(() -> {
            // 超时的时候，直接抛异常，让外层统一处理超时异常
            throw new TimeoutException("System busy!");
        });
        return webAsyncTask;  
	}

	@Override
	public void blockListByNodeIdDownload(String nodeId, Long date, String local,String timeZone, HttpServletResponse response) {
		BlockDownload blockDownload = blockService.blockListByNodeIdDownload(nodeId, date, local, timeZone);
		try {
			downFileCommon.download(response, blockDownload.getFilename(), blockDownload.getLength(), blockDownload.getData());
		} catch (Exception e) {
			logger.error(e.getMessage());
            throw new BusinessException(i18n.i(I18nEnum.DOWNLOAD_EXCEPTION));
		}
	}

	@Override
	public WebAsyncTask<BaseResp<BlockDetailResp>> blockDetails(@Valid BlockDetailsReq req) {
		// 5s钟没返回，则认为超时  
        WebAsyncTask<BaseResp<BlockDetailResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, () -> {
            BlockDetailResp blockDetailResp = blockService.blockDetails(req);
            return BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),blockDetailResp);
        });
        webAsyncTask.onCompletion(() -> {
        });
        webAsyncTask.onTimeout(() -> {
            // 超时的时候，直接抛异常，让外层统一处理超时异常
            throw new TimeoutException("System busy!");
        });
        return webAsyncTask;  
	}

	@Override
	public WebAsyncTask<BaseResp<BlockDetailResp>> blockDetailNavigate(@Valid BlockDetailNavigateReq req) {
		// 5s钟没返回，则认为超时  
        WebAsyncTask<BaseResp<BlockDetailResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, () -> {
            BlockDetailResp blockDetailResp = blockService.blockDetailNavigate(req);
            return BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),blockDetailResp);
        });
        webAsyncTask.onCompletion(new Runnable() {  
            @Override  
            public void run() {  
            }  
        });  
        webAsyncTask.onTimeout(() -> {
            // 超时的时候，直接抛异常，让外层统一处理超时异常
            throw new TimeoutException("System busy!");
        });
        return webAsyncTask; 
	}

	
}
