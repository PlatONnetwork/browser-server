package com.platon.browser.elasticsearch.service;

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
	public enum Operator {  
        TERM, TERMS, RANGE, FUZZY, QUERY_STRING, MISSING
    }
	
//	/**
//	 * match枚举
//	 */
//	public enum MatchMode {  
//		START, END, ANYWHERE
//	}  
//	
//	/**
//	 * 算法枚举
//	 */
//	public enum Projection {
//		MAX, MIN, AVG, LENGTH, SUM, COUNT
//	}

	public List<QueryBuilder> listBuilders();
}

