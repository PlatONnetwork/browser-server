package com.platon.browser.controller;

import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.req.ticket.TicketListReq;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class TicketControllerTest extends ControllerTestBase {
    private static final Logger logger = LoggerFactory.getLogger(TicketControllerTest.class);

    @Test
    public void getPage() {
        chainsConfig.getChainIds().forEach(chainId->{
            try {
                Transaction transaction = getOneVoteTransaction(chainId);
                TicketListReq req = new TicketListReq();
                req.setCid(chainId);
                req.setTxHash(transaction.getHash());
                sendPost("/ticket/list",req);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
