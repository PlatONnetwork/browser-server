package com.platon.browser.res.proposal;

import lombok.Data;

/**
 *  投票列表返回对象
 *  @file VoteListResp.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
@Data
public class VoteListResp {
	private String voterName;          //投票验证人名称
	private String voter;            //投票验证人
	private String option;            //投票选型  1：支持；  2：反对；  3弃权
	private String txHash;       //投票的hash
	private Long timestamp;         //投票的时间
}
