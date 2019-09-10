
package com.platon.browser.config.bean;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Gov {
    private BigDecimal VersionProposalVote_ConsensusRounds;
    private BigDecimal VersionProposalActive_ConsensusRounds;
    private BigDecimal VersionProposal_SupportRate;
    private BigDecimal TextProposalVote_ConsensusRounds;
    private BigDecimal TextProposal_VoteRate;
    private BigDecimal TextProposal_SupportRate;
    private BigDecimal CancelProposal_VoteRate;
    private BigDecimal CancelProposal_SupportRate;
}
