package com.platon.browser.req.app;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Auther: Chendongming
 * @Date: 2019/5/16 18:17
 * @Description:
 */
@Data
public class HeaderReq {
    @NotBlank(message = "{chain.id.notnull}")
    private String cid;
}
