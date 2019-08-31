package com.platon.browser.res.home;

import lombok.Data;

/**
 * 首页区块列表返回对象
 *  @file BlockListNewResp.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
@Data
public class BlockListNewResp {
	private Long number; // 区块高度
	private Long timestamp; // 出块时间
	private Long serverTime; // 服务器时间
	private String nodeId; // 出块节点id
	private String nodeName; // 出块节点名称
	private Integer statTxQty; // 交易数

}
