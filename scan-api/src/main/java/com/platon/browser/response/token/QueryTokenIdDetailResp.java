package com.platon.browser.response.token;

import com.platon.browser.bean.CustomTokenInventory;
import com.platon.browser.utils.CommonUtil;
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

    public static QueryTokenIdDetailResp copy(CustomTokenInventory source) {
        return QueryTokenIdDetailResp.builder()
                .address(CommonUtil.ofNullable(() -> source.getOwner()).orElse(""))
                .contract(CommonUtil.ofNullable(() -> source.getTokenAddress()).orElse(""))
                .tokenId(CommonUtil.ofNullable(() -> source.getTokenAddress()).orElse(""))
                .image(CommonUtil.ofNullable(() -> source.getImage()).orElse(""))
                .txCount(CommonUtil.ofNullable(() -> source.getTokenTxQty()).orElse(0))
                .name(CommonUtil.ofNullable(() -> source.getName()).orElse(""))
                .tokenName(CommonUtil.ofNullable(() -> source.getTokenName()).orElse(""))
                .symbol(CommonUtil.ofNullable(() -> source.getSymbol()).orElse(""))
                .build();
    }

}
