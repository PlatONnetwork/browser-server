package com.platon.browser.filter;

import com.platon.browser.dao.entity.Block;

import java.util.ArrayList;
import java.util.List;

public class CacheTool {
    public final static List<Block>  blocks = new ArrayList<>();
    public static long currentBlockNumber = 1;
}
