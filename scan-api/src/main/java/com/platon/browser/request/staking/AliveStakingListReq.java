package com.platon.browser.request.staking;

import com.platon.browser.request.PageReq;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 *  活跃验证人列表请求对象
 *  @file AliveStakingListReq.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class AliveStakingListReq extends PageReq{
    private String key;
    @NotBlank(message = "{queryStatus not null}")
    @Pattern(regexp = "all|active|candidate", message = "{queryStatus.illegal}")
    private String queryStatus;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getQueryStatus() {
		return queryStatus;
	}
	public void setQueryStatus(String queryStatus) {
		this.queryStatus = queryStatus;
	}
    
}