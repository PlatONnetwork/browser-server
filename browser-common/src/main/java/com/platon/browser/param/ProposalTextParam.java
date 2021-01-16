package com.platon.browser.param;

import com.platon.browser.utils.HexUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * User: dongqile
 * Date: 2019/8/6
 * Time: 15:02
 * tyType=2000提交文本提案(创建提案)
 */
@Data
@Builder
@AllArgsConstructor
@Accessors(chain = true)
public class ProposalTextParam extends TxParam {

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
     * 节点名称
     */
    private String nodeName;
}
