package com.platon.browser.task.bean;

import lombok.Data;

@Data
public class TaskBase {
    // 是否已经被合并到采块线程对应的地址结果中
    private volatile boolean isMerged =false;
}
