package com.platon.browser.bean;

import com.platon.browser.dao.entity.TokenHolderKey;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

@Data
public class CustomTokenHolder {
    private String tokenAddress;

    private String address;

    private BigDecimal balance;

    private String type;

    private String symbol;

    private String name;

    private BigDecimal totalSupply;

    private Integer decimal;

    private Integer txCount;

}