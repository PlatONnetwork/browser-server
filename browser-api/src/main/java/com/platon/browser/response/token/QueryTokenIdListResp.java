package com.platon.browser.response.token;

import com.platon.browser.bean.CustomToken;
import com.platon.browser.dao.entity.TokenInventory;
import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(value = "钱包地址")
    private String address;
    @ApiModelProperty(value = "合约地址")
    private String contract;
    @ApiModelProperty(value = "tokenId")
    private String tokenId;
    @ApiModelProperty(value = "图片")
    private String image;
    @ApiModelProperty(value = "交易数")
    private Integer txCount;

    public static QueryTokenIdListResp fromToken(TokenInventory token) {
        return QueryTokenIdListResp.builder()
                .address(token.getOwner()).contract(token.getTokenAddress())
                .tokenId(token.getTokenAddress()).image(token.getImage())
                .txCount(token.getTokenTxQty())
                .build();
    }
}
