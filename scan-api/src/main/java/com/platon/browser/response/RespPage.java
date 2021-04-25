package com.platon.browser.response;

import com.github.pagehelper.Page;

import java.util.Collections;
import java.util.List;

/**
 * 统一返回分页对象
 *
 * @author zhangrj
 * @file RespPage.java
 * @description
 * @data 2019年8月31日
 */
public class RespPage<T> {
    /**
     * 相关的错误信息
     */
    private String errMsg = "";
    /**
     * 成功（0），失败则由相关失败码
     */
    private int code = 0;
    /**
     * 总数
     */
    private long totalCount;
    /**
     * 显示总数
     */
    private long displayTotalCount;
    /**
     * 总页数
     */
    private long totalPages;
    /**
     * 响应数据
     */
    private List<T> data = Collections.emptyList();

    /**
     * 初始化数据
     */
    public void init(Page<?> page, List<T> data) {
        this.setTotalCount(page.getTotal());
        this.setTotalPages(page.getPages());
        this.setDisplayTotalCount(page.getTotal());
        this.setData(data);
    }

    /**
     * 初始化数据
     */
    public void init(List<T> data, long totalCount, long displayTotalCount, long totalPages) {
        this.setTotalCount(totalCount);
        this.setTotalPages(totalPages);
        this.setDisplayTotalCount(displayTotalCount);
        this.setData(data);
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public long getDisplayTotalCount() {
        return displayTotalCount;
    }

    public void setDisplayTotalCount(long displayTotalCount) {
        this.displayTotalCount = displayTotalCount;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

}
