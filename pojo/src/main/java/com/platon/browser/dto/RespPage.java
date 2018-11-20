package com.platon.browser.dto;

import lombok.Data;

import java.util.List;

@Data
public class RespPage<T> {
    //相关的错误信息
    private String errMsg = "";
    //成功（0），失败则由相关失败码
    private int code = 0;
    //总数
    private int totalCount;
    //总页数
    private int totalPages;
    // 响应数据
    private List<T> data;
}
