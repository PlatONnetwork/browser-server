package com.platon.browserweb.common.req.userlevel;

public class SetFeeDiscountRateReq {
	/**
	 * 记录id
	 */
	private Long id;
	
	/**
	 * 等级代码
	 */
	private String levelCode;
	
	/**
	 * 提币折扣率 百分比，配置成小数。
如果20%，配置成0.20
	 */
	private String withdraw;
	
	/**
	 * 交易折扣率 百分比，配置成小数。
如果20%，配置成0.20
	 */
	private String trade;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLevelCode() {
		return levelCode;
	}

	public void setLevelCode(String levelCode) {
		this.levelCode = levelCode;
	}

	public String getWithdraw() {
		return withdraw;
	}

	public void setWithdraw(String withdraw) {
		this.withdraw = withdraw;
	}

	public String getTrade() {
		return trade;
	}

	public void setTrade(String trade) {
		this.trade = trade;
	}
	
}
