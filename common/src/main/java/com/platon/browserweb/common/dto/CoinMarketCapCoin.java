/*
 * Copyright (c) 2018. juzix.io. All rights reserved.
 */

package com.platon.browserweb.common.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: CK
 * @date: 2018/7/17
 */
public class CoinMarketCapCoin {
    private long id;
    private String name;
    private String symbol;
    private String website_slug;
    private int rank;
    private BigDecimal circulating_supply;
    private BigDecimal total_supply;
    private BigDecimal max_supply;
    private Map<String, CoinMarketCapCoinQuote> quotes;
    private Date last_updated;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getWebsite_slug() {
        return website_slug;
    }

    public void setWebsite_slug(String website_slug) {
        this.website_slug = website_slug;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public BigDecimal getCirculating_supply() {
        return circulating_supply;
    }

    public void setCirculating_supply(BigDecimal circulating_supply) {
        this.circulating_supply = circulating_supply;
    }

    public BigDecimal getTotal_supply() {
        return total_supply;
    }

    public void setTotal_supply(BigDecimal total_supply) {
        this.total_supply = total_supply;
    }

    public BigDecimal getMax_supply() {
        return max_supply;
    }

    public void setMax_supply(BigDecimal max_supply) {
        this.max_supply = max_supply;
    }

    public Map<String, CoinMarketCapCoinQuote> getQuotes() {
        return quotes;
    }

    public void setQuotes(Map<String, CoinMarketCapCoinQuote> quotes) {
        this.quotes = quotes;
    }

    public Date getLast_updated() {
        return last_updated;
    }

    public void setLast_updated(Date last_updated) {
        this.last_updated = last_updated;
    }
}
