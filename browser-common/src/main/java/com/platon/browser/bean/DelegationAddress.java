package com.platon.browser.bean;

import java.math.BigDecimal;


import lombok.Data;

/**
 * 地址委托返回对象
 *  @file DelegationAddress.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年11月12日
 */
@Data
public class DelegationAddress {

	private String nodeId;
	
	private String nodeName;
	
	private BigDecimal delegateHes;
	
	private BigDecimal delegateLocked;
	
	private BigDecimal delegateReleased;
	
}
