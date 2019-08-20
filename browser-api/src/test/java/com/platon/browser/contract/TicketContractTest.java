package com.platon.browser.contract;

import com.platon.browser.TestBase;
import com.platon.browser.client.PlatonClient;
import lombok.Data;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigInteger;

//@RunWith(SpringRunner.class)
public class TicketContractTest extends TestBase {

    private static final Logger logger = LoggerFactory.getLogger(TicketContractTest.class);

//    @Autowired
//    private PlatonClient platon;

    @Data
    public static class TxInfo{
        @Data
        public static class Parameter{
            BigInteger price;
            Integer count;
            String nodeId;
        }
        String functionName,type;
        Parameter parameters;
    }
}
