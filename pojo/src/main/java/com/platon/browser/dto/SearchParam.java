package com.platon.browser.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data

public class SearchParam {
    @NotBlank(message = "链ID不能为空！")
    private String cid;
    private String parameter;
}
