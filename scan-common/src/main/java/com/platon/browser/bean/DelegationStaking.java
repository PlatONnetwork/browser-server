package com.platon.browser.bean;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 地址委托返回数据
 *  @file DelegationStaking.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年11月12日
 */
@Data
public class DelegationStaking {

	private String delegateAddr;
	
	private BigDecimal delegateHes;
	
	private BigDecimal delegateLocked;
	
	private BigDecimal delegateReleased;
	
}
