package com.platon.browser.service;

import com.platon.browser.dto.app.node.AppNodeDetailDto;
import com.platon.browser.dto.app.node.AppNodeListWrapper;
import com.platon.browser.dto.app.node.AppUserNodeDto;
import com.platon.browser.dto.app.transaction.AppTransactionDto;
import com.platon.browser.dto.app.transaction.AppVoteTransactionDto;
import com.platon.browser.enums.app.DirectionEnum;
import com.platon.browser.req.app.AppTransactionListReq;
import com.platon.browser.req.app.AppTransactionListVoteReq;
import com.platon.browser.req.app.AppUserNodeListReq;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
public class AppNodeServiceTest extends ServiceTestBase {
    private static final Logger logger = LoggerFactory.getLogger(AppNodeServiceTest.class);

    @Test
    public void list(){
        chainsConfig.getChainIds().forEach(chainId -> {
            try {
                AppNodeListWrapper anlw = appNodeService.list(chainId);
                Assert.assertNotNull(anlw);
                Assert.assertNotNull(anlw.getTicketPrice());
                Assert.assertNotNull(anlw.getTotalCount());
                Assert.assertNotNull(anlw.getVoteCount());
                Assert.assertNotNull(anlw.getList());
                Assert.assertTrue(anlw.getList().size()==6);
            } catch (Exception e) {
                Assert.fail(e.getMessage());
            }

        });
    }

    @Test
    public void detail(){
        chainsConfig.getChainIds().forEach(chainId -> {
            String nodeId = "0x97e424be5e58bfd4533303f8f515211599fd4ffe208646f7bfdf27885e50b6dd85d957587180988e76ae77b4b6563820a27b16885419e5ba6f575f19f6cb36b0";
            AppNodeDetailDto node = appNodeService.detail(chainId,nodeId);
            Assert.assertTrue(nodeId.equals(node.getNodeId()));
        });
    }

    @Test
    public void getUserNodeList(){
        chainsConfig.getChainIds().forEach(chainId -> {
            List<String> walletAddrs = new ArrayList<>();
            walletAddrs.add("0x275cddfd926ed4d7dd8208392c1487655dd33929");
            AppUserNodeListReq req = new AppUserNodeListReq();
            req.setWalletAddrs(walletAddrs);
            try {
                List<AppUserNodeDto> nodes = appNodeService.getUserNodeList(chainId,req);
                Assert.assertTrue(nodes.size()>0);
            } catch (Exception e) {
                Assert.fail(e.getMessage());
            }

        });
    }

}
