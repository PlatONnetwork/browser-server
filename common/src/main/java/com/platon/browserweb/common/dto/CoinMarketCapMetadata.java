/*
 * Copyright (c) 2018. juzix.io. All rights reserved.
 */

package com.platon.browserweb.common.dto;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: CK
 * @date: 2018/7/17
 */
public class CoinMarketCapMetadata {
    private Date timestamp;
    private long num_cryptocurrencies;
    private String error;

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public long getNum_cryptocurrencies() {
        return num_cryptocurrencies;
    }

    public void setNum_cryptocurrencies(long num_cryptocurrencies) {
        this.num_cryptocurrencies = num_cryptocurrencies;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
