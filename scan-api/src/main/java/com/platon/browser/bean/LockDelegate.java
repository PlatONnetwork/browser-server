package com.platon.browser.bean;

import lombok.Data;

import java.math.BigInteger;

@Data
public class LockDelegate {

    /**
     * 解锁块高
     */
    private BigInteger blockNum;

    /**
     * 解锁时间
     */
    private long date;

    /**
     * 冻结的数量（lat）
     */
    private String lock;

}
