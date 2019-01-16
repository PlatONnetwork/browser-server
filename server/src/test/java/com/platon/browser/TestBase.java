package com.platon.browser;

import com.platon.browser.config.ChainsConfig;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.block.BlockListItem;
import com.platon.browser.dto.node.NodeListItem;
import com.platon.browser.dto.transaction.TransactionListItem;
import com.platon.browser.req.block.BlockPageReq;
import com.platon.browser.req.node.NodePageReq;
import com.platon.browser.req.transaction.TransactionPageReq;
import com.platon.browser.service.BlockService;
import com.platon.browser.service.NodeService;
import com.platon.browser.service.TransactionService;
import com.platon.browser.util.TestDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@SpringBootTest(classes= ServerApplication.class, value = "spring.profiles.active=server")
public class TestBase {
    private static Web3j web3j = Web3j.build(new HttpService("http://192.168.9.76:8793"));
    static { TestDataUtil.web3j = web3j; }

    @Autowired
    protected ChainsConfig chainsConfig;
    @Autowired
    private NodeService nodeService;
    @Autowired
    private BlockService blockService;
    @Autowired
    private TransactionService transactionService;

    protected NodeListItem getOneNode(String chainId){
        NodePageReq req = new NodePageReq();
        req.setCid(chainId);
        req.setPageNo(1);
        req.setPageSize(1);
        RespPage<NodeListItem> nodes = nodeService.getPage(req);
        if(nodes.getData().size()>0){
            return nodes.getData().get(0);
        }else{
            return null;
        }
    }

    protected BlockListItem getOneBlock(String chainId){
        BlockPageReq req = new BlockPageReq();
        req.setCid(chainId);
        req.setPageSize(1);
        req.setPageSize(1);
        RespPage<BlockListItem> data = blockService.getPage(req);
        if(data.getData().size()==0){
            return null;
        }
        return data.getData().get(0);
    }

    protected TransactionListItem getOneTransaction(String chainId){
        TransactionPageReq req = new TransactionPageReq();
        req.setCid(chainId);
        req.setPageSize(1);
        req.setPageSize(1);
        RespPage<TransactionListItem> data = transactionService.getPage(req);
        if(data.getData().size()==0){
            return null;
        }
        return data.getData().get(0);
    }
}
