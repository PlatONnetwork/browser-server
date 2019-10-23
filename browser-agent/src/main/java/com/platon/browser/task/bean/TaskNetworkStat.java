package com.platon.browser.task.bean;

import lombok.Data;

@Data
public class TaskNetworkStat  extends TaskBase{
    private volatile String issueValue;
    private volatile String turnValue;
    private volatile Long currentNumber;
}
