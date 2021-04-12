package com.platon.browser.response.token;

import com.platon.browser.dao.entity.TokenInventory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 合约列表数据响应报文
 *
 * @author AgentRJ
 * @create 2020-09-23 14:39
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class QueryTokenIdListResp {

    private String address;
    private String contract;
    private String tokenId;
    private String image;
    private Integer txCount;

    public static QueryTokenIdListResp fromToken(TokenInventory token) {
        return QueryTokenIdListResp.builder()
                .address(token.getOwner()).contract(token.getTokenAddress())
                .tokenId(token.getTokenId()).image(token.getImage())
                .txCount(token.getTokenTxQty())
                .build();
    }
}
