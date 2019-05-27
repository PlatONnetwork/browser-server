package com.platon.browser;

import org.junit.Test;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.websocket.WebSocketClient;
import org.web3j.protocol.websocket.WebSocketService;

import java.io.IOException;
import java.math.BigInteger;

/**
 * @Auther: Chendongming
 * @Date: 2019/5/21 16:28
 * @Description:
 */
public class TestWebsock {

    @Test
    public void testWebsocket() throws IOException {
        Web3j web3j = Web3j.build(new WebSocketService("ws://192.168.112.183:6790",true));
//        Web3j web3j = Web3j.build(new HttpService("http://192.168.112.183:6789",true));
        BigInteger num = web3j.ethBlockNumber().send().getBlockNumber();
        System.out.println(num.toString());
        //WebSocketClient wsc = new WebSocketClient();

    }

}
