package com.platon.browser.old.task.bean;

import lombok.Data;

@Data
public class TaskBase {
    // 是否已经被合并到采块线程对应结果中
    private volatile boolean isMerged =false;
}
