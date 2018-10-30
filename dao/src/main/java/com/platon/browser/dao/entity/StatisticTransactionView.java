package com.platon.browser.dao.entity;

import java.util.Date;

public class StatisticTransactionView {
    private Long height;

    private Date time;

    private Long transaction;

    public Long getHeight() {
        return height;
    }

    public void setHeight(Long height) {
        this.height = height;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Long getTransaction() {
        return transaction;
    }

    public void setTransaction(Long transaction) {
        this.transaction = transaction;
    }
}