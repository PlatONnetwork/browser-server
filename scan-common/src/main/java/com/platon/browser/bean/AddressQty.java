package com.platon.browser.bean;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AddressQty {

    /**
     * 地址
     */
    private String address;

    /**
     * 交易总数
     */
    private long txQty;

    /**
     * token对应的交易数
     */
    private long tokenQty;

    /**
     * 转账交易总数
     */
    private long transferQty;

    /**
     * 委托交易总数
     */
    private long delegateQty;

    /**
     * 质押交易总数
     */
    private long stakingQty;

    /**
     * 治理交易总数
     */
    private long proposalQty;

}
