package com.platon.browser.req.app;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Auther: Chendongming
 * @Date: 2019/5/13 09:45
 * @Description:
 */
@Data
public class AppNodeDetailReq {
    @NotBlank(message = "{node.id.notnull}")
    private String nodeId;
}
