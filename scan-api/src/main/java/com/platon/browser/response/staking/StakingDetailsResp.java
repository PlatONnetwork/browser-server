package com.platon.browser.response.staking;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.platon.browser.config.json.CustomLatSerializer;

import java.math.BigDecimal;

/**
 * 验证人详情返回对象
 *
 * @author zhangrj
 * @file StakingDetailsResp.java
 * @description
 * @data 2019年8月31日
 */
public class StakingDetailsResp {

    private String nodeName;            //验证人名称

    private String stakingIcon;         //验证人图标

    private Integer status;              //状态   1:候选中  2:活跃中  3:出块中  4:退出中 5:已退出

    private BigDecimal totalValue;          //质押总数=有效的质押+委托

    private BigDecimal delegateValue;       //委托总数

    private BigDecimal stakingValue;        //质押总数

    private Integer delegateQty;         //委托人数

    private Integer slashLowQty;         //低出块率举报次数

    private Integer slashMultiQty;       //多签举报次数

    private Long blockQty;           //产生的区块数

    private Long expectBlockQty;      //预计的出块数

    private String expectedIncome;      //预计年收化率（从验证人加入时刻开始计算）

    private Long joinTime;            //加入时间

    private Integer verifierTime;        //进入共识验证轮次数

    private BigDecimal rewardValue;         //累计的收益

    private String nodeId;              //节点id

    private String stakingAddr;         //发起质押的账户地址

    /**
     * 发起质押的账户地址类型
     */
    private Integer stakingAddrType;

    private String denefitAddr;         //收益地址

    /**
     * 收益地址类型
     */
    private Integer denefitAddrType;

    private String website;             //节点的第三方主页

    private String details;             //节点的描述

    private String externalId;          //身份证id

    private String externalUrl;          //身份证id连接

    private Long stakingBlockNum;     //最新的质押交易块高

    private BigDecimal statDelegateReduction;//待提取的委托

    private Long leaveTime;  //退出时间

    private Boolean isInit;          //是否为初始节点

    private String deleAnnualizedRate;      //预计委托年收化率（从委托开始计算）

    private String rewardPer;      //委托奖励比例

    private String nextRewardPer;      //下一结算周期委托奖励比例

    private BigDecimal haveDeleReward;      //节点当前质押已领取委托奖励

    private BigDecimal totalDeleReward;      //节点当前质押累积委托奖励

    private BigDecimal deleRewardRed;      //节点当前质押待领取委托奖励

    /**
     * 出块率
     */
    private String genBlocksRate;

    /**
     * 版本
     */
    private String Version;

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getStakingIcon() {
        return stakingIcon;
    }

