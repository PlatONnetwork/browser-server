package com.platon.browser.request.token;

import com.platon.browser.request.PageReq;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 查询合约持有人列表
 *
 * @author AgentRJ
 * @create 2020-09-23 14:36
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueryTokenHolderListReq extends PageReq {

    @NotBlank(message = "{contract address required}")
    @Size(min = 42, max = 42)
    private String contract;

    @NotBlank(message = "{erc20/erc721/erc1155}")
    private String ercType;

}
