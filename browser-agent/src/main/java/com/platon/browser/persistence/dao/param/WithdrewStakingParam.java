package com.platon.browser.persistence.dao.param;

import lombok.Data;

import java.math.BigInteger;
import java.util.Date;

/**
 * @Auther: dongqile
 * @Date: 2019/11/1
 * @Description:
 */
@Data
public class WithdrewStakingParam {

    /**
     * 节点id
     */
    private String nodeId;

    /**
     * 交易hash
     */
    private String txHash;

    /**
     * 质押交易所在块高
     */
    private BigInteger stakingBlockNum;

    /**
     * 时间
     */
    private Date time;

    /**
     * 交易块高
     */
    private BigInteger bNum;

    /**
     * 结算周期标识
     */
    private int stakingReductionEpoch;
}