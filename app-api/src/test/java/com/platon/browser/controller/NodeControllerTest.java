package com.platon.browser.controller;

import com.platon.browser.req.app.AppUserNodeListReq;
import com.platon.browser.req.node.NodePageReq;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
public class NodeControllerTest extends ControllerTestBase {
    private static final Logger logger = LoggerFactory.getLogger(NodeControllerTest.class);

    @Test
    public void list() {
        chainsConfig.getChainIds().forEach(chainId->{
            try {
                sendPost("/node/list",chainId,new Object());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void listUserVoteNode() {
        chainsConfig.getChainIds().forEach(chainId->{
            try {
                List<String> walletAddrs = new ArrayList<>();
                walletAddrs.add("0x275cddfd926ed4d7dd8208392c1487655dd33929");
                AppUserNodeListReq req = new AppUserNodeListReq();
                req.setWalletAddrs(walletAddrs);
                sendPost("/node/listUserVoteNode",chainId,req);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
