package com.platon.browser.response.home;

/**
 * 首页趋势图返回对象
 *  @file BlockStatisticNewResp.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class BlockStatisticNewResp {
	private Long[] x;
	private Double[] ya;
	private Long[] yb;
	public Long[] getX() {
		return x;
	}
	public void setX(Long[] x) {
		this.x = x;
	}
	public Double[] getYa() {
		return ya;
	}
	public void setYa(Double[] ya) {
		this.ya = ya;
	}
	public Long[] getYb() {
		return yb;
	}
	public void setYb(Long[] yb) {
		this.yb = yb;
	}

}
