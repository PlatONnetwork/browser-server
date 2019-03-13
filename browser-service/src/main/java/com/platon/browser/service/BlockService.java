package com.platon.browser.service;

import com.platon.browser.dao.entity.Block;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.block.BlockDetail;
import com.platon.browser.dto.block.BlockListItem;
import com.platon.browser.dto.transaction.TransactionListItem;
import com.platon.browser.req.block.*;

import java.util.List;
import java.util.Set;

public interface BlockService {
    RespPage<BlockListItem> getPage(BlockPageReq req);
    BlockDetail getDetail(BlockDetailReq req);
    BlockDetail getDetailNavigate(BlockDetailNavigateReq req);
    List<Block> getList(BlockDownloadReq req);
    void clearCache(String chainId);
    void updateCache(String chainId, Set<Block> data);
    RespPage<TransactionListItem> getBlockTransactionList(BlockTransactionPageReq req);
//    RespPage<Ticket> getBlockTicketList(BlockTicketPageReq req);
}
