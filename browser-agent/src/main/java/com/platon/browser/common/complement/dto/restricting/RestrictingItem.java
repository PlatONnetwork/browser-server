package com.platon.browser.common.complement.dto.restricting;

import java.math.BigDecimal;

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
    private Long number;
}
