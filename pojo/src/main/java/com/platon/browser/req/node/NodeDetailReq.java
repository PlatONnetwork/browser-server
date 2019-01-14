package com.platon.browser.req.node;

import lombok.Data;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Data
public class NodeDetailReq {
    @NotBlank(message = "{chain.id.notnull}")
    private String cid;
    @NotNull(message = "{node.id.notnull}")
    private Long id;
}
