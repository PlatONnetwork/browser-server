package com.platon.browser.common.req.block;

import com.platon.browser.common.req.PageReq;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class BlockListReq extends PageReq {
    @NotBlank(message = "链ID不能为空！")
    private String cid;
}
