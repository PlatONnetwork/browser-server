package com.platon.browser.param;

import lombok.Data;

import java.lang.management.GarbageCollectorMXBean;

/**
 * User: dongqile
 * Date: 2019/8/6
 * Time: 15:10
 * txType=2002提交参数提案(创建提案)
 */
@Data
public class CreateProposalParameterParam {

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
     * 提案投票截止块高（EpochSize*N-20，不超过2周的块高
     */
    private Integer endVotingBlock;

    /**
     * 参数名称
     */
    private String paramName;

    /**
     * 当前值
     */
    private String currentValue;

    /**
     * 新的值
     */
    private String newVersion;

    public void init(String verifier,String githubID,String url,Integer endVotingBlock,String paramName,String currentValue,String newVersion){
        this.setVerifier(verifier);
        this.setGithubID(githubID);
        this.setUrl(url);
        this.setEndVotingBlock(endVotingBlock);
        this.setParamName(paramName);
        this.setCurrentValue(currentValue);
        this.setNewVersion(newVersion);

    }
}