    public void setStakingIcon(String stakingIcon) {
        this.stakingIcon = stakingIcon;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @JsonSerialize(using = CustomLatSerializer.class)
    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    @JsonSerialize(using = CustomLatSerializer.class)
    public BigDecimal getDelegateValue() {
        return delegateValue;
    }

    public void setDelegateValue(BigDecimal delegateValue) {
        this.delegateValue = delegateValue;
    }

    @JsonSerialize(using = CustomLatSerializer.class)
    public BigDecimal getStakingValue() {
        return stakingValue;
    }

    public void setStakingValue(BigDecimal stakingValue) {
        this.stakingValue = stakingValue;
    }

    public Integer getDelegateQty() {
        return delegateQty;
    }

    public void setDelegateQty(Integer delegateQty) {
        this.delegateQty = delegateQty;
    }

    public Integer getSlashLowQty() {
        return slashLowQty;
    }

    public void setSlashLowQty(Integer slashLowQty) {
        this.slashLowQty = slashLowQty;
    }

    public Integer getSlashMultiQty() {
        return slashMultiQty;
    }

    public void setSlashMultiQty(Integer slashMultiQty) {
        this.slashMultiQty = slashMultiQty;
    }

    public Long getBlockQty() {
        return blockQty;
    }

    public void setBlockQty(Long blockQty) {
        this.blockQty = blockQty;
    }

    public Long getExpectBlockQty() {
        return expectBlockQty;
    }

    public void setExpectBlockQty(Long expectBlockQty) {
        this.expectBlockQty = expectBlockQty;
    }

    public String getExpectedIncome() {
        return expectedIncome;
    }

    public void setExpectedIncome(String expectedIncome) {
        this.expectedIncome = expectedIncome;
    }

    public Long getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(Long joinTime) {
        this.joinTime = joinTime;
    }

    public Integer getVerifierTime() {
        return verifierTime;
    }

    public void setVerifierTime(Integer verifierTime) {
        this.verifierTime = verifierTime;
    }

    @JsonSerialize(using = CustomLatSerializer.class)
    public BigDecimal getRewardValue() {
        return rewardValue;
    }

    public void setRewardValue(BigDecimal rewardValue) {
        this.rewardValue = rewardValue;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getStakingAddr() {
        return stakingAddr;
    }

    public void setStakingAddr(String stakingAddr) {
        this.stakingAddr = stakingAddr;
    }

    public String getDenefitAddr() {
        return denefitAddr;
    }

    public void setDenefitAddr(String denefitAddr) {
        this.denefitAddr = denefitAddr;
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

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public Long getStakingBlockNum() {
        return stakingBlockNum;
    }

    public void setStakingBlockNum(Long stakingBlockNum) {
        this.stakingBlockNum = stakingBlockNum;
    }

    @JsonSerialize(using = CustomLatSerializer.class)
    public BigDecimal getStatDelegateReduction() {
        return statDelegateReduction;
    }

    public void setStatDelegateReduction(BigDecimal statDelegateReduction) {
        this.statDelegateReduction = statDelegateReduction;
    }

    public Long getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(Long leaveTime) {
        this.leaveTime = leaveTime;
    }

    public Boolean getIsInit() {
        return isInit;
    }

    public void setIsInit(Boolean isInit) {
        this.isInit = isInit;
    }

    public String getExternalUrl() {
        return externalUrl;
    }

    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }

    public String getDeleAnnualizedRate() {
        return deleAnnualizedRate;
    }

    public void setDeleAnnualizedRate(String deleAnnualizedRate) {
        this.deleAnnualizedRate = deleAnnualizedRate;
    }

    public String getRewardPer() {
        return rewardPer;
    }

    public void setRewardPer(String rewardPer) {
        this.rewardPer = rewardPer;
    }

    public String getNextRewardPer() {
        return nextRewardPer;
    }

    public void setNextRewardPer(String nextRewardPer) {
        this.nextRewardPer = nextRewardPer;
    }

    @JsonSerialize(using = CustomLatSerializer.class)
    public BigDecimal getHaveDeleReward() {
        return haveDeleReward;
    }

    public void setHaveDeleReward(BigDecimal haveDeleReward) {
        this.haveDeleReward = haveDeleReward;
    }

    @JsonSerialize(using = CustomLatSerializer.class)
    public BigDecimal getTotalDeleReward() {
        return totalDeleReward;
    }

    public void setTotalDeleReward(BigDecimal totalDeleReward) {
        this.totalDeleReward = totalDeleReward;
    }

    @JsonSerialize(using = CustomLatSerializer.class)
    public BigDecimal getDeleRewardRed() {
        return deleRewardRed;
    }

    public void setDeleRewardRed(BigDecimal deleRewardRed) {
        this.deleRewardRed = deleRewardRed;
    }

    public String getGenBlocksRate() {
        return genBlocksRate;
    }

    public void setGenBlocksRate(String genBlocksRate) {
        this.genBlocksRate = genBlocksRate;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public Integer getStakingAddrType() {
        return stakingAddrType;
    }

    public void setStakingAddrType(Integer stakingAddrType) {
        this.stakingAddrType = stakingAddrType;
    }

    public Integer getDenefitAddrType() {
        return denefitAddrType;
    }

    public void setDenefitAddrType(Integer denefitAddrType) {
        this.denefitAddrType = denefitAddrType;
    }

}
