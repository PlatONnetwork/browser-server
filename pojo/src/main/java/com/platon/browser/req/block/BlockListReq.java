package com.platon.browser.req.block;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
public class BlockListReq {
    @NotBlank(message = "{chain.id.notnull}")
    private String cid;
    @NotNull(message = "{node.address.notnull}")
    private String address;
}