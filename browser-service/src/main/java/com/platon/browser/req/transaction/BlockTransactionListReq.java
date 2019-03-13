package com.platon.browser.req.transaction;

import com.platon.browser.req.PageReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
public class BlockTransactionListReq extends PageReq {
    @NotBlank(message = "{chain.id.notnull}")
    private String cid;
    @NotNull(message = "{block.height.notnull}")
    private Long height;
}
