package com.platon.browser.res.proposal;

import lombok.Data;

@Data
public class VoteListResp {
	private String nodeName;          //投票验证人的节点名称
	private String nodeId;            //投票验证人的节点id
	private String option;            //投票选型  1：支持；  2：反对；  3弃权
	private String txHash;       //投票的hash
	private Long timestamp;         //投票的时间
}
