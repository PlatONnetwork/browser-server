package com.platon.browser.contract;

import com.platon.browser.TestBase;
import com.platon.browser.client.PlatonClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.web3j.platon.contracts.TicketContract;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RunWith(SpringRunner.class)
public class TicketContractTest extends TestBase {

    private static final Logger logger = LoggerFactory.getLogger(TicketContractTest.class);

    @Autowired
    private PlatonClient platon;


}
