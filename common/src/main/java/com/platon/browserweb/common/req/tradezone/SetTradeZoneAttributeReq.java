package com.platon.browserweb.common.req.tradezone;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

public class SetTradeZoneAttributeReq {

	@NotBlank(message = "币对不能为空")
	private String symbol;

	@NotBlank(message = "最小交易单位不能为空")
	private String lotSize;

	@NotBlank(message = "最小价格单位不能为空")
	private String tickSize;

	@NotNull(message = "冷静期时长不能为空")
	private Integer quoteDuration;

	@NotBlank(message = "价格波动刻度不能为空")
	private String quoteLimit;

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getLotSize() {
		return lotSize;
	}

	public void setLotSize(String lotSize) {
		this.lotSize = lotSize;
	}

	public String getTickSize() {
		return tickSize;
	}

	public void setTickSize(String tickSize) {
		this.tickSize = tickSize;
	}

	public Integer getQuoteDuration() {
		return quoteDuration;
	}

	public void setQuoteDuration(Integer quoteDuration) {
		this.quoteDuration = quoteDuration;
	}

	public String getQuoteLimit() {
		return quoteLimit;
	}

	public void setQuoteLimit(String quoteLimit) {
		this.quoteLimit = quoteLimit;
	}
}
