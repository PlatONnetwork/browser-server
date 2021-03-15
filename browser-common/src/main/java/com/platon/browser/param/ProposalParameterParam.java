package com.platon.browser.param;

import com.platon.browser.utils.HexUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * User: chendongming
 * Date: 2019/11/25
 * Time: 11:42
 * txType=2002参数提案(创建提案)
 */
@Data
@Builder
@AllArgsConstructor
@Accessors(chain = true)
public class ProposalParameterParam extends TxParam {
    /**
     * 提交提案的验证人
     */
    private String verifier;
    public void setVerifier(String verifier){
        this.verifier= HexUtil.prefix(verifier);
    }
    /**
     * 提案在pIDID
     */
    private String pIDID;
    /**
     * 参数模块
     */
    private String module;
    /**
     * 参数名称
     */
    private String name;
    /**
     * 参数新值
     */
    private String newValue;
    /**
     * 节点名称
     */
    private String nodeName;
}
