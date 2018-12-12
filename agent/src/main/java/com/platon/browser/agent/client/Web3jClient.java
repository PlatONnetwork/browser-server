package com.platon.browser.agent.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import javax.annotation.PostConstruct;

/**
 * User: dongqile
 * Date: 2018/10/25
 * Time: 14:42
 */
@Component
public class Web3jClient {

    @Value("${node.ip}")
    private String nodeIp;

    private static Web3j web3j;

    private Web3jClient (){
    }

    @PostConstruct
    private void init(){
        web3j = Web3j.build(new HttpService(nodeIp));
    }

    public static Web3j getWeb3jClient(){
        return web3j;
    }
}
