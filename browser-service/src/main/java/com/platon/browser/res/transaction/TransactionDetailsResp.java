package com.platon.browser.res.transaction;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.platon.browser.config.CustomLatSerializer;

public class TransactionDetailsResp {
	private String txHash;      //交易hash
    private String from;       //发送方地址
    private String to;         //接收方地址（操作地址）
    private Long timestamp;    //交易时间
    private Long serverTime;      //服务器时间
    private String confirmNum;         //区块确认数
    private Long blockNumber;    //交易所在区块高度
    private String gasLimit;           //能量限制
    private String gasUsed;          //能量消耗
    private String gasPrice;        //能量价格
    private String value;            //金额(单位:von)
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
    private Integer redeemStatus;      //赎回状态， 1： 退回中   2：退回成功 
    private String redeemUnLockedBlock; //预计赎回到账的区块
    private String proposalUrl;         //提案的github地址  https://github.com/ethereum/EIPs/blob/master/EIPS/eip-100.md  eip-100为提案id
    private String proposalHash;        //提案id
    private String proposalOption;      //投票  1：文本提案    2：升级提案   3：参数提案
    private String proposalNewVersion;  //升级提案的版本
    private String declareVersion;      //声明的版本 
    private Integer txReceiptStatus;     //交易状态
    private String voteStatus;      //投票选项 1:支持  2:反对 3:弃权
    
    private String evidence;//证据
    private Integer reportType;//举报类型:1：区块双签
    private String reportRewards;//举报奖励
    private Integer reportStatus;//举报状态 \r\n1：失败\r\n2：成功
    
    private String pipNum;//提案pip编号
    private Integer proposalStatus;//提案状态\r\n1：投票中\r\n2：通过\r\n3：失败\r\n4：预升级\r\n5：升级完成
    private String proposalTitle;//提案标题
    
    private String preHash;// 上一条记录
    private String nextHash;// 下一条记录
    
    private String txAmount; //惨剧费用
    

