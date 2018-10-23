package com.platon.browserweb.common.req.risk;

public class SetWithdrawRiskReq {
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

    /**
     * 每笔交易最大阈值
     */
    private String maxQty;

    /**
     * 每天交易最大阈值
     */
    private String dailyQty;

    /**
     * 单次交易人工审核阀值
     */
    private String maxConfirmQty;

    /**
     * 每天交易最大次数
     */
    private Integer maxCount;

	/**
	 * 是否启用
	 */
    private Boolean enable;

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

	public String getMaxQty() {
		return maxQty;
	}

	public void setMaxQty(String maxQty) {
		this.maxQty = maxQty;
	}

	public String getDailyQty() {
		return dailyQty;
	}

	public void setDailyQty(String dailyQty) {
		this.dailyQty = dailyQty;
	}

	public String getMaxConfirmQty() {
		return maxConfirmQty;
	}

	public void setMaxConfirmQty(String maxConfirmQty) {
		this.maxConfirmQty = maxConfirmQty;
	}

	public Integer getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(Integer maxCount) {
		this.maxCount = maxCount;
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}
}
