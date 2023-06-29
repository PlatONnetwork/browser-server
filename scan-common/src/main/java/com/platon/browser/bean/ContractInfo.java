package com.platon.browser.bean;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ContractInfo {
    private String address;
    private String bin;     //为空不传
    private int contractType; //为空不传 //0:erc20; 1:erc721; 2:erc1155; 3:general
    private String tokenName; //为空不传
    private String tokenSymbol; //为空不传
    private int tokenDecimals; //为空不传
    private BigDecimal tokenTotalSupply; //为空不传
    private boolean supportErc721Metadata;
    private boolean supportErc721Enumerable;
    private boolean supportErc1155Metadata;
}
