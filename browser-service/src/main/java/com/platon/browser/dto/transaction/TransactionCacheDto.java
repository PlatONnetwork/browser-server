package com.platon.browser.dto.transaction;

import java.util.List;

import com.platon.browser.dao.entity.Transaction;
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
	public TransactionCacheDto(List<Transaction> transactionList,RespPage<?> page) {
		this.transactionList = transactionList;
		this.page = page;
	}
	private List<Transaction> transactionList;
	
	private RespPage<?> page;

	public List<Transaction> getTransactionList() {
		return transactionList;
	}

	public void setTransactionList(List<Transaction> transactionList) {
		this.transactionList = transactionList;
	}

	public RespPage<?> getPage() {
		return page;
	}

	public void setPage(RespPage<?> page) {
		this.page = page;
	}
	
	
	
}
