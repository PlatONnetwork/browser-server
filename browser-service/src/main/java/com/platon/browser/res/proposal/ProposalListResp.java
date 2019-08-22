package com.platon.browser.res.proposal;

import lombok.Data;

@Data
public class ProposalListResp {
	private String pipNum;
	private String proposalHash; // 提案内部标识
	private String topic;  //提案标题
	private String description;  //提案描述
	private String url; // github地址 https://github.com/ethereum/EIPs/blob/master/EIPS/eip-100.md PIP编号
						// eip-100
	private String type; // 提案类型 1：文本提案； 2：升级提案； 3参数提案。
	private String status; // 状态 1：投票中 2：通过 3：失败 4：预升级 5：升级完成 已通过=2 或4 或 5
	private String curBlock; // 当前块高
	private String endVotingBlock; // 投票结算的快高
	private String newVersion; // 升级提案升级的版本
	private String paramName; // 参数名
	private Long timestamp; // 提案时间
}
