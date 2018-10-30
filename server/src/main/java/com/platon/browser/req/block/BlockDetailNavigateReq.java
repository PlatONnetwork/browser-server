package com.platon.browser.req.block;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class BlockDetailNavigateReq {
    @NotBlank(message = "链ID不能为空！")
    private String cid;
    @NotBlank(message = "浏览方向不能为空！")
    @Pattern(regexp = "prev|next", message = "方向取值不合法！")
    private String direction;
    @NotNull(message = "块高不能为空！")
    private Long height;
}
