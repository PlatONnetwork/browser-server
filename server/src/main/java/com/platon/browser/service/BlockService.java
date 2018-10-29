package com.platon.browser.service;

import com.platon.browser.dto.block.BlockDetail;
import com.platon.browser.dto.block.BlockList;
import com.platon.browser.req.block.BlockDetailReq;
import com.platon.browser.req.block.BlockListReq;

import java.util.List;

public interface BlockService {
    List<BlockList> getBlockList(BlockListReq req);
    BlockDetail getBlockDetail(BlockDetailReq req);
}
