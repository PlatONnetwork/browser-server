package com.platon.browser.dao.param.ppos;

import com.platon.browser.dao.param.BusinessParam;
import com.platon.browser.enums.BusinessType;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.math.BigInteger;


/**
 * @Auther: dongqile
 * @Date: 2019/11/2
 * @Description: 创建委托 入库参数
 */
@Data
@Builder
@Accessors(chain = true)
public class DelegateCreate implements BusinessParam {
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
        return BusinessType.DELEGATE_CREATE;
    }
}
