/*
 * Copyright (c) 2018. juzix.io. All rights reserved.
 */

package com.platon.browserweb.common.dto;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: CK
 * @date: 2018/7/17
 */
public class CoinMarketCapData {
    private List<CoinMarketCapCoin> data;
    private CoinMarketCapMetadata metadata;

    public List<CoinMarketCapCoin> getData() {
        return data;
    }

    public void setData(List<CoinMarketCapCoin> data) {
        this.data = data;
    }

    public CoinMarketCapMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(CoinMarketCapMetadata metadata) {
        this.metadata = metadata;
    }
}
