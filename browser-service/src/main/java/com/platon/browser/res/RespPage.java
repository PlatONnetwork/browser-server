package com.platon.browser.res;

import com.github.pagehelper.Page;
import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * 统一返回分页对象
 *  @file RespPage.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
@Data
public class RespPage<T> {
    /**相关的错误信息*/
    private String errMsg = "";
    /**成功（0），失败则由相关失败码*/
    private int code = 0;
    /**总数*/
    private long totalCount;
    /**显示总数*/
    private long displayTotalCount;
    /**总页数*/
    private long totalPages;
    /**响应数据*/
    private List<T> data= Collections.emptyList();

    /** 初始化数据 */
    public void init(Page<?> page, List<T> data){
        this.setTotalCount(page.getTotal());
        this.setTotalPages(page.getPages());
        this.setDisplayTotalCount(page.getTotal());
        this.setData(data);
    }
}
