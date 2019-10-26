package com.platon.browser.task.bean;

import lombok.Data;

@Data
public class TaskNetworkStat  extends TaskBase{
    private String issueValue;
    private String turnValue;
    private Long currentNumber;
}
