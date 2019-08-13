package com.platon.browser.res.home;

import lombok.Data;

@Data
public class BlockListNewResp {
	private Long number; // 区块高度
	private Long timestamp; // 出块时间
	private Long serverTime; // 服务器时间
	private String nodeId; // 出块节点id
	private String nodeName; // 出块节点名称
	private Long statTxQty; // 交易数

}
