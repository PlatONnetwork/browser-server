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
 * Time: 14:58
 * tyType=1004发起委托(委托)
 */
@Data
@Builder
@AllArgsConstructor
@Accessors(chain = true)
public class DelegateCreateParam extends TxParam{
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
    public void setNodeId(String nodeId){
        this.nodeId= HexUtil.prefix(nodeId);
    }

    /**
     * 委托的金额(按照最小单位算，1LAT = 10**18 von)
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
