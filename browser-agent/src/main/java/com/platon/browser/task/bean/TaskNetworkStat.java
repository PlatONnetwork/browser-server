package com.platon.browser.task.bean;

import lombok.Data;

import java.math.BigInteger;

@Data
public class TaskNetworkStat  extends TaskBase{
    private volatile String issueValue;
    private volatile String turnValue;
    private volatile Long currentNumber;
}
