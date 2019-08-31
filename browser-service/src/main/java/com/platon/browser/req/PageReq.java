/*
 * Copyright (c) 2017. juzhen.io. All rights reserved.
 */

package com.platon.browser.req;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import lombok.Data;

/**
 * 	分页对象
 *  @file PageReq.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
@Data
public class PageReq {
    /**
     * 当前页
     */
    private Integer pageNo = 1;
    /**
     * 页大小
     */
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
}
