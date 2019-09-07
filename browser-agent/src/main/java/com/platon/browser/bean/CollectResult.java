package com.platon.browser.bean;

import com.platon.browser.dao.entity.Block;
import com.platon.browser.dto.CustomBlock;
import lombok.Data;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @Auther: Chendongming
 * @Date: 2019/9/6 14:22
 * @Description:
 */
@Data
public class CollectResult {
    // 并发采集的块信息，无序
    public final static Map<Long, CustomBlock> CONCURRENT_BLOCK_MAP = new ConcurrentHashMap<>();
    // 由于异常而未采集的区块号列表
    public final static Set<BigInteger> RETRY_NUMBERS = new CopyOnWriteArraySet<>();
    // 已排序的区块信息列表
    private final static List<CustomBlock> SORTED_BLOCKS = new LinkedList<>();
    public static List <CustomBlock> getSortedBlocks() {
        SORTED_BLOCKS.clear();
        SORTED_BLOCKS.addAll(CONCURRENT_BLOCK_MAP.values());
        SORTED_BLOCKS.sort(Comparator.comparing(Block::getNumber));
        return SORTED_BLOCKS;
    }
    public static void reset() {
        CONCURRENT_BLOCK_MAP.clear();
        RETRY_NUMBERS.clear();
        SORTED_BLOCKS.clear();
    }
}
