package com.platon.browser.elasticsearch.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

@Data
public class Transaction {
    private Long id;
    private String hash;
    private String bHash;
    private Long num;
    private Integer index;
    private Date time;
    private String nonce;
    private Integer status;
    private BigDecimal gasPrice;
    private BigDecimal gasUsed;
    private BigDecimal gasLimit;
    private String from;
    private String to;
    private BigDecimal value;
    private String type;
    private BigDecimal cost;
    private Integer toType;
    private Long seq;
    private Date creTime;
    private Date updTime;
    private String input;
    private String info;
    private String failReason;
}