package com.platon.browser.dto;

import java.math.BigDecimal;

import com.platon.browser.dao.entity.Delegation;

import lombok.Data;

@Data
public class DelegationStaking extends Delegation{

	private BigDecimal statDelegateHes;
	
	private BigDecimal statDelegateLocked;
	
	private Integer status;
	
	private String nodeName;
	
	private BigDecimal statDelegateReleased;
	
	private BigDecimal allDelegate;
	
	private BigDecimal allLockDelegate;
}
