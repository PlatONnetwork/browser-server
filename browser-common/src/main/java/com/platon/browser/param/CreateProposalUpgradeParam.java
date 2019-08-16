package com.platon.browser.param;

import jnr.ffi.annotations.In;
import lombok.Data;

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
     * 提案在github上的id
     */
    private String githubID;

    /**
     * 提案URL，长度不超过512
     */
    private String url;

    /**
     * 提案投票截止块高（EpochSize*N-20，不超过2周的块高）
     */
    private Integer endVotingBlock;

    /**
     * 升级版本
     */
    private Integer newVersion;

    /**
     * 生效块高（endVotingBlock+ 4*250 ~ endVotingBlock+ 10*250）
     */
    private Integer activeBlock;

    public void init( String verifier, String githubID, String url, Integer endVotingBlock,Integer newVersion,Integer activeBlock ){
        this.setVerifier(verifier);
        this.setGithubID(githubID);
        this.setUrl(url);
        this.setEndVotingBlock(endVotingBlock);
        this.setNewVersion(newVersion);
        this.setActiveBlock(activeBlock);

    }
}
