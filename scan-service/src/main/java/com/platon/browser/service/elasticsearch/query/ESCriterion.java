package com.platon.browser.service.elasticsearch.query;

import org.elasticsearch.index.query.QueryBuilder;

import java.util.List;

/**
 * es查询接口
 *  @file ESCriterion.java
 *  @description
 *	@author zhangrj
 *  @data 2019年10月31日
 */
public interface ESCriterion {
	/**
	 * 操作枚举
	 */
	enum Operator {
        TERM, TERMS, RANGE, FUZZY, QUERY_STRING, MISSING
    }

	List<QueryBuilder> listBuilders();
}

