package com.platon.browser.res.home;

import lombok.Data;

@Data
public class QueryNavigationStructResp {
	private Long number; // 区块高度
	private String txHash; // 交易hash
	private String address; // 地址
	private String nodeId; // 节点地址

}
