package com.platon.browser.response.transaction;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.platon.browser.config.json.CustomLatSerializer;
import com.platon.browser.config.json.CustomVersionSerializer;

import java.math.BigDecimal;
import java.util.List;

/**
 * 交易详情返回对象
 *
 * @author zhangrj
 * @file TransactionDetailsResp.java
 * @description
 * @data 2019年8月31日
 */
public class TransactionDetailsResp {

    private String txHash; // 交易hash

    private String from; // 发送方地址

    private String to; // 接收方地址（操作地址）

    private Long timestamp; // 交易时间

    private Long serverTime; // 服务器时间

    private String confirmNum; // 区块确认数

    private Long blockNumber; // 交易所在区块高度

    private String gasLimit; // 能量限制

    private String gasUsed; // 能量消耗

    private BigDecimal gasPrice; // 能量价格

    private BigDecimal value; // 金额(单位:von)

    private BigDecimal actualTxCost; // 交易费用(单位:von)

    private String txType; // 交易类型

    private String input; // 附加输入数据

    private String txInfo; // 附加输入数据解析后的结构

    private String failReason; // 失败原因

    private Boolean first; // 是否第一条记录

    private Boolean last; // 是否最后一条记录

    private String receiveType; // 此字段表示的是to字段存储的账户类型：account-钱包地址，contract-合约地址，

    private Integer contractType; // 合约类型 1-evm合约 2-wasm合约

    private String method; // 合约调用函数

    private String contractName; // 合约名称

    private String rPAccount; // 锁仓计划的地址

    private BigDecimal rPNum;

    private List<TransactionDetailsRPPlanResp> rPPlan;

    private List<TransactionDetailsEvidencesResp> evidences;

    private String nodeId; // 节点id

    private String nodeName; // 节点名称

    private String benefitAddr; // 用于接受出块奖励和质押奖励的收益账户

    private String externalId; // 外部Id(有长度限制，给第三方拉取节点描述的Id)

    private String externalUrl; // 外部url

    private String website; // 节点的第三方主页(有长度限制，表示该节点的主页)

    private String details; // 节点的描述(有长度限制，表示该节点的描述)

    private String programVersion; // 程序的真实版本，治理rpc获取

    private BigDecimal applyAmount; // 申请赎回的金额

    private BigDecimal redeemLocked; // 赎回中被锁定的金额

    private Integer redeemStatus; // 赎回状态， 1： 退回中 2：退回成功

    private String redeemUnLockedBlock; // 预计赎回到账的区块

    private String proposalUrl; // 提案的github地址 https://github.com/ethereum/EIPs/blob/master/EIPS/eip-100.md eip-100为提案id

    private String proposalHash; // 提案id

    private String proposalOption; // 投票 1：文本提案 2：升级提案 3：参数提案

    private String proposalNewVersion; // 升级提案的版本

    private String declareVersion; // 声明的版本

    private Integer txReceiptStatus; // 交易状态

    private String voteStatus; // 投票选项 1:支持 2:反对 3:弃权

    private String evidence;// 证据

    private Integer reportType;// 举报类型:1：区块双签

    private BigDecimal reportRewards;// 举报奖励

    private Integer reportStatus;// 举报状态 \r\n1：失败\r\n2：成功

    private String pipNum;// 提案pip编号

    private Integer proposalStatus;// 提案状态\r\n1：投票中\r\n2：通过\r\n3：失败\r\n4：预升级\r\n5：升级完成

    private String proposalTitle;// 提案标题

    private String preHash;// 上一条记录

    private String nextHash;// 下一条记录

    private BigDecimal txAmount; // 交易费用

    private String delegationRatio; // 委托比例

    private List<TransactionDetailsRewardsResp> rewards;

    // private String innerFrom; // 内部交易from
    // private String innerTo; // 内部交易to
    // private String innerValue; // 内部交易value
    // private String innerContractAddr; // 内部交易对应地址
    // private String innerContractName; // 内部交易对应名称
    // private String innerSymbol; // 内部交易对应单位

    private List<Arc20Param> erc20Params;

    private List<Arc721Param> erc721Params;

    private List<Arc1155Param> erc1155Params;

    private List<InternalTransferParam> internalTransferParams;

    /**
     * 总领取解锁的委托
     */
    private BigDecimal redeemDelegationValue;

