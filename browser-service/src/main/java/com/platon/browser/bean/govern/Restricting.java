package com.platon.browser.bean.govern;

import lombok.Builder;

import java.math.BigDecimal;

/**
 * @description: 可修改锁仓配置
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-25 18:30:11
 **/
@Builder
public class Restricting {
    private BigDecimal minimumRelease;

	public BigDecimal getMinimumRelease() {
		return minimumRelease;
	}

	public void setMinimumRelease(BigDecimal minimumRelease) {
		this.minimumRelease = minimumRelease;
	}
}
