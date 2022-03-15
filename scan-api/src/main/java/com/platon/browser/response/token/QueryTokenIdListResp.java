package com.platon.browser.response.token;

import cn.hutool.core.util.StrUtil;
import com.platon.browser.bean.CustomTokenHolder;
import com.platon.browser.dao.entity.Token1155Inventory;
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

    private String balance;

    public static QueryTokenIdListResp fromToken(TokenInventory token) {
        // 默认取中等缩略图
        String image = "";
        if (StrUtil.isNotEmpty(token.getMediumImage())) {
            image = token.getMediumImage();
        } else {
            image = token.getImage();
        }
        return QueryTokenIdListResp.builder()
                .address(token.getOwner()).contract(token.getTokenAddress())
                .tokenId(token.getTokenId()).image(image)
                .txCount(token.getTokenTxQty())
                .build();
    }

    public static QueryTokenIdListResp fromToken(CustomTokenHolder token, Token1155Inventory token1155Inventory) {
        // 默认取中等缩略图
        String image = "";
        if (StrUtil.isNotEmpty(token1155Inventory.getMediumImage())) {
            image = token1155Inventory.getMediumImage();
        } else {
            image = token1155Inventory.getImage();
        }
        return QueryTokenIdListResp.builder()
                .contract(token.getTokenAddress())
                .tokenId(token.getTokenId()).image(image)
                .txCount(token1155Inventory.getTokenTxQty())
                .address(token.getAddress())
                .balance(token.getBalance())
                .build();
    }
}
