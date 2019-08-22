package com.platon.browser.dto.transaction;

import java.util.List;

import com.platon.browser.dto.RespPage;
import com.platon.browser.redis.dto.TransactionRedis;

public class TransactionCacheDto {
	
	public TransactionCacheDto(List<TransactionRedis> transactionRedisList,RespPage<?> page) {
		this.transactionRedisList = transactionRedisList;
		this.page = page;
	}
	private List<TransactionRedis> transactionRedisList;
	
	private RespPage<?> page;

	public List<TransactionRedis> getTransactionRedisList() {
		return transactionRedisList;
	}

	public void setTransactionRedisList(List<TransactionRedis> transactionRedisList) {
		this.transactionRedisList = transactionRedisList;
	}

	public RespPage<?> getPage() {
		return page;
	}

	public void setPage(RespPage<?> page) {
		this.page = page;
	}
	
	
	
}
