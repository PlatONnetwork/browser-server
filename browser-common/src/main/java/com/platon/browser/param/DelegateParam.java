package com.platon.browser.param;

import lombok.Data;

/**
 * User: dongqile
 * Date: 2019/8/6
 * Time: 14:58
 * tyType=1004发起委托(委托)
 */
@Data
public class DelegateParam {
    /**
     * 表示使用账户自由金额还是账户的锁仓金额做质押
     * 0: 自由金额
     * 1: 锁仓金额
     */
    private Integer type;

    /**
     * 被质押的节点Id(也叫候选人的节点Id
     */
    private String nodeId;

    /**
     * 委托的金额(按照最小单位算，1LAT = 10**18 von)
     */
    private String amount;

    /**
     * 被质押节点的名称(有长度限制，表示该节点的名称)
     */
    private String nodeName;

    /**
     * 质押交易快高
     */
    private String stakingBlockNum;

    public void init( Integer type,String nodeId,String amount ,String nodeName,String stakingBlockNum ){
        this.setType(type);
        this.setNodeId(nodeId);
        this.setAmount(amount);
        this.setNodeName(nodeName);
        this.setStakingBlockNum(stakingBlockNum);
    }

}
