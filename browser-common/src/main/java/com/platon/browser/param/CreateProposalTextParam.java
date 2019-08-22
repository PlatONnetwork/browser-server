package com.platon.browser.param;

import lombok.Data;

/**
 * User: dongqile
 * Date: 2019/8/6
 * Time: 15:02
 * tyType=2000提交文本提案(创建提案)
 */
@Data
public class CreateProposalTextParam {

    /**
     * 提交提案的验证人
     */
    private String verifier;

    /**
     * 提案pIDID
     */
    private String pIDID;

    /**
     * 提案投票截止块高（EpochSize*N-20，不超过2周的块高）
     */
    private Integer endVotingBlock;

    public void init(String verifier,String pIDID,Integer endVotingBlock){
        this.setVerifier(verifier);
        this.setPIDID(pIDID);
        this.setEndVotingBlock(endVotingBlock);
    }


}
