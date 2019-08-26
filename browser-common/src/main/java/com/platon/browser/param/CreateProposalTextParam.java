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
     * 节点名称
     */
    private String nodeName;


    public void init(String verifier,String pIDID){
        this.setVerifier(verifier);
        this.setPIDID(pIDID);
    }


}
