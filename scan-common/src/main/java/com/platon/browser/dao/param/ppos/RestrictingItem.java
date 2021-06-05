package com.platon.browser.dao.param.ppos;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.math.BigInteger;


@Data
@Builder
@Accessors(chain = true)
public class RestrictingItem {
    private String address;
    private BigInteger epoch;
    private BigDecimal amount;
    private BigInteger number;
}
