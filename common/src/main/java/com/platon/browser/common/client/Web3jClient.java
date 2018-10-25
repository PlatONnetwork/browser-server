package com.platon.browser.common.client;

import com.platon.browser.common.constant.ConfigConst;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

/**
 * User: dongqile
 * Date: 2018/10/25
 * Time: 14:42
 */
public class Web3jClient {
    private static Web3j web3j;

    private Web3jClient (){}

    static {
        web3j = Web3j.build(new HttpService(ConfigConst.getNodeIp()));
    }

    public static Web3j getWeb3jClient(){
        return web3j;
    }
}
