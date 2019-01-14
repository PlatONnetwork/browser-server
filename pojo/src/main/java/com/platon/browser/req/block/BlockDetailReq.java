package com.platon.browser.req.block;

import lombok.Data;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Data
public class BlockDetailReq {
    @NotBlank(message = "{chain.id.notnull}")
    private String cid;
    @NotNull(message = "{block.height.notnull}")
    private Long height;
}
