package com.platon.browserweb.common.req.tradezone;

import com.platon.browserweb.common.base.Dto;

public class ListTradeZoneSetResp extends Dto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4693592859604382784L;
	
    /**
     * 基准币代码
     */
    private String baseCurrency;

    /**
     * 币名称
     */
    private String baseCurrencyName;
    
    /**
     * 货币对
     */
    private String symbol;
    
    /**
     * 图标 base64
     */
    private String baseCurrencyIcon;
    
    /**
     * 最小交易单位
     */
    private String lotSize;
    
    /**
     * 最小价格单位
     */
    private String tickSize;
    
    /**
     * 冷静期比例
     */
    private String quoteLimit;
    
    /**
     * 冷静期间隔 分钟
     */
    private int quoteDuration;
    
    /**
     * 交易状态
1：交易中
0：未开始交易
     */
    private String status;
    
    /**
     * 是否是erc20代币 
0：不是
1：是
     */
    private boolean baseCurrencyIfErc20;
    
    /**
     * 交易币代码
     */
    private String quoteCurrency;

	/**
	 * 是否启用
	 */
    private Boolean enable;

	public String getBaseCurrency() {
		return baseCurrency;
	}

	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrency = baseCurrency;
	}

	public String getBaseCurrencyName() {
		return baseCurrencyName;
	}

	public void setBaseCurrencyName(String baseCurrencyName) {
		this.baseCurrencyName = baseCurrencyName;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getBaseCurrencyIcon() {
		return baseCurrencyIcon;
	}

	public void setBaseCurrencyIcon(String baseCurrencyIcon) {
		this.baseCurrencyIcon = baseCurrencyIcon;
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

	public String getQuoteLimit() {
		return quoteLimit;
	}

	public void setQuoteLimit(String quoteLimit) {
		this.quoteLimit = quoteLimit;
	}

	public int getQuoteDuration() {
		return quoteDuration;
	}

	public void setQuoteDuration(int quoteDuration) {
		this.quoteDuration = quoteDuration;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isBaseCurrencyIfErc20() {
		return baseCurrencyIfErc20;
	}

	public void setBaseCurrencyIfErc20(boolean baseCurrencyIfErc20) {
		this.baseCurrencyIfErc20 = baseCurrencyIfErc20;
	}

	public String getQuoteCurrency() {
		return quoteCurrency;
	}

	public void setQuoteCurrency(String quoteCurrency) {
		this.quoteCurrency = quoteCurrency;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}
}
