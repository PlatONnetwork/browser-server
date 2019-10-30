package com.platon.browser.service;

import com.platon.browser.dao.entity.Transaction;

import java.util.Set;

/**
 *  交易缓存数据处理接口
 *  @file TransactionCacheService.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public interface TransactionCacheService {
	/**
	 * 清空交易缓存数据信息
	 * @method clear
	 */
    void clear();
    /**
     * 更新交易数据缓存信息
     * @method update
     * @param items
     */
    void update(Set<Transaction> items);
    /**
     * 重置交易数据缓存信息
     * @method reset
     * @param clearOld
     */
    void reset(boolean clearOld);
}
