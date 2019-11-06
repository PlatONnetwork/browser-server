package com.platon.browser.common.complement.dto.delegate;

import com.platon.browser.common.complement.dto.BusinessParam;
import com.platon.browser.common.enums.BusinessType;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;


/**
 * @Auther: dongqile
 * @Date: 2019/11/2
 * @Description: 创建委托 入库参数
 */
@Data
@Slf4j
@Builder
@Accessors(chain = true)
public class DelegateCreate extends BusinessParam {

    /**
     * 节点id
     */
    private String nodeId;

    /**
     * 交易hash
     */
    private String txHash;

    /**
     * 委托金额
     */
    private BigDecimal amount;

    /**
     *  委托交易块高
     */
    private BigInteger blockNumber;

    /**
     * 交易发送方
     */
    private String txFrom;

    /**
     * 交易序号
     */
    private BigInteger sequence;

    /**
     * 节点质押快高
     */
    private BigInteger stakingBlockNumber;

    @Override
    public BusinessType getBusinessType() {
        return BusinessType.DELEGATE_CREATE;
    }
}
