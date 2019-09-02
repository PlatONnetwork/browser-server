package com.platon.browser.dto.transaction;

import java.util.List;

import com.platon.browser.dao.entity.TransactionWithBLOBs;
import com.platon.browser.res.RespPage;

/**
 * 交易缓存dto
 *  @file TransactionCacheDto.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class TransactionCacheDto {
	
	/**
	 * 交易构造初始方法
	 * @param transactionRedisList
	 * @param page
	 */
	public TransactionCacheDto(List<TransactionWithBLOBs> transactionList,RespPage<?> page) {
		this.transactionList = transactionList;
		this.page = page;
	}
	private List<TransactionWithBLOBs> transactionList;
	
	private RespPage<?> page;

	public List<TransactionWithBLOBs> getTransactionList() {
		return transactionList;
	}

	public void setTransactionList(List<TransactionWithBLOBs> transactionList) {
		this.transactionList = transactionList;
	}

	public RespPage<?> getPage() {
		return page;
	}

	public void setPage(RespPage<?> page) {
		this.page = page;
	}
	
	
	
}
