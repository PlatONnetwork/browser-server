package com.platon.browser.bean;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class CustomTokenDetail extends CustomToken{
    private String creator;

    private String txHash;

    private Integer txCount;

    private String binCode;

}