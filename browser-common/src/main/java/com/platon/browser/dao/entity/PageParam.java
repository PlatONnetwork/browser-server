package com.platon.browser.dao.entity;

import lombok.Data;

@Data
public class PageParam {
    // 链ID
    private String cid;
    // 偏移量
    private int offset;
    // 每页大小
    private int pageSize;
}
