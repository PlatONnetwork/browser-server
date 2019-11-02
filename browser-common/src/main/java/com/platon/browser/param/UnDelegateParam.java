package com.platon.browser.param;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * User: dongqile
 * Date: 2019/8/6
 * Time: 15:00
 * txType=1005减持/撤销委托(赎回委托)
 */
@Data
@Builder
@Accessors(chain = true)
public class UnDelegateParam extends TxParam{

    /**
     * 代表着某个node的某次质押的唯一标示
     */
    private Long stakingBlockNum;

    /**
     * 被质押的节点Id(也叫候选人的节点Id)
     */
    private String nodeId;

    /**
     * 减持委托的金额(按照最小单位算，1LAT = 10**18 von)
     */
    private String amount;

    /**
     * 被质押节点的名称(有长度限制，表示该节点的名称)
     */
    private String nodeName;
    /********把字符串类数值转换为大浮点数的便捷方法********/
    public BigDecimal decimalAmount(){return StringUtils.isBlank(amount)?BigDecimal.ZERO:new BigDecimal(amount);}
    /********把字符串类数值转换为大整数的便捷方法********/
    public BigInteger integerAmount(){return StringUtils.isBlank(amount)?BigInteger.ZERO:new BigInteger(amount);}

}
