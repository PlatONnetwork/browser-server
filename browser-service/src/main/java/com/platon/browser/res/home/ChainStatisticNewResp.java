package com.platon.browser.res.home;

import lombok.Data;

@Data
public class ChainStatisticNewResp {
	private Long currentNumber; // 当前区块高度
	private String nodeId; // 出块节点id
	private String nodeName; // 出块节点名称
	private Long txQty; // 总的交易数
	private Integer currentTps; // 当前的TPS
	private Integer maxTps; // 最大交易TPS
	private String turnValue; // 当前流通量
	private String issueValue; // 当前发行量
	private String takingDelegationValue; // 当前质押总数=有效的质押+委托
	private String addressQty; // 地址数
	private String proposalQty; // 总提案数
	private String doingProposalQty; // 进行中提案数
}
