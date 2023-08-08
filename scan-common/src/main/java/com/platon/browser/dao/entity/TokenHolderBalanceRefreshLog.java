package com.platon.browser.dao.entity;

import lombok.Data;

@Data
public class TokenHolderBalanceRefreshLog {
    private long id;
    private long blockNumber;
    private String holder;
    private String tokenAddress;
    private String tokenType;  //token类型, erc20, erc721, erc1155
    private String tokenId;
}
