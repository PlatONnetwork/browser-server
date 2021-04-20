package com.platon.browser.response.token;

import com.platon.browser.bean.CustomToken;
import com.platon.browser.utils.ConvertUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

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
public class QueryTokenListResp {

    private String address;

    private String type;

    private String name;

    private String symbol;

    private Integer decimal;

    private BigDecimal totalSupply;

    private String icon;

    private String webSite;

    private String details;

    private Date createTime;

    private Integer holder;

//    @ApiModelProperty(value = "合约创建者")
//    private String creator;
//    @ApiModelProperty(value = "合约部署所在哈希")
//    private String txHash;
//    @ApiModelProperty(value = "合约创建时间")
//    private Date blockTimestamp;

//    public static QueryTokenListResp fromErc20Token(Erc20Token token) {
//        return QueryTokenListResp.builder()
//                .address(token.getAddress()).name(token.getName())
//                .symbol(token.getSymbol()).decimal(token.getDecimal())
//                .totalSupply(ConvertUtil.convertByFactor(token.getTotalSupply(), token.getDecimal()))
//                .creator(token.getCreator()).txHash(token.getTxHash())
//                .blockTimestamp(token.getBlockTimestamp())
//                .createTime(token.getCreateTime())
//                .holder(token.getHolder())
//                .build();
//    }

    public static QueryTokenListResp fromToken(CustomToken token) {
        return QueryTokenListResp.builder()
                .address(token.getAddress()).name(token.getName()).type(token.getType())
                .symbol(token.getSymbol()).decimal(token.getDecimal())
                .totalSupply(ConvertUtil.convertByFactor(token.getTotalSupply() == null? BigDecimal.ZERO : token.getTotalSupply(), token.getDecimal() == null ? 0 : token.getDecimal()))
                .createTime(token.getCreateTime())
                .details(token.getDetails()).holder(token.getHolder())
                .build();
    }

}
