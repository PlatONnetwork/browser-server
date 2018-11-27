package com.platon.browser.req.node;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
public class NodeBlockListReq {
    @NotBlank(message = "{chain.id.notnull}")
    private String cid;
    @NotNull(message = "{node.address.notnull}")
    private String address;
}