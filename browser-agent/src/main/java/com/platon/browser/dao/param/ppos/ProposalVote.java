package com.platon.browser.dao.param.ppos;

import com.platon.browser.dao.param.BusinessParam;
import com.platon.browser.enums.BusinessType;
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
public class ProposalVote implements BusinessParam {
    //节点id
    private String nodeId;
    //提案hash
    private String proposalHash;
    //投票选项
    private int voteOption;
    //交易hash
    private String txHash;
    //区块高度
    private BigInteger bNum;
    //时间
    private Date timestamp;
    //质押名称
    private String stakingName;
    @Override
    public BusinessType getBusinessType() {
        return BusinessType.PROPOSAL_VOTE;
    }
}
