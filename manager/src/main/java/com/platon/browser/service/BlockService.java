package com.platon.browser.service;

import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.block.BlockDetail;
import com.platon.browser.dto.block.BlockDetailNavigate;
import com.platon.browser.dto.block.BlockItem;
import com.platon.browser.req.block.BlockDetailNavigateReq;
import com.platon.browser.req.block.BlockDetailReq;
import com.platon.browser.req.block.BlockPageReq;

public interface BlockService {
    RespPage<BlockItem> getBlockPage(BlockPageReq req);
    BlockDetail getBlockDetail(BlockDetailReq req);

    BlockDetailNavigate getBlockDetailNavigate(BlockDetailNavigateReq req);
}
