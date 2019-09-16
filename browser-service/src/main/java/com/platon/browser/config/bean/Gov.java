
package com.platon.browser.config.bean;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Gov {
    //
    private BigDecimal VersionProposalVote_DurationSeconds;
    // 升级提案通过率
    private BigDecimal VersionProposal_SupportRate;
    //
    private BigDecimal TextProposalVote_DurationSeconds;
    // 文本提案参与率
    private BigDecimal TextProposal_VoteRate;
    // 文本提案支持率
    private BigDecimal TextProposal_SupportRate;
    // 取消提案参与率
    private BigDecimal CancelProposal_VoteRate;
    // 取消提案支持率
    private BigDecimal CancelProposal_SupportRate;
}
