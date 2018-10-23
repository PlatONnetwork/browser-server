package com.platon.browserweb.common.req.userlevel;

public class ListRiskDiscountRateReq{

	/**
	 * 业务类型
WITHDRAW：提币
TRADE：交易
	 */
	private String bizCode;

	/**
	 * 代理商ID
	 */
	private Long brokerId;

	public String getBizCode() {
		return bizCode;
	}

	public void setBizCode(String bizCode) {
		this.bizCode = bizCode;
	}

	public Long getBrokerId() {
		return brokerId;
	}

	public void setBrokerId(Long brokerId) {
		this.brokerId = brokerId;
	}
}
