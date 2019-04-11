package com.platon.browser;

import com.platon.browser.client.PlatonClient;
import com.platon.browser.config.ChainsConfig;
import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.dao.entity.TransactionExample;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dao.mapper.NodeRankingMapper;
import com.platon.browser.dao.mapper.PendingTxMapper;
import com.platon.browser.dao.mapper.TransactionMapper;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.block.BlockListItem;
import com.platon.browser.dto.node.NodeListItem;
import com.platon.browser.dto.transaction.TransactionListItem;
import com.platon.browser.enums.TransactionTypeEnum;
import com.platon.browser.req.block.BlockPageReq;
import com.platon.browser.req.node.NodePageReq;
import com.platon.browser.req.transaction.TransactionPageReq;
import com.platon.browser.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

@SpringBootTest(classes= ApiApplication.class, value = "spring.profiles.active=testa")
public class TestBase extends TestData {
    @Autowired
    protected NodeRankingMapper nodeRankingMapper;
    @Autowired
    protected BlockMapper blockMapper;
    @Autowired
    protected TransactionMapper transactionMapper;
    @Autowired
    protected PendingTxMapper pendingTxMapper;
    @Autowired
    protected ChainsConfig chainsConfig;
    @Autowired
    protected NodeService nodeService;
    @Autowired
    protected BlockService blockService;
    @Autowired
    protected TransactionService transactionService;
    @Autowired
    protected PendingTxService pendingTxService;

    @Autowired
    protected RedisTemplate<String,String> redisTemplate;

    /*@Autowired
    protected TicketService ticketService;*/

    @Autowired
    protected PlatonClient platon;

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

    protected Transaction getOneVoteTransaction(String chainId){
        TransactionExample example = new TransactionExample();
        example.createCriteria().andChainIdEqualTo(chainId).andTxTypeEqualTo(TransactionTypeEnum.TRANSACTION_VOTE_TICKET.code);
        List<Transaction> transactions = transactionMapper.selectByExample(example);
        if(transactions.size()==0) return null;
        return transactions.get(0);
    }
}
