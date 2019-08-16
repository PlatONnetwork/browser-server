package com.platon.browser.res.transaction;

import lombok.Data;

@Data
public class TransactionDetailsEvidencesResp {

	private String verify;        //节点id
	private String nodeName;       //被举报的节点名称
}
