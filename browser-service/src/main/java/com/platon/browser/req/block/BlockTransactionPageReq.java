package com.platon.browser.req.block;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
public class BlockTransactionPageReq {
    @NotBlank(message = "{chain.id.notnull}")
    private String cid;
    @NotNull(message = "{block.height.notnull}")
    private Long blockNumber;
    private Integer pageNo = 1;
    private Integer pageSize = 10;
    private String txType;
}
