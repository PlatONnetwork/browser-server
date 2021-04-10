package com.platon.browser.response.transaction;

/**
 * 交易详情举报子结构体返回对象
 *  @file TransactionDetailsEvidencesResp.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class TransactionDetailsEvidencesResp {

	private String verify;        //节点id
	private String nodeName;       //被举报的节点名称
	public String getVerify() {
		return verify;
	}
	public void setVerify(String verify) {
		this.verify = verify;
	}
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
}
