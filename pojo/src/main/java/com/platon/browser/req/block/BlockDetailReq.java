package com.platon.browser.req.block;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Data
public class BlockDetailReq {
    @NotBlank(message = "链ID不能为空！")
    private String cid;
    @NotNull(message = "块高不能为空！")
    private Long height;
}
