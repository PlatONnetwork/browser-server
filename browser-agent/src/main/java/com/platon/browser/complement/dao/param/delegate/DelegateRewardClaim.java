package com.platon.browser.complement.dao.param.delegate;

import com.platon.browser.common.enums.BusinessType;
import com.platon.browser.complement.dao.param.BusinessParam;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.math.BigInteger;


/**
 * // TODO: 定义领取奖励业务入库参数
 * @Auther: chendongming
 * @Date: 2020/01/2
 * @Description: 领取奖励
 */
@Data
@Builder
@Accessors(chain = true)
public class DelegateRewardClaim implements BusinessParam {
    //节点id
    private String nodeId;
    //委托金额
    private BigDecimal amount;
    //委托交易块高
    private BigInteger blockNumber;
    //交易发送方
    private String txFrom;
    //交易序号
    private BigInteger sequence;
    //节点质押快高
    private BigInteger stakingBlockNumber;

    @Override
    public BusinessType getBusinessType() {
        return BusinessType.CLAIM_REWARD;
    }
}
