package com.platon.browser.param;

import lombok.Data;

/**
 * User: dongqile
 * Date: 2019/8/6
 * Time: 14:56
 * tyType=1003撤销质押(退出验证人)
 */
@Data
public class ExitValidatorParam {
    /**
     * 被质押的节点Id(也叫候选人的节点Id)
     */
    private String nodeId;

    /**
     * 被质押节点的名称(有长度限制，表示该节点的名称)
     */
    private String nodeName;

    /**
     * 质押交易快高
     */
    private String stakingBlockNum;

    /**
     *  撤销金额
     */
    private String amount;

    public void init(String nodeId,String nodeName,String stakingBlockNum){
        this.setNodeId(nodeId);
        this.setNodeName(nodeName);
        this.setStakingBlockNum(stakingBlockNum);
    }
}
