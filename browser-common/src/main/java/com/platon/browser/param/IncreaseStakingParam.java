package com.platon.browser.param;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * User: dongqile
 * Date: 2019/8/6
 * Time: 14:54
 * txType=1002增持质押(增加自有质押)
 */
@Data
public class IncreaseStakingParam {
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

    /**
     * 质押的von
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

    public void init(String nodeId, Integer type,String amount,String stakingBlockNum){
        this.setNodeId(nodeId);
        this.setType(type);
        this.setAmount(amount);
        this.setStakingBlockNum(stakingBlockNum);
    }

    /********把字符串类数值转换为大浮点数的便捷方法********/
    public BigDecimal decimalAmount(){return StringUtils.isBlank(amount)?BigDecimal.ZERO:new BigDecimal(amount);}
    /********把字符串类数值转换为大整数的便捷方法********/
    public BigInteger integerAmount(){return StringUtils.isBlank(amount)?BigInteger.ZERO:new BigInteger(amount);}
}
