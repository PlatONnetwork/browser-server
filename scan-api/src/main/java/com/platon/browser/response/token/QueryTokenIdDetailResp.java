package com.platon.browser.response.token;

import com.platon.browser.bean.CustomTokenInventory;
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
public class QueryTokenIdDetailResp {

    private String address;

    private String contract;

    private String tokenId;

    private String image;

    private String name;

    private Integer txCount;

    /**
     * 合约名称
     */
    private String tokenName;

    /**
     * 合约符号
     */
    private String symbol;

    public static QueryTokenIdDetailResp fromTokenIdDetail(TokenInventory token) {
        return QueryTokenIdDetailResp.builder()
                .address(token.getOwner()).contract(token.getTokenAddress())
                .tokenId(token.getTokenAddress()).image(token.getImage())
                .txCount(token.getTokenTxQty()).name(token.getName())
                .build();
    }

    public static QueryTokenIdDetailResp copy(CustomTokenInventory source) {
        return QueryTokenIdDetailResp.builder()
                .address(source.getOwner())
                .contract(source.getTokenAddress())
                .tokenId(source.getTokenAddress())
                .image(source.getImage())
                .txCount(source.getTokenTxQty())
                .name(source.getName())
                .tokenName(source.getTokenName())
                .symbol(source.getSymbol())
                .build();
    }

}
