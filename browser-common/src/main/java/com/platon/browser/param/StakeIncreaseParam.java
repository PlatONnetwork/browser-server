package com.platon.browser.param;

import com.platon.browser.utils.HexUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * User: dongqile
 * Date: 2019/8/6
 * Time: 14:54
 * txType=1002增持质押(增加自有质押)
 */
@Data
@Builder
@AllArgsConstructor
@Accessors(chain = true)
public class StakeIncreaseParam extends TxParam{
    /**
     * 表示使用账户自由金额还是账户的锁仓金额做质押
     * 0: 自由金额
     * 1: 锁仓金额
     */
    private Integer type;

    /**
     * 被质押的节点Id(也叫候选人的节点Id)
     */
    private String nodeId;
    public void setNodeId(String nodeId){
        this.nodeId= HexUtil.prefix(nodeId);
    }

    /**
     * 质押的von
     */
    private BigDecimal amount;

    /**
     * 被质押节点的名称(有长度限制，表示该节点的名称)
     */
    private String nodeName;

    /**
     * 质押交易快高
     */
    private BigInteger stakingBlockNum;
}
