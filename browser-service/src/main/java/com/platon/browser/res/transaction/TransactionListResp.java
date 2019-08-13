package com.platon.browser.res.transaction;

import lombok.Data;

@Data
public class TransactionListResp {
	private String txHash;    //交易hash
    private String from;      //发送方地址（操作地址）
    private String to;        //接收方地址
    private String value;          //金额(单位:von)
    private String actualTxCost;    //交易费用(单位:von)
    private String txType;             //交易类型 0：转账  1：合约发布  2：合约调用    5：MPC交易
    private Long serverTime;    //服务器时间
    private Long timestamp;//出块时间
    private Long blockNumber;  //交易所在区块高度
    private String failReason;        //失败原因
    private String receiveType; //此字段表示的是to字段存储的账户类型：account-钱包地址，contract-合约地址，
                                    //前端页面在点击接收方的地址时，根据此字段来决定是跳转到账户详情还是合约详情
}
