package com.platon.browser.req.node;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class NodeDetailReq {
    @NotBlank(message = "{chain.id.notnull}")
    private String cid;
    private Long id;
    private String nodeId;
}
