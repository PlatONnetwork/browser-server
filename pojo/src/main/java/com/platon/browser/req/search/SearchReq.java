package com.platon.browser.req.search;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class SearchReq {
    @NotBlank(message = "{chain.id.notnull}")
    private String cid;
    @NotBlank(message = "{search.keyword.notnull}")
    private String parameter;
}
