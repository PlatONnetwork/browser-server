package com.platon.browser.common.complement.dto.proposal;

import java.math.BigInteger;
import java.util.Date;

import com.platon.browser.common.complement.dto.BusinessParam;
import com.platon.browser.common.enums.BusinessType;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Builder
@Accessors(chain = true)
public class ProposalUpgradeOrCancel extends BusinessParam {

    /**
     * 节点id
     */
    private String nodeId;

    /**
     * pIDID替换
     */
    private String pIDID;

    /**
     * url
     */
    private String url;

    /**
     * PIP-{pip_id}
     */
    private String pipNum;

    /**
     * 投票结束快高
     */
    private BigInteger endVotingBlock;
    
    /**
     * 提案主题
     */
    private String topic;

    /**
     * 提案描述
     */
    private String description;

    /**
     * 操作描述
     */
    private String optDesc;

    /**
     * 被取消提案id
     */
    private String canceledId;

    /**
     * 交易hash
     */
    private String txHash;


    /**
     * 区块高度
     */
    private BigInteger blockNumber;

    /**
     * 时间
     */
    private Date timestamp;

    /**
     * 质押名称
     */
    private String stakingName;


    /**
     * 质押名称
     */
    private BigInteger activeBlock;
    @Override
    public BusinessType getBusinessType() {
        return BusinessType.PROPOSAL_CANCEL;
    }
}
