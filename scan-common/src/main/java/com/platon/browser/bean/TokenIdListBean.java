package com.platon.browser.bean;

import lombok.Data;

@Data
public class TokenIdListBean {

    private String address;

    private String contract;

    private String tokenId;

    private String image;

    private Integer txCount;

    private String balance;

}
