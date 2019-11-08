package com.platon.browser.common.complement.param.restricting;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Builder
@Accessors(chain = true)
public class RestrictingItem {
    private String address;
    private Long epoch;
    private BigDecimal amount;
    private BigInteger number;
}
