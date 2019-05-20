package com.platon.browser.req.app;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Auther: Chendongming
 * @Date: 2019/5/13 09:45
 * @Description:
 */
@Data
public class AppUserNodeListReq {
    @NotEmpty(message = "{transaction.address.notnull}")
    private List<String> walletAddrs;
}
