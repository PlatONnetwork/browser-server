package com.platon.browser.dto;

import com.platon.browser.dto.node.NodeInfo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Data
public class NodeIncrement {
    private boolean changed=false; // 是否有改动
    private List<NodeInfo> increment=new ArrayList<>(); // 增量数据
    private ReentrantReadWriteLock lock=new ReentrantReadWriteLock(); // 读写锁
}
