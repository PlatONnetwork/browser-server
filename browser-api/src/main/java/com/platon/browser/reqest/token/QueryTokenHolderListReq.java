package com.platon.browser.reqest.token;

import com.platon.browser.reqest.PageReq;
import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(value = "合约地址")
    @Size(min = 42, max = 42)
    @NotBlank
    private String contract;

}
