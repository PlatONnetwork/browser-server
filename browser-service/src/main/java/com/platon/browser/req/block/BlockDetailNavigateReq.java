package com.platon.browser.req.block;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class BlockDetailNavigateReq {
    @NotBlank(message = "{chain.id.notnull}")
    private String cid;
    @NotBlank(message = "{navigate.direction.notnull}")
    @Pattern(regexp = "prev|next", message = "{direction.illegal}")
    private String direction;
    @NotNull(message = "{block.height.notnull}")
    private Long height;
}
