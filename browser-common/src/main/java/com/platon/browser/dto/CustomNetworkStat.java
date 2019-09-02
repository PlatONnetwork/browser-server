package com.platon.browser.dto;

import com.platon.browser.dao.entity.NetworkStat;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * User: dongqile
 * Date: 2019/8/20
 * Time: 11:13
 */
public class CustomNetworkStat extends NetworkStat {

    public CustomNetworkStat () {
        this.setId(1); // 只有一条数据，默认ID为1
        this.setCurrentNumber(0L);
        this.setNodeName("");
        this.setNodeId("");
        this.setTxQty(0);
        this.setCurrentTps(0);
        this.setMaxTps(0);
        this.setIssueValue("0");
        this.setTurnValue("0");
        this.setStakingDelegationValue("0");
        this.setStakingValue("0");
        this.setProposalQty(0);
        this.setDoingProposalQty(0);
        this.setAddressQty(0);
        this.setBlockReward("0");
        this.setStakingReward("0");
        this.setAddIssueBegin(0L);
        this.setAddIssueEnd(0L);
        this.setNextSetting(0L);
    }

    /********把字符串类数值转换为大浮点数的便捷方法********/
    public BigDecimal decimalBlockReward(){return new BigDecimal(this.getBlockReward());}
    public BigDecimal decimalIssueValue(){return new BigDecimal(this.getIssueValue());}
    public BigDecimal decimalStakingDelegationValue(){return new BigDecimal(this.getStakingDelegationValue());}
    public BigDecimal decimalStakingReward(){return new BigDecimal(this.getStakingReward());}
    public BigDecimal decimalStakingValue(){return new BigDecimal(this.getStakingValue());}
    public BigDecimal decimalTurnValue(){return new BigDecimal(this.getTurnValue());}
    /********把字符串类数值转换为大整数的便捷方法********/
    public BigInteger integerBlockReward(){return new BigInteger(this.getBlockReward());}
    public BigInteger integerIssueValue(){return new BigInteger(this.getIssueValue());}
    public BigInteger integerStakingDelegationValue(){return new BigInteger(this.getStakingDelegationValue());}
    public BigInteger integerStakingReward(){return new BigInteger(this.getStakingReward());}
    public BigInteger integerStakingValue(){return new BigInteger(this.getStakingValue());}
    public BigInteger integerTurnValue(){return new BigInteger(this.getTurnValue());}

}
