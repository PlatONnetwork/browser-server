package com.platon.browser.req.block;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper = false)
public class BlockTicketPageReq {
    @NotBlank(message = "{chain.id.notnull}")
    private String cid;
    @NotBlank(message = "{block.height.notnull}")
    private Long blockNumber;
    private Integer pageNo = 1;
    private Integer pageSize = 10;
}
