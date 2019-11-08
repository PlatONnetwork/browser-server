package com.platon.browser.common.complement.param.slash;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import com.platon.browser.common.complement.param.BusinessParam;
import com.platon.browser.common.enums.BusinessType;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
public class Report extends BusinessParam {
    /**
     * 举报证据
     */
    private String slashData;

    /**
     * 节点Id
     */
    private String nodeId;

    /**
     * 交易hash
     */
    private String txHash;

    /**
     * 交易块高
     */
    private BigInteger bNum;

    /**
     * 时间
     */
    private Date time;

    /**
     * 通过（block_number/每个结算周期出块数）向上取整
     */
    private int settingEpoch;

    /**
     * 质押交易所在块高
     */
    private BigInteger stakingBlockNum;

    /**
     * 双签惩罚比例
     */
    private BigDecimal slashRate;
    
    /**
     * 惩罚金分配给举报人比例
     */
    private BigDecimal slash2ReportRate;

    /**
     *  交易发送者
     */
    private String benefitAddr;
    
    

    /**
     * 当前锁定的
     */
    private BigDecimal codeCurStakingLocked;

    /**
     * 奖励的金额
     */
    private BigDecimal codeRewardValue;

    /**
     * 节点操作描述  'PERCENT|AMOUNT' 中
     */
    private String codeNodeOptDesc;

    /**
     * 节点状态
     */
    private int codeStatus;

    @Override
    public BusinessType getBusinessType() {
        return BusinessType.REPORT;
    }
}
