package com.platon.browser.param;

import lombok.Data;

/**
 * User: dongqile
 * Date: 2019/8/6
 * Time: 15:13
 * txType=2003给提案投票(提案投票)
 */
@Data
public class VotingProposalParam {

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
     * 0x01：支持
     * 0x02：反对
     * 0x03：反对
     */
    private String option;

    /**
     * 节点代码版本
     */
    private String programVersion;

    /**
     * 代码版本签名
     */
    private String versionSign;

    /**
     * 被取消的提案的pidid
     */
    private String pIDID;

    /**
     * 被取消的提案的类型
     */
    private String proposalType;

    /**
     * 投票的验证人节点名称
     */
    private String nodeName;



    public void init(String verifier,String proposalId,String option,String programVersion,String versionSign){
        this.setVerifier(verifier);
        this.setProposalId(proposalId);
        this.setOption(option);
        this.setProgramVersion(programVersion);
        this.setVersionSign(versionSign);
    }

}
