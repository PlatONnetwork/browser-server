package com.platon.browser.enums;

public enum StakingStatusEnum {
	ALL("all", null),//全部
	CANDIDATE("candidate" ,1),//候选中
	ACTIVE("active", 2),//活跃中
	BLOCK("block", 3),//出块中
	ABORTING("aboring",4),//推出中
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

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}
	
	public static Integer getCodeByStatus(Integer status, Integer isConsensus) {
		if(StakingStatus.CANDIDATE.getCode().equals(status)) {
			if(IsConsensusStatus.YES.getCode().equals(isConsensus)) {
				return StakingStatusEnum.ACTIVE.getCode();
			}
			return StakingStatusEnum.CANDIDATE.getCode();
		}
		if(StakingStatus.ABORTING.getCode().equals(status)) {
			return StakingStatusEnum.ABORTING.getCode();
		}
		if(StakingStatus.EXITED.getCode().equals(status)) {
			return StakingStatusEnum.EXITED.getCode();
		}
		return null;
	}
	
}
