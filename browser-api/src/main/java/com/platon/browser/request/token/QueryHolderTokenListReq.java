package com.platon.browser.request.token;

import com.platon.browser.request.PageReq;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 查询持有人的合约列表
 *
 * @author AgentRJ
 * @create 2020-09-23 14:36
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueryHolderTokenListReq extends PageReq {

    @NotBlank(message = "{address required}")
    @Size(min = 42, max = 42)
    private String address;

    /**
     * type取值:erc20 | erc721 | 为空
     */
    @NotBlank(message = "{type required}")
    private String type;

}
