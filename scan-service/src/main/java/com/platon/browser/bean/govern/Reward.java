package com.platon.browser.bean.govern;

import lombok.Builder;

import java.math.BigDecimal;

/**
 * @description: 可修改收益配置
 **/
@Builder
public class Reward {
    private BigDecimal increaseIssuanceRatio;

	public BigDecimal getIncreaseIssuanceRatio() {
		return increaseIssuanceRatio;
	}

	public void setIncreaseIssuanceRatio(BigDecimal increaseIssuanceRatio) {
		this.increaseIssuanceRatio = increaseIssuanceRatio;
	}
    
}
