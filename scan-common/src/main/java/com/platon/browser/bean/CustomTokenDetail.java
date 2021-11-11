package com.platon.browser.bean;

import lombok.Data;

@Data
public class CustomTokenDetail extends CustomToken{
    private String creator;

    private String txHash;

    private Integer txCount;

    private String binCode;

    /**
     * token所属合约是否已销毁：0-否，1-是
     */
    private int isContractDestroy;

}