    public String getTxHash() {
        return this.txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public String getFrom() {
        return this.from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return this.to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getServerTime() {
        return this.serverTime;
    }

    public void setServerTime(Long serverTime) {
        this.serverTime = serverTime;
    }

    public String getConfirmNum() {
        return this.confirmNum;
    }

    public void setConfirmNum(String confirmNum) {
        this.confirmNum = confirmNum;
    }

    public Long getBlockNumber() {
        return this.blockNumber;
    }

    public void setBlockNumber(Long blockNumber) {
        this.blockNumber = blockNumber;
    }

    public String getGasLimit() {
        return this.gasLimit;
    }

    public void setGasLimit(String gasLimit) {
        this.gasLimit = gasLimit;
    }

    public String getGasUsed() {
        return this.gasUsed;
    }

    public void setGasUsed(String gasUsed) {
        this.gasUsed = gasUsed;
    }

    @JsonSerialize(using = CustomLatSerializer.class)
    public BigDecimal getGasPrice() {
        return this.gasPrice;
    }

    public void setGasPrice(BigDecimal gasPrice) {
        this.gasPrice = gasPrice;
    }

    @JsonSerialize(using = CustomLatSerializer.class)
    public BigDecimal getValue() {
        return this.value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    @JsonSerialize(using = CustomLatSerializer.class)
    public BigDecimal getActualTxCost() {
        return this.actualTxCost;
    }

    public void setActualTxCost(BigDecimal actualTxCost) {
        this.actualTxCost = actualTxCost;
    }

    public String getTxType() {
        return this.txType;
    }

    public void setTxType(String txType) {
        this.txType = txType;
    }

    public String getInput() {
        return this.input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getTxInfo() {
        return this.txInfo;
    }

    public void setTxInfo(String txInfo) {
        this.txInfo = txInfo;
    }

    public String getFailReason() {
        return this.failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    public Boolean getFirst() {
        return this.first;
    }

    public void setFirst(Boolean first) {
        this.first = first;
    }

    public Boolean getLast() {
        return this.last;
    }

    public void setLast(Boolean last) {
        this.last = last;
    }

    public String getReceiveType() {
        return this.receiveType;
    }

    public void setReceiveType(String receiveType) {
        this.receiveType = receiveType;
    }

    public String getRPAccount() {
        return this.rPAccount;
    }

    public void setRPAccount(String rPAccount) {
        this.rPAccount = rPAccount;
    }

    @JsonSerialize(using = CustomLatSerializer.class)
    public BigDecimal getRPNum() {
        return this.rPNum;
    }

    public void setRPNum(BigDecimal rPNum) {
        this.rPNum = rPNum;
    }

    public List<TransactionDetailsRPPlanResp> getRPPlan() {
        return this.rPPlan;
    }

    public void setRPPlan(List<TransactionDetailsRPPlanResp> rPPlan) {
        this.rPPlan = rPPlan;
    }

    public List<TransactionDetailsEvidencesResp> getEvidences() {
        return this.evidences;
    }

    public void setEvidences(List<TransactionDetailsEvidencesResp> evidences) {
        this.evidences = evidences;
    }

    public String getNodeId() {
        return this.nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeName() {
        return this.nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getBenefitAddr() {
        return this.benefitAddr;
    }

    public void setBenefitAddr(String benefitAddr) {
        this.benefitAddr = benefitAddr;
    }

    public String getExternalId() {
        return this.externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getWebsite() {
        return this.website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getDetails() {
        return this.details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @JsonSerialize(using = CustomVersionSerializer.class)
    public String getProgramVersion() {
        return this.programVersion;
    }

    public void setProgramVersion(String programVersion) {
        this.programVersion = programVersion;
    }

    @JsonSerialize(using = CustomLatSerializer.class)
    public BigDecimal getApplyAmount() {
        return this.applyAmount;
    }

    public void setApplyAmount(BigDecimal applyAmount) {
        this.applyAmount = applyAmount;
    }

    @JsonSerialize(using = CustomLatSerializer.class)
    public BigDecimal getRedeemLocked() {
        return this.redeemLocked;
    }

    public void setRedeemLocked(BigDecimal redeemLocked) {
        this.redeemLocked = redeemLocked;
    }

    public Integer getRedeemStatus() {
        return this.redeemStatus;
    }

    public void setRedeemStatus(Integer redeemStatus) {
        this.redeemStatus = redeemStatus;
    }

    public String getRedeemUnLockedBlock() {
        return this.redeemUnLockedBlock;
    }

    public void setRedeemUnLockedBlock(String redeemUnLockedBlock) {
        this.redeemUnLockedBlock = redeemUnLockedBlock;
    }

    public String getProposalUrl() {
        return this.proposalUrl;
    }

    public void setProposalUrl(String proposalUrl) {
        this.proposalUrl = proposalUrl;
    }

    public String getProposalHash() {
        return this.proposalHash;
    }

    public void setProposalHash(String proposalHash) {
        this.proposalHash = proposalHash;
    }

    public String getProposalOption() {
        return this.proposalOption;
    }

    public void setProposalOption(String proposalOption) {
        this.proposalOption = proposalOption;
    }

    @JsonSerialize(using = CustomVersionSerializer.class)
    public String getProposalNewVersion() {
        return this.proposalNewVersion;
    }

    public void setProposalNewVersion(String proposalNewVersion) {
        this.proposalNewVersion = proposalNewVersion;
    }

    @JsonSerialize(using = CustomVersionSerializer.class)
    public String getDeclareVersion() {
        return this.declareVersion;
    }

    public void setDeclareVersion(String declareVersion) {
        this.declareVersion = declareVersion;
    }

    public Integer getTxReceiptStatus() {
        return this.txReceiptStatus;
    }

    public void setTxReceiptStatus(Integer txReceiptStatus) {
        this.txReceiptStatus = txReceiptStatus;
    }

    public String getEvidence() {
        return this.evidence;
    }

    public void setEvidence(String evidence) {
        this.evidence = evidence;
    }

    public Integer getReportType() {
        return this.reportType;
    }

    public void setReportType(Integer reportType) {
        this.reportType = reportType;
    }

    @JsonSerialize(using = CustomLatSerializer.class)
    public BigDecimal getReportRewards() {
        return this.reportRewards;
    }

    public void setReportRewards(BigDecimal reportRewards) {
        this.reportRewards = reportRewards;
    }

    public Integer getReportStatus() {
        return this.reportStatus;
    }

    public void setReportStatus(Integer reportStatus) {
        this.reportStatus = reportStatus;
    }

    public String getPipNum() {
        return this.pipNum;
    }

    public void setPipNum(String pipNum) {
        this.pipNum = pipNum;
    }

    public Integer getProposalStatus() {
        return this.proposalStatus;
    }

    public void setProposalStatus(Integer proposalStatus) {
        this.proposalStatus = proposalStatus;
    }

    public String getProposalTitle() {
        return this.proposalTitle;
    }

    public void setProposalTitle(String proposalTitle) {
        this.proposalTitle = proposalTitle;
    }

    public String getPreHash() {
        return this.preHash;
    }

    public void setPreHash(String preHash) {
        this.preHash = preHash;
    }

    public String getNextHash() {
        return this.nextHash;
    }

    public void setNextHash(String nextHash) {
        this.nextHash = nextHash;
    }

    @JsonSerialize(using = CustomLatSerializer.class)
    public BigDecimal getTxAmount() {
        return this.txAmount;
    }

    public void setTxAmount(BigDecimal txAmount) {
        this.txAmount = txAmount;
    }

    public String getVoteStatus() {
        return this.voteStatus;
    }

    public void setVoteStatus(String voteStatus) {
        this.voteStatus = voteStatus;
    }

    public String getExternalUrl() {
        return this.externalUrl;
    }

    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }

    public String getrPAccount() {
        return this.rPAccount;
    }

    public void setrPAccount(String rPAccount) {
        this.rPAccount = rPAccount;
    }

    public BigDecimal getrPNum() {
        return this.rPNum;
    }

    public void setrPNum(BigDecimal rPNum) {
        this.rPNum = rPNum;
    }

    public List<TransactionDetailsRPPlanResp> getrPPlan() {
        return this.rPPlan;
    }

    public void setrPPlan(List<TransactionDetailsRPPlanResp> rPPlan) {
        this.rPPlan = rPPlan;
    }

    public String getDelegationRatio() {
        return this.delegationRatio;
    }

    public void setDelegationRatio(String delegationRatio) {
        this.delegationRatio = delegationRatio;
    }

    public List<TransactionDetailsRewardsResp> getRewards() {
        return this.rewards;
    }

    public void setRewards(List<TransactionDetailsRewardsResp> rewards) {
        this.rewards = rewards;
    }

    public Integer getContractType() {
        return this.contractType;
    }

    public void setContractType(Integer contractType) {
        this.contractType = contractType;
    }

    public String getMethod() {
        return this.method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getContractName() {
        return this.contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public List<Arc721Param> getErc721Params() {
        return erc721Params;
    }

    public void setErc721Params(List<Arc721Param> erc721Params) {
        this.erc721Params = erc721Params;
    }

    public List<Arc20Param> getErc20Params() {
        return erc20Params;
    }

    public void setErc20Params(List<Arc20Param> erc20Params) {
        this.erc20Params = erc20Params;
    }

    public List<Arc1155Param> getErc1155Params() {
        return erc1155Params;
    }

    public void setErc1155Params(List<Arc1155Param> erc1155Params) {
        this.erc1155Params = erc1155Params;
    }

    public List<InternalTransferParam> getInternalTransferParams() {
        return internalTransferParams;
    }

    public void setInternalTransferParams(List<InternalTransferParam> internalTransferParams) {
        this.internalTransferParams = internalTransferParams;
    }

    public BigDecimal getRedeemDelegationValue() {
        return redeemDelegationValue;
    }

    public void setRedeemDelegationValue(BigDecimal redeemDelegationValue) {
        this.redeemDelegationValue = redeemDelegationValue;
    }
}
