package com.platon.browser.service.cache;

import com.platon.browser.dao.entity.Block;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.block.BlockListItem;
import com.platon.browser.dto.block.BlockPushItem;

import java.util.List;
import java.util.Set;

public interface BlockCacheService {
    void clearBlockCache(String chainId);
    void updateBlockCache(String chainId, Set<Block> items);
    void resetBlockCache(String chainId, boolean clearOld);
    RespPage<BlockListItem> getBlockPage(String chainId, int pageNum, int pageSize);
    List<BlockPushItem> getBlockPushCache(String chainId, int pageNum, int pageSize);
}
