package com.platon.browser.param;

import lombok.Data;

/**
 * User: dongqile
 * Date: 2019/8/21
 * Time: 10:37
 */
@Data
public class CancelProposalParam {

    /**
     * 提交提案的验证人
     */
    private String verifier;

    /**
     * 提案pIDID
     */
    private String pIDID;

    /**
     * 提案投票截止块高（EpochSize*N-20，不超过2周的块高
     */
    private Integer endVotingBlock;

    /**
     * 被取消的目标提案
     */
    private String canceledProposalID;


    public void init(String verifier,String pIDID,Integer endVotingBlock,String canceledProposalID){
        this.setVerifier(verifier);
        this.setPIDID(pIDID);
        this.setEndVotingBlock(endVotingBlock);
        this.setCanceledProposalID(canceledProposalID);


    }
}