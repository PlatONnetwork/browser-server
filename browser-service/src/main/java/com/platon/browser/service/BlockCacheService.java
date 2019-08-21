package com.platon.browser.service;

import com.platon.browser.dao.entity.Block;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.block.BlockListItem;
import com.platon.browser.dto.block.BlockPushItem;

import java.util.List;
import java.util.Set;

public interface BlockCacheService {
    void clear();
    void update(Set<Block> items);
    void reset(boolean clearOld);
}
