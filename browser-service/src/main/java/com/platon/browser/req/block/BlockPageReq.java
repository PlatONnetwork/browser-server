package com.platon.browser.req.block;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper = false)
public class BlockPageReq {
    @NotBlank(message = "{chain.id.notnull}")
    private String cid;
    private Integer pageNo = 1;
    private Integer pageSize = 10;
}
