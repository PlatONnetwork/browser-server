package com.platon.browser.bean;

import lombok.Data;

import java.util.Date;

@Data
public class CustomTokenHolder {

    private String tokenAddress;

    private String address;

    private String type;

    private String symbol;

    private String name;

    private String totalSupply;

    private String balance;

    private Integer decimal;

    private Integer txCount;

    private String tokenId;

    private Date createTime;

}