package com.platon.browser.config.bean;

import lombok.Data;

/**
 * @Auther: Chendongming
 * @Date: 2019/9/7 22:07
 * @Description:
 */
@Data
public class Web3Response {
    private String jsonrpc;
    private int id;
    private Error error;
    private String result;
}


