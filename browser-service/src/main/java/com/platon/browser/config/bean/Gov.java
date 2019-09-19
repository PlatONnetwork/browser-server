
package com.platon.browser.config.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Gov {
    //
    @JSONField(name = "VersionProposalVote_DurationSeconds")
    private BigDecimal versionProposalVoteDurationSeconds;
    // 升级提案通过率
    @JSONField(name = "VersionProposal_SupportRate")
    private BigDecimal versionProposalSupportRate;
    //
    @JSONField(name = "TextProposalVote_DurationSeconds")
    private BigDecimal textProposalVoteDurationSeconds;
    // 文本提案参与率
    @JSONField(name = "TextProposal_VoteRate")
    private BigDecimal textProposalVoteRate;
    // 文本提案支持率
    @JSONField(name = "TextProposal_SupportRate")
    private BigDecimal textProposalSupportRate;
    // 取消提案参与率
    @JSONField(name = "CancelProposal_VoteRate")
    private BigDecimal cancelProposalVoteRate;
    // 取消提案支持率
    @JSONField(name = "CancelProposal_SupportRate")
    private BigDecimal cancelProposalSupportRate;
}
