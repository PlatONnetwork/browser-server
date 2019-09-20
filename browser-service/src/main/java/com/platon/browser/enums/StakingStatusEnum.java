package com.platon.browser.enums;

import com.platon.browser.dto.CustomStaking;

/**
 *  返回前端验证人状态枚举
 *  @file StakingStatusEnum.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public enum StakingStatusEnum {
	ALL("all", null),//全部
	CANDIDATE("candidate" ,1),//候选中
	ACTIVE("active", 2),//活跃中
	BLOCK("block", 3),//出块中
	EXITING("exiting",4),//退出中
	EXITED("exited",5);//已退出
	
	private String name;
	
	private Integer code;
	
	StakingStatusEnum(String name, Integer code) {
		this.code = code;
		this.name = name;
	}

	public String getName() {
		return name;
	}
	public Integer getCode() {
		return code;
	}
	
	/**
	 * 根据数据状态转换成对应的前端状态
	 * @method getCodeByStatus
	 */
	public static Integer getCodeByStatus(Integer status, Integer isConsensus, Integer isSetting) {
		if(CustomStaking.StatusEnum.CANDIDATE.getCode() == status) {
			if(CustomStaking.YesNoEnum.YES.getCode() == isSetting) {
				/** 当状态既为候选中且在结算周期则认为节点为活跃中*/
				return StakingStatusEnum.ACTIVE.getCode();
			}
			return StakingStatusEnum.CANDIDATE.getCode();
		}
		if(CustomStaking.StatusEnum.EXITING.getCode() == status) {
			return StakingStatusEnum.EXITING.getCode();
		}
		if(CustomStaking.StatusEnum.EXITED.getCode() == status) {
			return StakingStatusEnum.EXITED.getCode();
		}
		return null;
	}
	
}
