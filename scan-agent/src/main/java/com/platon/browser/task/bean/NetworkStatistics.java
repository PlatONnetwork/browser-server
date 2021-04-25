package com.platon.browser.task.bean;

import java.math.BigDecimal;

public class NetworkStatistics {
	private BigDecimal totalValue;
	private BigDecimal stakingValue;

	public BigDecimal getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(BigDecimal totalValue) {
		this.totalValue = totalValue;
	}

	public BigDecimal getStakingValue() {
		return stakingValue;
	}

	public void setStakingValue(BigDecimal stakingValue) {
		this.stakingValue = stakingValue;
	}
}
