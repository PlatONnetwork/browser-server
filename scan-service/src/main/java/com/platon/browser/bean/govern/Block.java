package com.platon.browser.bean.govern;

import lombok.Builder;

import java.math.BigDecimal;

/**
 * @description: 可修改的区块参数配置
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-25 18:32:13
 **/
@Builder
public class Block {
    private BigDecimal maxBlockGasLimit;

	public BigDecimal getMaxBlockGasLimit() {
		return maxBlockGasLimit;
	}

	public void setMaxBlockGasLimit(BigDecimal maxBlockGasLimit) {
		this.maxBlockGasLimit = maxBlockGasLimit;
	}
    
}
