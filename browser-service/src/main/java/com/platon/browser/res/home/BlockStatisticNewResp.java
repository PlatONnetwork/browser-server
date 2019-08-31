package com.platon.browser.res.home;

import lombok.Data;

/**
 * 首页趋势图返回对象
 *  @file BlockStatisticNewResp.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
@Data
public class BlockStatisticNewResp {
	private Long[] x;
	private Double[] ya;
	private Long[] yb;

}
