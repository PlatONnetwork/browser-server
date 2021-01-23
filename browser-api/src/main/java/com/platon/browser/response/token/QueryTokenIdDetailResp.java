package com.platon.browser.response.token;

import com.platon.browser.bean.CustomToken;
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
public class QueryTokenIdDetailResp {

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

    public static QueryTokenIdDetailResp fromToken(CustomToken token) {
        return QueryTokenIdDetailResp.builder()
                .address(token.getAddress()).contract(token.getName())
                .tokenId(token.getSymbol()).image(token.getAddress())
                .txCount(null)
                .build();
    }
}
