package com.platon.browser.dto.json;

import lombok.Data;

/**
 * User: dongqile
 * Date: 2019/8/6
 * Time: 15:13
 * txType=2003给提案投票(提案投票)
 */
@Data
public class VotingProposalDto {

    /**
     * 投票的验证人
     */
    private String verifier;

    /**
     * 提案ID
     */
    private String proposalId;

    /**
     * 投票选项
     * 0x01：文本提案
     * 0x02：升级提案
     * 0x03：参数提案
     */
    private String option;

    public void init(String verifier,String proposalId,String option){
        this.setVerifier(verifier);
        this.setProposalId(proposalId);
        this.setOption(option);
    }

}