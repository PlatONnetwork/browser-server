package com.platon.browser.dto;

import com.platon.browser.dto.block.BlockInfo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Data
public class BlockIncrement {
    private boolean changed=false; // 是否有改动
    private List<BlockInfo> increment=new ArrayList<>(); // 增量数据
    private ReentrantReadWriteLock lock=new ReentrantReadWriteLock(); // 读写锁
}
