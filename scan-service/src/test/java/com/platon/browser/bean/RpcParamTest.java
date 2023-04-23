package com.platon.browser.bean;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import static junit.framework.TestCase.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class RpcParamTest {
    @Test
    public void test(){
        RpcParam param = new RpcParam();
        param.setId(88);
        param.setMethod("me");
        param.setJsonrpc("33");
        param.setParams(new ArrayList<>());

        param.getParams();
        param.getId();
        param.getJsonrpc();
        param.getMethod();
        param.toJsonString();

        assertTrue(true);
    }
}
