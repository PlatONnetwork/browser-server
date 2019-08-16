package com.platon.browser.res.transaction;

import java.util.List;

import lombok.Data;

@Data
public class TransactionDetailsResp {
	private String txHash;      //交易hash
    private String from;       //发送方地址
    private String to;         //接收方地址（操作地址）
    private Long timestamp;    //交易时间
    private Long serverTime;      //服务器时间
    private String confirmNum;         //区块确认数
    private Long blockNumber;    //交易所在区块高度
    private Long gasLimit;           //能量限制
    private Long gasUsed;          //能量消耗
    private Long gasPrice;        //能量价格
    private Long value;            //金额(单位:von)
    private String actualTxCost;      //交易费用(单位:von)
    private String txType;              //交易类型
    private String input;               //附加输入数据
    private String txInfo;   //附加输入数据解析后的结构
    private String failReason;          //失败原因
    private Boolean first;            //是否第一条记录
    private Boolean last;              //是否最后一条记录
    private String receiveType;  //此字段表示的是to字段存储的账户类型：account-钱包地址，contract-合约地址，
    private String RPAccount;           //锁仓计划的地址
    private String RPNum;
    private List<TransactionDetailsRPPlanResp> RPPlan;
    private List<TransactionDetailsEvidencesResp> evidences;
    private String nodeId;              //节点id
    private String nodeName;            //节点名称
    private String benefitAddr;         //用于接受出块奖励和质押奖励的收益账户
    private String externalId;          //外部Id(有长度限制，给第三方拉取节点描述的Id)
    private String website;             //节点的第三方主页(有长度限制，表示该节点的主页)
    private String details;             //节点的描述(有长度限制，表示该节点的描述)
    private String programVersion;      //程序的真实版本，治理rpc获取
    private String applyAmount;         //申请赎回的金额
    private String redeemLocked;        //赎回中被锁定的金额
    private String redeemStatus;      //赎回状态， 1： 退回中   2：退回成功 
    private String redeemUnLockedBlock; //预计赎回到账的区块
    private String proposalUrl;         //提案的github地址  https://github.com/ethereum/EIPs/blob/master/EIPS/eip-100.md  eip-100为提案id
    private String proposalHash;        //提案id
    private String proposalOption;      //投票  1：文本提案    2：升级提案   3：参数提案
    private String proposalNewVersion;  //升级提案的版本
    private String declareVersion;      //声明的版本 
    private String txReceiptStatus;     //交易状态
}
