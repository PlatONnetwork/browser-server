package com.platon.browser.req.node;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper = false)
public class NodePageReq {
    @NotBlank(message = "{chain.id.notnull}")
    private String cid;
    private String keyword;
    private Integer isValid;
    private Integer nodeType;
    private Integer pageNo = 1;
    private Integer pageSize = 10;
}
