package com.platon.browser.param;

import lombok.Data;

import java.math.BigDecimal;

/**
 * User: dongqile
 * Date: 2019/8/6
 * Time: 15:05
 * txType=2001提交升级提案(创建提案)
 */
@Data
public class CreateProposalUpgradeParam {

    /**
     * 提交提案的验证人
     */
    private String verifier;

    /**
     * 提案在pIDID
     */
    private String pIDID;

    /**
     * 提案投票截止块高（EpochSize*N-20，不超过2周的块高）
     */
    private BigDecimal endVotingRound;

    /**
     * 升级版本
     */
    private Integer newVersion;

    /**
     * 节点名称
     */
    private String nodeName;


    public void init( String verifier, String pIDID, BigDecimal endVotingRound,Integer newVersion){
        this.setVerifier(verifier);
        this.setPIDID(pIDID);
        this.setEndVotingRound(endVotingRound);
        this.setNewVersion(newVersion);
    }
}
