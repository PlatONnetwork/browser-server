package com.platon.browser.bean;


import lombok.Data;

import java.math.BigInteger;

@Data
public class ValidatorEx {
    private String nodeId;

    private BigInteger validatorId;

    private byte[] publicKeyHex;

    private String stakingAddress;

    private String benifitAddress;

    private BigInteger rewardPer;

    private BigInteger nextRewardPer;

    private BigInteger rewardPerChangeEpoch;

    private BigInteger stakingTxIndex;

    private BigInteger programVersion;

    private BigInteger status;

    private BigInteger stakingEpoch;

    private BigInteger stakingBlockNum;

    private BigInteger shares;

    private BigInteger released;

    private BigInteger releasedHes;

    private BigInteger restrictingPlan;

    private BigInteger restrictingPlanHes;

    private String externalId;

    private String nodeName;

    private String website;

    private String details;

    private BigInteger validatorTerm;

    private BigInteger delegateEpoch;

    private BigInteger delegateTotal;

    private BigInteger delegateTotalHes;

    private BigInteger delegateRewardTotal;
}