	public String getTxHash() {
		return txHash;
	}
	public void setTxHash(String txHash) {
		this.txHash = txHash;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	public Long getServerTime() {
		return serverTime;
	}
	public void setServerTime(Long serverTime) {
		this.serverTime = serverTime;
	}
	public String getConfirmNum() {
		return confirmNum;
	}
	public void setConfirmNum(String confirmNum) {
		this.confirmNum = confirmNum;
	}
	public Long getBlockNumber() {
		return blockNumber;
	}
	public void setBlockNumber(Long blockNumber) {
		this.blockNumber = blockNumber;
	}
	public String getGasLimit() {
		return gasLimit;
	}
	public void setGasLimit(String gasLimit) {
		this.gasLimit = gasLimit;
	}
	public String getGasUsed() {
		return gasUsed;
	}
	public void setGasUsed(String gasUsed) {
		this.gasUsed = gasUsed;
	}
	@JsonSerialize(using = CustomLatSerializer.class)
	public String getGasPrice() {
		return gasPrice;
	}
	public void setGasPrice(String gasPrice) {
		this.gasPrice = gasPrice;
	}
	@JsonSerialize(using = CustomLatSerializer.class)
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@JsonSerialize(using = CustomLatSerializer.class)
	public String getActualTxCost() {
		return actualTxCost;
	}
	public void setActualTxCost(String actualTxCost) {
		this.actualTxCost = actualTxCost;
	}
	public String getTxType() {
		return txType;
	}
	public void setTxType(String txType) {
		this.txType = txType;
	}
	public String getInput() {
		return input;
	}
	public void setInput(String input) {
		this.input = input;
	}
	public String getTxInfo() {
		return txInfo;
	}
	public void setTxInfo(String txInfo) {
		this.txInfo = txInfo;
	}
	public String getFailReason() {
		return failReason;
	}
	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}
	public Boolean getFirst() {
		return first;
	}
	public void setFirst(Boolean first) {
		this.first = first;
	}
	public Boolean getLast() {
		return last;
	}
	public void setLast(Boolean last) {
		this.last = last;
	}
	public String getReceiveType() {
		return receiveType;
	}
	public void setReceiveType(String receiveType) {
		this.receiveType = receiveType;
	}
	public String getRPAccount() {
		return RPAccount;
	}
	public void setRPAccount(String rPAccount) {
		RPAccount = rPAccount;
	}
	public String getRPNum() {
		return RPNum;
	}
	public void setRPNum(String rPNum) {
		RPNum = rPNum;
	}
	public List<TransactionDetailsRPPlanResp> getRPPlan() {
		return RPPlan;
	}
	public void setRPPlan(List<TransactionDetailsRPPlanResp> rPPlan) {
		RPPlan = rPPlan;
	}
	public List<TransactionDetailsEvidencesResp> getEvidences() {
		return evidences;
	}
	public void setEvidences(List<TransactionDetailsEvidencesResp> evidences) {
		this.evidences = evidences;
	}
	public String getNodeId() {
		return nodeId;
	}
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public String getBenefitAddr() {
		return benefitAddr;
	}
	public void setBenefitAddr(String benefitAddr) {
		this.benefitAddr = benefitAddr;
	}
	public String getExternalId() {
		return externalId;
	}
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public String getProgramVersion() {
		return programVersion;
	}
	public void setProgramVersion(String programVersion) {
		this.programVersion = programVersion;
	}
	@JsonSerialize(using = CustomLatSerializer.class)
	public String getApplyAmount() {
		return applyAmount;
	}
	public void setApplyAmount(String applyAmount) {
		this.applyAmount = applyAmount;
	}
	@JsonSerialize(using = CustomLatSerializer.class)
	public String getRedeemLocked() {
		return redeemLocked;
	}
	public void setRedeemLocked(String redeemLocked) {
		this.redeemLocked = redeemLocked;
	}
	public Integer getRedeemStatus() {
		return redeemStatus;
	}
	public void setRedeemStatus(Integer redeemStatus) {
		this.redeemStatus = redeemStatus;
	}
	public String getRedeemUnLockedBlock() {
		return redeemUnLockedBlock;
	}
	public void setRedeemUnLockedBlock(String redeemUnLockedBlock) {
		this.redeemUnLockedBlock = redeemUnLockedBlock;
	}
	public String getProposalUrl() {
		return proposalUrl;
	}
	public void setProposalUrl(String proposalUrl) {
		this.proposalUrl = proposalUrl;
	}
	public String getProposalHash() {
		return proposalHash;
	}
	public void setProposalHash(String proposalHash) {
		this.proposalHash = proposalHash;
	}
	public String getProposalOption() {
		return proposalOption;
	}
	public void setProposalOption(String proposalOption) {
		this.proposalOption = proposalOption;
	}
	public String getProposalNewVersion() {
		return proposalNewVersion;
	}
	public void setProposalNewVersion(String proposalNewVersion) {
		this.proposalNewVersion = proposalNewVersion;
	}
	public String getDeclareVersion() {
		return declareVersion;
	}
	public void setDeclareVersion(String declareVersion) {
		this.declareVersion = declareVersion;
	}
	public Integer getTxReceiptStatus() {
		return txReceiptStatus;
	}
	public void setTxReceiptStatus(Integer txReceiptStatus) {
		this.txReceiptStatus = txReceiptStatus;
	}
	public String getEvidence() {
		return evidence;
	}
	public void setEvidence(String evidence) {
		this.evidence = evidence;
	}
	public Integer getReportType() {
		return reportType;
	}
	public void setReportType(Integer reportType) {
		this.reportType = reportType;
	}
	public String getReportRewards() {
		return reportRewards;
	}
	public void setReportRewards(String reportRewards) {
		this.reportRewards = reportRewards;
	}
	public Integer getReportStatus() {
		return reportStatus;
	}
	public void setReportStatus(Integer reportStatus) {
		this.reportStatus = reportStatus;
	}
	public String getPipNum() {
		return pipNum;
	}
	public void setPipNum(String pipNum) {
		this.pipNum = pipNum;
	}
	public Integer getProposalStatus() {
		return proposalStatus;
	}
	public void setProposalStatus(Integer proposalStatus) {
		this.proposalStatus = proposalStatus;
	}
	public String getProposalTitle() {
		return proposalTitle;
	}
	public void setProposalTitle(String proposalTitle) {
		this.proposalTitle = proposalTitle;
	}
	public String getPreHash() {
		return preHash;
	}
	public void setPreHash(String preHash) {
		this.preHash = preHash;
	}
	public String getNextHash() {
		return nextHash;
	}
	public void setNextHash(String nextHash) {
		this.nextHash = nextHash;
	}
    @JsonSerialize(using = CustomLatSerializer.class)
	public String getTxAmount() {
		return txAmount;
	}
	public void setTxAmount(String txAmount) {
		this.txAmount = txAmount;
	}
	public String getVoteStatus() {
		return voteStatus;
	}
	public void setVoteStatus(String voteStatus) {
		this.voteStatus = voteStatus;
	}

}
