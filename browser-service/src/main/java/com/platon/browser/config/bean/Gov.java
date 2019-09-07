
package com.platon.browser.config.bean;

import lombok.Data;

@Data
public class Gov {
    private int VersionProposalVote_ConsensusRounds;
    private int VersionProposalActive_ConsensusRounds;
    private double VersionProposal_SupportRate;
    private int TextProposalVote_ConsensusRounds;
    private double TextProposal_VoteRate;
    private double TextProposal_SupportRate;
    private double CancelProposal_VoteRate;
    private double CancelProposal_SupportRate;
}
