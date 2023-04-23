/*
 * Copyright (c) 2017. juzhen.io. All rights reserved.
 */

package com.platon.browser.request;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import javax.validation.constraints.NotNull;

/**
 * 	分页对象
 *  @file PageReq.java
 *  @description
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class PageReq {
    /**
     * 当前页
     */
	@NotNull(message = "{pageNo not null}")
    private Integer pageNo = 1;
    /**
     * 页大小
     */
	@NotNull(message = "{pageSize not null}")
    private Integer pageSize = 10;

    private Page<?> pager;

    /**
     * 上层控制使用分页
     */
    public void buildPage() {
        pager = PageHelper.startPage(this.pageNo, this.pageSize);
    }

    public int getTotal() {
        if (pager != null) {
            return (int) pager.getTotal();
        }
        return 0;
    }

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
    	if(pageSize>1000){
			this.pageSize = 1000;
		} else {
			this.pageSize = pageSize;
		}
	}

	public Page getPager() {
		return pager;
	}

	public void setPager(Page pager) {
		this.pager = pager;
	}

}
