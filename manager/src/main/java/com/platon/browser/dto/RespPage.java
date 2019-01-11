package com.platon.browser.dto;

import com.github.pagehelper.Page;
import lombok.Data;

import java.util.List;

@Data
public class RespPage<T> {
    //相关的错误信息
    private String errMsg = "";
    //成功（0），失败则由相关失败码
    private int code = 0;
    //总数
    private long totalCount;
    //显示总数
    private long displayTotalCount;
    //总页数
    private long totalPages;
    // 响应数据
    private List<T> data;

    public void init(Page page, List<T> data){
        this.setTotalCount(page.getTotal());
        this.setTotalPages(page.getPages());
        this.setDisplayTotalCount(page.getTotal());
        this.setData(data);
    }
}
