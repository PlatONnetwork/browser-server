package com.platon.browser.param;

import com.platon.browser.utils.HexUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * User: dongqile
 * Date: 2019/8/6
 * Time: 15:13
 * txType=2003给提案投票(提案投票)
 */
@Data
@Builder
@AllArgsConstructor
@Accessors(chain = true)
public class ProposalVoteParam extends TxParam{
    /**
     * 投票的验证人
     */
    private String verifier;
    public void setVerifier(String verifier){
        this.verifier= HexUtil.prefix(verifier);
    }
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

    /**
     * 提案URL
     */
    private String url;
}
