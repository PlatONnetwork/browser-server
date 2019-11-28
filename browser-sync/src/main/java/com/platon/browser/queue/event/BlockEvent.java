package com.platon.browser.queue.event;

import com.platon.browser.elasticsearch.dto.Block;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.concurrent.CompletableFuture;

/**
 * 自检事件
 */
@Data
@Builder
@Accessors(chain = true)
public class BlockEvent {
    private CompletableFuture<Block> blockCF;
}
