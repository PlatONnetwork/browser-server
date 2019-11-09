package com.platon.browser.common.task;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TaskCacheBase {
    // 是否已经被合并到采块线程对应结果中
    private volatile boolean isMerged =false;
}
