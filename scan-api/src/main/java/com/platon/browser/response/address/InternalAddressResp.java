package com.platon.browser.response.address;

import com.platon.browser.dao.entity.InternalAddress;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class InternalAddressResp {

    /**
     * 基金会账户总余额
     */
    private BigDecimal totalBalance;

    /**
     * 基金会账户总锁仓余额
     */
    private BigDecimal totalRestrictingBalance;

    /**
     * 基金会账户
     */
    List<InternalAddress> InternalAddressBaseResp;

}
