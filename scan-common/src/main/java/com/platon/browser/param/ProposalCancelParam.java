package com.platon.browser.param;

import com.platon.browser.utils.HexUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * User: dongqile
 * Date: 2019/8/21
 * Time: 10:37
 */
@Data
@Builder
@AllArgsConstructor
@Accessors(chain = true)
public class ProposalCancelParam extends TxParam {

    /**
     * 提交提案的验证人
     */
    private String verifier;
    public void setVerifier(String verifier){
        this.verifier= HexUtil.prefix(verifier);
    }

    /**
     * 提案pIDID
     */
    private String pIDID;

    /**
     * 提案投票截止块高（EpochSize*N-20，不超过2周的块高
     */
    private BigDecimal endVotingRound;

    /**
     * 被取消的目标提案
     */
    private String canceledProposalID;

    /**
     * 节点名称
     */
    private String nodeName;
}
