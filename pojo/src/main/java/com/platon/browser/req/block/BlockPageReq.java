package com.platon.browser.req.block;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper = false)
public class BlockPageReq {
    @NotBlank(message = "链ID不能为空！")
    private String cid;
    private Integer pageNo = 1;
    private Integer pageSize = 10;
}
