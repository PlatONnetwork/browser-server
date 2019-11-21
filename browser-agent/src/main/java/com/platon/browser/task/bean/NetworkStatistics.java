package com.platon.browser.task.bean;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
public class NetworkStatistics {
	private BigDecimal totalValue;
	private BigDecimal stakingValue;
}
