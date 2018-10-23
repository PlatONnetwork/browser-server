package com.platon.browserweb.common.req.risk;

public class SetDepositRiskReq {
	/**
	 * 记录id
	 */
	private Long id;
	
    /**
     * 币种代码
     */
    private String currency;

    /**
     * 每笔交易最小阈值
     */
    private String minQty;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getMinQty() {
		return minQty;
	}

	public void setMinQty(String minQty) {
		this.minQty = minQty;
	}
    
}
