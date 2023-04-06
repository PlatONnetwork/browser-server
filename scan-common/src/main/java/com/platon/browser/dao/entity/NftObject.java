package com.platon.browser.dao.entity;

import lombok.Data;

@Data
public class NftObject {
    private Long tokenInventoryId;
    private Long tokenId;
    private String tokenAddress;
    private String tokenType;
    private Long createdBlockNumber;
    private String tokenUrl;
    private String image;
    private String description;
    private String name;
    private Integer retryNum;
    private Integer decimal;
}
