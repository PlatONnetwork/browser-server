package com.platon.browser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import com.platon.browser.client.PlatonClient;
import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.dao.entity.TransactionExample;
import com.platon.browser.dto.block.BlockListItem;
import com.platon.browser.dto.transaction.TransactionListItem;
import com.platon.browser.enums.TransactionTypeEnum;

@SpringBootTest(classes= BrowserApiApplication.class, value = "spring.profiles.active=dev")
public class TestBase extends TestData {

//    @Autowired
//    protected BlockService blockService;
//    @Autowired
//    protected TransactionService transactionService;

    @Autowired
    protected RedisTemplate<String,String> redisTemplate;

    /*@Autowired
    protected TicketService ticketService;*/

    @Autowired
    protected PlatonClient platon;

   /* protected NodeListItem getOneNode(String chainId){
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
    }*/

    protected BlockListItem getOneBlock(String chainId){
       /* BlockPageReq req = new BlockPageReq();
        req.setCid(chainId);
        req.setPageSize(1);
        req.setPageSize(1);
        RespPage <BlockListItem> data = blockService.getPage(req);
        if(data.getData().size()==0){
            return null;
        }
        return data.getData().get(0);*/
        return null;
    }

    protected TransactionListItem getOneTransaction(String chainId){
       /* TransactionPageReq req = new TransactionPageReq();
        req.setCid(chainId);
        req.setPageSize(1);
        req.setPageSize(1);
        RespPage<TransactionListItem> data = transactionService.getPage(req);
        if(data.getData().size()==0){
            return null;
        }
        return data.getData().get(0);*/
       return null;
    }

    protected Transaction getOneVoteTransaction( String chainId){
        TransactionExample example = new TransactionExample();
        example.createCriteria().andTxTypeEqualTo(TransactionTypeEnum.VOTING_PROPOSAL.code);
        return null;
    }
}
