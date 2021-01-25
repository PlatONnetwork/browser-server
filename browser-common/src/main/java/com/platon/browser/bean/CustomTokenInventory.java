package com.platon.browser.bean;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class CustomTokenInventory {
    private String tokenAddress;

    private String tokenId;

    private Integer tokenTxQty;

    private String name;

    private String owner;

    private String creator;

    private String txHash;

    private String webSite;



}