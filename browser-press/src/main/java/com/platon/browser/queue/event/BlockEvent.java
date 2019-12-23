package com.platon.browser.queue.event;

import com.platon.browser.elasticsearch.dto.Block;

import java.util.List;

public class BlockEvent {
    private List<Block> blockList;

    public List<Block> getBlockList() {
        return blockList;
    }

    public void setBlockList(List<Block> blockList) {
        this.blockList = blockList;
    }
}
