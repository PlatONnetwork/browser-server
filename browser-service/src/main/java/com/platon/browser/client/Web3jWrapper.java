package com.platon.browser.client;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jService;

/**
 * @description:
 * @author: chendongming@juzix.net
 * @create: 2019-11-16 15:24:09
 **/
@Data
@Builder
@Accessors(chain = true)
public class Web3jWrapper {
    private Web3j web3j;
    private Web3jService web3jService;
    private String address;
}
