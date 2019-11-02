package com.platon.browser.elasticsearch.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

@Data
@Accessors(chain = true)
public class Block {
    private Long num;
    private String hash;
    private String pHash;
    private Date time;
    private Integer size;
    private BigDecimal gasLimit;
    private BigDecimal gasUsed;
    private Integer txQty;
    private Integer tranQty;
    private Integer sQty;
    private Integer pQty;
    private Integer dQty;
    private BigDecimal txGasLimit;
    private BigDecimal txFee;
    private String nodeName;
    private String nodeId;
    private BigDecimal reward;
    private String miner;
    private Date creTime;
    private Date updTime;
    private String extra;
}