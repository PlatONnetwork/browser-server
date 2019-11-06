package com.platon.browser.common.complement.dto.proposal;

import com.platon.browser.common.complement.dto.BusinessParam;
import com.platon.browser.common.enums.BusinessType;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.util.Date;

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
     * 生效块高
     */
    private BigInteger activeBlock;

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
     * version
     */
    private String newVersion;
    @Override
    public BusinessType getBusinessType() {
        return BusinessType.PROPOSAL_UPGRADE;
    }
}
