package com.platon.browser.resp.staking;

import lombok.Data;

@Data
public class DelegationListByAddressResp {
	private String nodeId;            //节点id
    private String nodeName;          //节点名称
    private String delegateValue;     //委托数量
    private String delegateHes;       //未锁定委托（LAT）
    private String delegateLocked;    //已锁定委托（LAT）
    private String allDelegateLocked; //当前验证人总接收的锁定委托量（LAT）
    private String delegateUnlock;    //已解除委托（LAT） 
    private String delegateReduction;  //赎回中委托（LAT） 
}
