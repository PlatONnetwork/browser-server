package com.platon.browser.v0160.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecoveredDelegation {

    /**
     * 地址
     */
    private String address;
    /**
     * 金额
     */
    private BigInteger delegationAmount;

}
