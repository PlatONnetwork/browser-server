package com.platon.browser.bean;

import lombok.Data;

import java.math.BigInteger;
import java.util.Date;

@Data
public class CustomToken {

    private String address;

    private String type;

    private String name;

    private String symbol;

    private String totalSupply;

    private Integer decimal;

    private String webSite;

    private String details;

    private Date createTime;

    private Integer tokenTxQty;

    private Integer holder;

    private BigInteger createdBlockNumber;

}
