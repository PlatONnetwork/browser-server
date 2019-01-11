package com.platon.browser.req.transaction;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper = false)
public class TransactionPageReq {
    @NotBlank(message = "{chain.id.notnull}")
    private String cid;
    private Long height;
    private Integer pageNo = 1;
    private Integer pageSize = 10;
}
