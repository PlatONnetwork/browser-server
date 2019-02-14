package com.platon.browser.contract;

import com.platon.browser.TestBase;
import com.platon.browser.client.PlatonClient;
import lombok.Data;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.web3j.platon.contracts.TicketContract;

import java.math.BigInteger;
import java.util.List;

@RunWith(SpringRunner.class)
public class TicketContractTest extends TestBase {

    private static final Logger logger = LoggerFactory.getLogger(TicketContractTest.class);

    @Autowired
    private PlatonClient platon;

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

    @Test
    public void getTicketList(){
        chainsConfig.getChainIds().forEach(chainId->{
            String txHash = "0xdc5ff0d689fc327d5df4fe21a66f231bd4f851736e212549dc8ff0d6a21a1ae5";
            TicketContract ticketContract = platon.getTicketContract(chainId);
            List<String> ticketIds = ticketContract.VoteTicketIds(100,txHash);
            if(ticketIds.size()==0) return;

            StringBuilder sb = new StringBuilder();
            String tail = ticketIds.get(ticketIds.size()-1);
            ticketIds.forEach(id->{
                sb.append(id);
                if(!tail.equals(id)) sb.append(":");
            });
            try {
                String details = ticketContract.GetBatchTicketDetail(sb.toString()).send();
                System.out.println(details);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
