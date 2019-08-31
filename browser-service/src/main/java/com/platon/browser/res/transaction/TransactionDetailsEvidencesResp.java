package com.platon.browser.res.transaction;

import lombok.Data;

/**
 * 交易详情举报子结构体返回对象
 *  @file TransactionDetailsEvidencesResp.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
@Data
public class TransactionDetailsEvidencesResp {

	private String verify;        //节点id
	private String nodeName;       //被举报的节点名称
}
