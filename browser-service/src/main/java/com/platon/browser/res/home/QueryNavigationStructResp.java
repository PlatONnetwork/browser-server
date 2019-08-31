package com.platon.browser.res.home;

import lombok.Data;

/**
 * 首页返回查询对象子结构体对象
 *  @file QueryNavigationStructResp.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
@Data
public class QueryNavigationStructResp {
	private Long number; // 区块高度
	private String txHash; // 交易hash
	private String address; // 地址
	private String nodeId; // 节点地址

}
