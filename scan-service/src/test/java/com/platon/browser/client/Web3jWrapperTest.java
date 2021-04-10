package com.platon.browser.client;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static junit.framework.TestCase.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class Web3jWrapperTest {
    @Test
    public void test(){
        Web3jWrapper ww = Web3jWrapper.builder()
                .address("0xsfsff")
                .web3jService(null)
                .web3j(null)
                .build();
        ww.setAddress("0xsdfsd");
        ww.setWeb3j(null);
        ww.setWeb3jService(null);

        ww.getWeb3j();
        ww.getAddress();
        ww.getWeb3jService();

        assertTrue(true);
    }
}
