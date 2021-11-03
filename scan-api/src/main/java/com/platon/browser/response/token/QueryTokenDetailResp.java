package com.platon.browser.response.token;

import com.platon.browser.bean.CustomTokenDetail;
import com.platon.browser.utils.ConvertUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 合约信息详情返回
 *
 * @author AgentRJ
 * @create 2020-09-23 14:35
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class QueryTokenDetailResp {

    private String address;

    private String name;

    private String type;

    private String symbol;

    private Integer decimal;

    private String totalSupply;

    private String icon;

    private String creator;

    private String txHash;

    private String webSite;

    private Date blockTimestamp;

    private Integer holder;

    private Date createTime;

    // attach info, maybe is empty.
    private Integer txCount;

    private String abi;

    private String binCode;

    private String sourceCode;

    private int isContractDestroy;

    public static QueryTokenDetailResp fromTokenDetail(CustomTokenDetail token) {
        if (null == token) {
            return null;
        }
        BigDecimal totalSupply = BigDecimal.ZERO;
        if (token.getTotalSupply() != null && token.getDecimal() != null) {
            totalSupply = ConvertUtil.convertByFactor(new BigDecimal(token.getTotalSupply()), token.getDecimal());
        }
        return QueryTokenDetailResp.builder()
                .address(token.getAddress()).name(token.getName())
                .symbol(token.getSymbol()).decimal(token.getDecimal())
                .totalSupply(totalSupply.toString())
                .creator(token.getCreator()).txHash(token.getTxHash())
                .txCount(token.getTxCount())
                .createTime(token.getCreateTime())
                .holder(token.getHolder())
                .binCode(token.getBinCode())
                .webSite(token.getWebSite())
                .type(token.getType())
                .isContractDestroy(token.getIsContractDestroy())
                .build();
    }

}
