package com.platon.browserweb.common.req.userlevel;

import com.platon.browserweb.common.base.Dto;

public class ListUserLevelResp extends Dto{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1087594998119459370L;

	/**
	 * 等级代码
	 */
	private String code;
	
	/**
	 * 积分说明
	 */
	private String ranking;

	/**
	 * 积分规则
	 */
	private String scoreRule;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getRanking() {
		return ranking;
	}

	public void setRanking(String ranking) {
		this.ranking = ranking;
	}

	public String getScoreRule() {
		return scoreRule;
	}

	public void setScoreRule(String scoreRule) {
		this.scoreRule = scoreRule;
	}
}
