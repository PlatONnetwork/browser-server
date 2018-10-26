package com.platon.browser.service;

import com.platon.browser.common.dto.block.BlockDetail;
import com.platon.browser.common.dto.block.BlockList;
import com.platon.browser.common.req.block.BlockDetailReq;
import com.platon.browser.common.req.block.BlockListReq;

import java.util.List;

public interface BlockService {
    List<BlockList> getBlockList(BlockListReq req);
    BlockDetail getBlockDetail(BlockDetailReq req);
}
