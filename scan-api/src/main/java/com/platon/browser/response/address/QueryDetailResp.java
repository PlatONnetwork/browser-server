package com.platon.browser.response.address;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.platon.browser.bean.LockDelegate;
import com.platon.browser.config.json.CustomLatSerializer;

/**
 * 查询地址的返回的对象
 *
 * @author zhangrj
 * @file QueryDetailResp.java
 * @description
 * @data 2019年8月31日
 */
public class QueryDetailResp {

    /**
     * 地址详情  1：账号   2：内置合约   3：EVM合约 4:WASM
     */
    private Integer type;

    /**
     * 余额(单位:lat)
     */
    private BigDecimal balance;

    /**
     * 锁仓余额(单位:lat)
     */
    private BigDecimal restrictingBalance;

    /**
     * 质押的金额
     */
    private BigDecimal stakingValue;

    /**
     * 委托的金额
     */
    private BigDecimal delegateValue;

    /**
     * 赎回中的金额
     */
    private BigDecimal redeemedValue;

    /**
     * 交易总数
     */
    private Integer txQty;

    /**
     * 内部转账总数
     */
    private Integer internalTransferQty;

    /**
     * 是否是erc20
     */
    private boolean hasErc20 = false;

    /**
     * token erc20交易总数
     */
    private Integer erc20TxQty;

    /**
     * 是否是erc721
     */
    private boolean hasErc721 = false;

    /**
     * token erc 721交易总数
     */
    private Integer erc721TxQty;

    /**
     * 是否是erc1155
     */
    private boolean hasErc1155 = false;

    /**
     * token erc 1155交易总数
     */
    private Integer erc1155TxQty;

    /**
     * 转账交易总数
     */
    private Integer transferQty;

    /**
     * 委托交易总数
     */
    private Integer delegateQty;

    /**
     * 验证人交易总数
     */
    private Integer stakingQty;

    /**
     * 治理交易总数
     */
    private Integer proposalQty;

    /**
     * 已委托验证人
     */
    private Integer candidateCount;

    /**
     * 未锁定委托（lat）
     */
    private BigDecimal delegateHes;

    /**
     * 已锁定委托（lat）
     */
    private BigDecimal delegateLocked;

    /**
     * 已解除委托（lat）
     */
    private BigDecimal delegateUnlock;

    /**
     * 待赎回委托（lat）
     */
    private BigDecimal delegateReleased;

    /**
     * 待提取委托（lat）
     */
    private BigDecimal delegateClaim;

    /**
     * 已提取委托（lat）
     */
    private BigDecimal haveReward;

    /**
     * 合约名称
     */
    private String contractName;

    /**
     * 合约创建者地址
     */
    private String contractCreate;

    /**
     * 合约创建哈希
     */
    private String contractCreateHash;

    /**
     * 合约bin
     */
    private String contractBin;

    /**
     * 是否锁仓
     */
    private Integer isRestricting;

    /**
     * 是否销毁
     */
    private Integer isDestroy;

    /**
     * 合约销毁哈希
     */
    private String destroyHash;

    /**
     * 合约符号
     */
    private String tokenSymbol;

    /**
     * 合约名称
     */
    private String tokenName;

    /**
     * 委托冻结计划
     */
    private List<LockDelegate> lockDelegateList;

    /**
     * 已解冻的委托金额/待提取委托(lat)
     */
    private String unLockBalance;

