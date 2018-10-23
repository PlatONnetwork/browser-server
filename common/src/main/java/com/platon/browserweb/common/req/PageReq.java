/*
 * Copyright (c) 2017. juzhen.io. All rights reserved.
 */

package com.platon.browserweb.common.req;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

public class PageReq {
    /**
     * 当前页
     */
    private Integer pageNo = 1;
    /**
     * 页大小
     */
    private Integer pageSize = 10;

    private Page pager;

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
        this.pageSize = pageSize;
    }

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
