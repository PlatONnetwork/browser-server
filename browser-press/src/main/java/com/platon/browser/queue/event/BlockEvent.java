package com.platon.browser.queue.event;

import com.platon.browser.elasticsearch.dto.Block;
import lombok.Data;

import java.util.List;
@Data
public class BlockEvent {
    private List<Block> blockList;
}
