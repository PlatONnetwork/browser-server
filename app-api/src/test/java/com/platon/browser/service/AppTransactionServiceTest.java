package com.platon.browser.service;

import com.platon.browser.dto.app.transaction.AppTransactionDto;
import com.platon.browser.dto.app.transaction.AppVoteTransactionDto;
import com.platon.browser.enums.app.DirectionEnum;
import com.platon.browser.req.app.AppTransactionListReq;
import com.platon.browser.req.app.AppTransactionListVoteReq;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
public class AppTransactionServiceTest extends ServiceTestBase {
    private static final Logger logger = LoggerFactory.getLogger(AppTransactionServiceTest.class);

    @Test
    public void list(){
        chainsConfig.getChainIds().forEach(chainId -> {
            List<String> walletAddrs = new ArrayList<>();
            walletAddrs.add("0x493301712671ada506ba6ca7891f436d29185821");
            walletAddrs.add("0xf302915d2692a224da0ff80405f1d3a6b2115a55");
            long beginSequence = -1;

            AppTransactionListReq req = new AppTransactionListReq();
            req.setWalletAddrs(walletAddrs);
            req.setListSize(10);
            req.setBeginSequence(beginSequence);
            req.setDirection(DirectionEnum.NEW.name().toLowerCase());
            List<AppTransactionDto> transactionDtos = appTransactionService.list(chainId,req);
            Assert.assertTrue(transactionDtos.size()>0);
            // 断言顺序
            AppTransactionDto t0 = transactionDtos.get(0), t1 = transactionDtos.get(1);
            Long s0 = Long.valueOf(t0.getSequence()), s1=Long.valueOf(t1.getSequence());
            Assert.assertTrue(s0>s1);

            // 断言顺序
            beginSequence = 907940;
            req.setBeginSequence(beginSequence);
            req.setDirection(DirectionEnum.NEW.name().toLowerCase());
            transactionDtos = appTransactionService.list(chainId,req);
            Assert.assertTrue(transactionDtos.size()>0);
            t0 = transactionDtos.get(0);
            t1 = transactionDtos.get(1);
            s0 = Long.valueOf(t0.getSequence());
            s1=Long.valueOf(t1.getSequence());
            Assert.assertTrue(s0>beginSequence);
            Assert.assertTrue(s1>beginSequence);
            Assert.assertTrue(s0>s1);

            // 断言顺序
            beginSequence = 907940;
            req.setBeginSequence(beginSequence);
            req.setDirection(DirectionEnum.OLD.name().toLowerCase());
            transactionDtos = appTransactionService.list(chainId,req);
            Assert.assertTrue(transactionDtos.size()>0);
            t0 = transactionDtos.get(0);
            t1 = transactionDtos.get(1);
            s0 = Long.valueOf(t0.getSequence());
            s1=Long.valueOf(t1.getSequence());
            Assert.assertTrue(s0<beginSequence);
            Assert.assertTrue(s1<beginSequence);
            Assert.assertTrue(s0>s1);
        });
    }

    @Test
    public void listVote(){
        chainsConfig.getChainIds().forEach(chainId -> {
            List<String> walletAddrs = new ArrayList<>();
            walletAddrs.add("0x275cddfd926ed4d7dd8208392c1487655dd33929");
            long beginSequence = -1;

            AppTransactionListVoteReq req = new AppTransactionListVoteReq();
            req.setWalletAddrs(walletAddrs);
            req.setListSize(10);
            req.setBeginSequence(beginSequence);
            req.setNodeId("0x97e424be5e58bfd4533303f8f515211599fd4ffe208646f7bfdf27885e50b6dd85d957587180988e76ae77b4b6563820a27b16885419e5ba6f575f19f6cb36b0");
            req.setDirection(DirectionEnum.NEW.name().toLowerCase());
            List<AppVoteTransactionDto> transactionDtos = appTransactionService.listVote(chainId,req);
            Assert.assertTrue(transactionDtos.size()>0);
            // 断言顺序
            AppVoteTransactionDto t0 = transactionDtos.get(0), t1 = transactionDtos.get(1);
            Long s0 = Long.valueOf(t0.getSequence()), s1=Long.valueOf(t1.getSequence());
            Assert.assertTrue(s0>s1);


            // 断言顺序
            beginSequence = 970130;
            req.setBeginSequence(beginSequence);
            req.setDirection(DirectionEnum.NEW.name().toLowerCase());
            transactionDtos = appTransactionService.listVote(chainId,req);
            Assert.assertTrue(transactionDtos.size()>0);
            t0 = transactionDtos.get(0);
            t1 = transactionDtos.get(1);
            s0 = Long.valueOf(t0.getSequence());
            s1=Long.valueOf(t1.getSequence());
            Assert.assertTrue(s0>beginSequence);
            Assert.assertTrue(s1>beginSequence);
            Assert.assertTrue(s0>s1);

            // 断言顺序
            beginSequence = 970130;
            req.setBeginSequence(beginSequence);
            req.setDirection(DirectionEnum.OLD.name().toLowerCase());
            transactionDtos = appTransactionService.listVote(chainId,req);
            Assert.assertTrue(transactionDtos.size()>0);
            t0 = transactionDtos.get(0);
            t1 = transactionDtos.get(1);
            s0 = Long.valueOf(t0.getSequence());
            s1=Long.valueOf(t1.getSequence());
            Assert.assertTrue(s0<beginSequence);
            Assert.assertTrue(s1<beginSequence);
            Assert.assertTrue(s0>s1);
        });
    }

}