    /**
     * 未解冻的委托金额/待赎回委托(lat)
     */
    private String lockBalance;

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @JsonSerialize(using = CustomLatSerializer.class)
    public BigDecimal getBalance() {
        return this.balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @JsonSerialize(using = CustomLatSerializer.class)
    public BigDecimal getRestrictingBalance() {
        return this.restrictingBalance;
    }

    public void setRestrictingBalance(BigDecimal restrictingBalance) {
        this.restrictingBalance = restrictingBalance;
    }

    @JsonSerialize(using = CustomLatSerializer.class)
    public BigDecimal getStakingValue() {
        return this.stakingValue;
    }

    public void setStakingValue(BigDecimal stakingValue) {
        this.stakingValue = stakingValue;
    }

    @JsonSerialize(using = CustomLatSerializer.class)
    public BigDecimal getDelegateValue() {
        return this.delegateValue;
    }

    public void setDelegateValue(BigDecimal delegateValue) {
        this.delegateValue = delegateValue;
    }

    @JsonSerialize(using = CustomLatSerializer.class)
    public BigDecimal getRedeemedValue() {
        return this.redeemedValue;
    }

    public void setRedeemedValue(BigDecimal redeemedValue) {
        this.redeemedValue = redeemedValue;
    }

    public Integer getTxQty() {
        return this.txQty;
    }

    public void setTxQty(Integer txQty) {
        this.txQty = txQty;
    }

    public Integer getTransferQty() {
        return this.transferQty;
    }

    public void setTransferQty(Integer transferQty) {
        this.transferQty = transferQty;
    }

    public Integer getDelegateQty() {
        return this.delegateQty;
    }

    public void setDelegateQty(Integer delegateQty) {
        this.delegateQty = delegateQty;
    }

    public Integer getStakingQty() {
        return this.stakingQty;
    }

    public void setStakingQty(Integer stakingQty) {
        this.stakingQty = stakingQty;
    }

    public Integer getProposalQty() {
        return this.proposalQty;
    }

    public void setProposalQty(Integer proposalQty) {
        this.proposalQty = proposalQty;
    }

    public Integer getCandidateCount() {
        return this.candidateCount;
    }

    public void setCandidateCount(Integer candidateCount) {
        this.candidateCount = candidateCount;
    }

    @JsonSerialize(using = CustomLatSerializer.class)
    public BigDecimal getDelegateHes() {
        return this.delegateHes;
    }

    public void setDelegateHes(BigDecimal delegateHes) {
        this.delegateHes = delegateHes;
    }

    @JsonSerialize(using = CustomLatSerializer.class)
    public BigDecimal getDelegateLocked() {
        return this.delegateLocked;
    }

    public void setDelegateLocked(BigDecimal delegateLocked) {
        this.delegateLocked = delegateLocked;
    }

    @JsonSerialize(using = CustomLatSerializer.class)
    public BigDecimal getDelegateUnlock() {
        return this.delegateUnlock;
    }

    public void setDelegateUnlock(BigDecimal delegateUnlock) {
        this.delegateUnlock = delegateUnlock;
    }

    @JsonSerialize(using = CustomLatSerializer.class)
    public BigDecimal getDelegateReleased() {
        return this.delegateReleased;
    }

    public void setDelegateReleased(BigDecimal delegateReleased) {
        this.delegateReleased = delegateReleased;
    }

    public String getContractName() {
        return this.contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getContractCreate() {
        return this.contractCreate;
    }

    public void setContractCreate(String contractCreate) {
        this.contractCreate = contractCreate;
    }

    public String getContractCreateHash() {
        return this.contractCreateHash;
    }

    public void setContractCreateHash(String contractCreateHash) {
        this.contractCreateHash = contractCreateHash;
    }

    public Integer getIsRestricting() {
        return this.isRestricting;
    }

    public void setIsRestricting(Integer isRestricting) {
        this.isRestricting = isRestricting;
    }

    @JsonSerialize(using = CustomLatSerializer.class)
    public BigDecimal getDelegateClaim() {
        return this.delegateClaim;
    }

    public void setDelegateClaim(BigDecimal delegateClaim) {
        this.delegateClaim = delegateClaim;
    }

    @JsonSerialize(using = CustomLatSerializer.class)
    public BigDecimal getHaveReward() {
        return this.haveReward;
    }

    public void setHaveReward(BigDecimal haveReward) {
        this.haveReward = haveReward;
    }

    public String getContractBin() {
        return this.contractBin;
    }

    public void setContractBin(String contractBin) {
        this.contractBin = contractBin;
    }

    public Integer getIsDestroy() {
        return this.isDestroy;
    }

    public void setIsDestroy(Integer isDestroy) {
        this.isDestroy = isDestroy;
    }

    public String getDestroyHash() {
        return this.destroyHash;
    }

    public void setDestroyHash(String destroyHash) {
        this.destroyHash = destroyHash;
    }

    public Integer getErc20TxQty() {
        return erc20TxQty;
    }

    public void setErc20TxQty(Integer erc20TxQty) {
        this.erc20TxQty = erc20TxQty;
    }

    public Integer getErc721TxQty() {
        return erc721TxQty;
    }

    public void setErc721TxQty(Integer erc721TxQty) {
        this.erc721TxQty = erc721TxQty;
    }

    public boolean isHasErc20() {
        return hasErc20;
    }

    public void setHasErc20(boolean hasErc20) {
        this.hasErc20 = hasErc20;
    }

    public boolean isHasErc721() {
        return hasErc721;
    }

    public void setHasErc721(boolean hasErc721) {
        this.hasErc721 = hasErc721;
    }

    public boolean isHasErc1155() {
        return hasErc1155;
    }

    public void setHasErc1155(boolean hasErc1155) {
        this.hasErc1155 = hasErc1155;
    }

    public Integer getErc1155TxQty() {
        return erc1155TxQty;
    }

    public void setErc1155TxQty(Integer erc1155TxQty) {
        this.erc1155TxQty = erc1155TxQty;
    }

    public String getTokenSymbol() {
        return tokenSymbol;
    }

    public void setTokenSymbol(String tokenSymbol) {
        this.tokenSymbol = tokenSymbol;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public List<LockDelegate> getLockDelegateList() {
        return lockDelegateList;
    }

    public void setLockDelegateList(List<LockDelegate> lockDelegateList) {
        this.lockDelegateList = lockDelegateList;
    }

    public String getUnLockBalance() {
        return unLockBalance;
    }

    public void setUnLockBalance(String unLockBalance) {
        this.unLockBalance = unLockBalance;
    }

    public String getLockBalance() {
        return lockBalance;
    }

    public void setLockBalance(String lockBalance) {
        this.lockBalance = lockBalance;
    }

    public Integer getInternalTransferQty() {
        return internalTransferQty;
    }

    public void setInternalTransferQty(Integer internalTransferQty) {
        this.internalTransferQty = internalTransferQty;
    }

}
