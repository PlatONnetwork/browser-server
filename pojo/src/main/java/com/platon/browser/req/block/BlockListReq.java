package com.platon.browser.req.block;

import com.platon.browser.common.req.PageReq;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper = false)
public class BlockListReq extends PageReq {
    @NotBlank(message = "链ID不能为空！")
    private String cid;
}
