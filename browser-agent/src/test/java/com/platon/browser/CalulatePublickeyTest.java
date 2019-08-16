package com.platon.browser;

import com.platon.browser.client.PlatonClient;
import com.platon.browser.utils.NodeTool;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.PlatonBlock;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;

/**
 * User: dongqile
 * Date: 2019/8/13
 * Time: 10:41
 */
@SpringBootTest(classes= BrowserAgentApplication.class, value = "spring.profiles.active=agent")
public class CalulatePublickeyTest {

    @Autowired
    private PlatonClient client;

    @Test
    public void testCaluateTest(){
        try {
            Web3j web3j = Web3j.build(new HttpService("http://10.10.8.200:6789"));
            PlatonBlock.Block initData = web3j.platonGetBlockByNumber(DefaultBlockParameter.valueOf(BigInteger.valueOf(1000)),true).send().getBlock();
            BigInteger nodeIdInteger = NodeTool.testBlock(initData);
            String publicKey = nodeIdInteger.toString(16);
            System.out.println(publicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
