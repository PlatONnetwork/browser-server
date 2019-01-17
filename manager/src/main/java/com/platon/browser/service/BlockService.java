package com.platon.browser.service;

import com.platon.browser.dao.entity.Block;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.block.BlockDetail;
import com.platon.browser.dto.block.BlockListItem;
import com.platon.browser.req.block.BlockDetailNavigateReq;
import com.platon.browser.req.block.BlockDetailReq;
import com.platon.browser.req.block.BlockDownloadReq;
import com.platon.browser.req.block.BlockPageReq;

import java.util.List;
import java.util.Set;

public interface BlockService {
    RespPage<BlockListItem> getPage(BlockPageReq req);
    BlockDetail getDetail(BlockDetailReq req);
    BlockDetail getDetailNavigate(BlockDetailNavigateReq req);
    List<Block> getList(BlockDownloadReq req);
    void clearCache(String chainId);
    void updateCache(String chainId,Set<Block> data);
}
