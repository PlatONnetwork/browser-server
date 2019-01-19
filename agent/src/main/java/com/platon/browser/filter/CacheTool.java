package com.platon.browser.filter;

import com.platon.browser.dao.entity.Block;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CacheTool {
    public final static List<Block>  blocks = new ArrayList<>();
    public final static Set<Block> blocksCache = new HashSet<>();
    public static long currentBlockNumber = 1;
}
