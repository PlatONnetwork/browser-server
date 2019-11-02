package com.platon.browser.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * User: dongqile
 * Date: 2019/8/6
 * Time: 14:43
 * txType=1000,发起质押(创建验证人)
 */
@Data
@Builder
@AllArgsConstructor
@Accessors(chain = true)
public class StakeCreateParam extends TxParam{

    /**
     * 表示使用账户自由金额还是账户的锁仓金额做质押
     * 0: 自由金额
     * 1: 锁仓金额
     */
    private Integer type;

    /**
     * 用于接受出块奖励和质押奖励的收益账户
     */
    private String benefitAddress;

    /**
     * 被质押的节点Id(也叫候选人的节点Id)
     */
    private String nodeId;

    /**
     * 外部Id(有长度限制，给第三方拉取节点描述的Id)
     */
    private String externalId;

    /**
     * 被质押节点的名称(有长度限制，表示该节点的名称)
     */
    private String nodeName;

    /**
     * 节点的第三方主页(有长度限制，表示该节点的主页)
     */
    private String website;

    /**
     * 节点的描述(有长度限制，表示该节点的描述)
     */
    private String details;

    /**
     * 质押的von
     */
    private String amount;

    /**
     * 程序的真实版本，治理rpc获取
     */
    private BigInteger programVersion;

    /**
     * blockNumber
     */
    private Long blockNumber;

    /********把字符串类数值转换为大浮点数的便捷方法********/
    public BigDecimal decimalAmount(){return StringUtils.isBlank(amount)?BigDecimal.ZERO:new BigDecimal(amount);}
    /********把字符串类数值转换为大整数的便捷方法********/
    public BigInteger integerAmount(){return StringUtils.isBlank(amount)?BigInteger.ZERO:new BigInteger(amount);}
}
