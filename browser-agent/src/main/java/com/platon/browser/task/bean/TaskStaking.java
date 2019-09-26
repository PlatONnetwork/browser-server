package com.platon.browser.task.bean;

import lombok.Data;

@Data
public class TaskStaking extends TaskBase {
    private String nodeId;
    private Long stakingBlockNum;
    private String stakingHas;
    private String stakingLocked;
    private String stakingReduction;
    private String statDelegateHas;
    private String statDelegateLocked;
    private String statDelegateReduction;
    private Integer statDelegateQty;
    private String stakingIcon;
    private String externalName;
}
