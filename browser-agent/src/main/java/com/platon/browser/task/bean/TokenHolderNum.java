package com.platon.browser.task.bean;

import lombok.Data;

import java.util.Date;

@Data
public class TokenHolderNum {

    /**
     * 合约地址
     */
    private String tokenAddress;

    /**
     * 用户地址统计数
     */
    private Integer holderNum;

    /**
     * 更新时间
     */
    private Date updateTime;

}
