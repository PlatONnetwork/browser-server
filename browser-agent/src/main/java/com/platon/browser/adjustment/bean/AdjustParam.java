package com.platon.browser.adjustment.bean;

import lombok.Data;

/**
 * 质押或委托账户调账条目
 */
@Data
public class AdjustParam {
    private String optType;
    private String nodeId;
    private String stakingBlockNum;
    private String addr;
    private String lock;
    private String hes;
}
