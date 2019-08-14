package com.platon.browser.res.address;

import lombok.Data;

@Data
public class QueryDetailResp {
	private Integer type;                //地址详情  1：账号   2：合约   3：内置合约
    private String balance;             //余额(单位:LAT)
    private String restrictingBalance;  //锁仓余额(单位:LAT)
    private String stakingValue;        //质押的金额
    private String delegateValue;       //委托的金额
    private String redeemedValue;       //赎回中的金额
    private Integer txQty;             //交易总数
    private Integer transferQty;         //转账交易总数
    private Integer delegateQty;         //委托交易总数
    private Integer stakingQty;          //验证人交易总数
    private Integer proposalQty;         //治理交易总数
    private Integer candidateCount;      //已委托验证人
    private String delegateHes;         //未锁定委托（LAT）
    private String delegateLocked;      //已锁定委托（LAT）
    private String delegateUnlock;      //已解除委托（LAT）   
    private String delegateReduction;    //赎回中委托（LAT）   
    private String contractName;        //合约名称
    private String contractCreate;      //合约创建者地址
    private String contractCreateHash; //合约创建哈希
}
