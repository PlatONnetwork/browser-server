package com.platon.browser.response.api;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.platon.browser.config.json.CustomLowLatSerializer;
import lombok.Data;

/** 首页查询返回对象 */

@Data
public class StakingStatisticsResp {
	/**
	 * 质押率 88.92%
	 */
	private String stakingRate;
	/**
	 * 质押委托总金额
	 */
	@JsonSerialize(using = CustomLowLatSerializer.class)
	private String stakingDelegationValue;
	/**
	 * 验证人数量
	 */
	private Integer validatorsNumber;
	/**
	 * 委托人的平均年收益 88.92%
	 */
	private String aprForDelegators;
	/**
	 * 验证者的平均年收益 88.92%
	 */
	private String aprForValidators;
}
