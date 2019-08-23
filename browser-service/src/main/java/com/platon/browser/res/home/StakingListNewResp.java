package com.platon.browser.res.home;

import java.util.List;

import lombok.Data;

@Data
public class StakingListNewResp {
	private Boolean isRefresh;
	private List<StakingListResp> dataList;
}
