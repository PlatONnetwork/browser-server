package com.platon.browser.param;

import com.platon.browser.utils.HexUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * User: dongqile
 * Date: 2019/8/6
 * Time: 15:00
 * txType=1005减持/撤销委托(赎回委托)
 */
@Data
@Accessors(chain = true)
public class DelegateExitParam extends TxParam {

    /**
     * 代表着某个node的某次质押的唯一标示
     */
    private BigInteger stakingBlockNum;

    /**
     * 被质押的节点Id(也叫候选人的节点Id)
     */
    private String nodeId;

    public DelegateExitParam setNodeId(String nodeId) {
        this.nodeId = HexUtil.prefix(nodeId);
        return this;
    }

    /**
     * 减持委托的金额(按照最小单位算，1LAT = 10**18 von)
     */
    private BigDecimal amount;

    /**
     * 真正减持的委托金额(按照最小单位算，1LAT = 10**18 von)
     */
    private BigDecimal realAmount;

    /**
     * 委托金额对应的奖励数(按照最小单位算，1LAT = 10**18 von)
     */
    private BigDecimal reward;

    /**
     * 被质押节点的名称(有长度限制，表示该节点的名称)
     */
    private String nodeName;

    /**
     * 委托的收益(委托锁定新增字段)
     */
    private BigDecimal delegateIncome;

    /**
     * 撤销的委托金退回用户余额(委托锁定新增字段)
     */
    private BigDecimal released;

    /**
     * 撤销的委托金退回用户锁仓账户(委托锁定新增字段)
     */
    private BigDecimal restrictingPlan;

    /**
     * 撤销的委托金转到锁定期,来自余额(委托锁定新增字段)
     */
    private BigDecimal lockReleased;

    /**
     * 撤销的委托金转到锁定期,来自锁仓账户(委托锁定新增字段)
     */
    private BigDecimal lockRestrictingPlan;

}
