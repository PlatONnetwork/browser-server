package com.platon.browser.response.token;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * Erc20 持有人对应token列表信息
 *
 * @author AgentRJ
 * @create 2020-09-23 14:39
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class QueryHolderTokenListResp {

    private String address;

    private String contract;

    private BigDecimal balance;

    private Integer decimal;

    private String symbol;

    private String name;

    private Integer txCount;

    private String tokenId;

    /**
     * token所属合约是否已销毁：0-否，1-是
     */
    private int isContractDestroy;

}
