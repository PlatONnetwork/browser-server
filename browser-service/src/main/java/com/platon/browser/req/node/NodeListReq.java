package com.platon.browser.req.node;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper = false)
public class NodeListReq {
    @NotBlank(message = "{chain.id.notnull}")
    private String cid;
    private String keyword;
}