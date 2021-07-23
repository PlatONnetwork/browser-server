package com.platon.browser;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import com.platon.browser.client.PlatOnClient;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringRunner.class)
@SpringBootTest(classes= BrowserServiceApplication.class, value = "spring.profiles.active=dev",webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiTestBase extends ApiTestData {

//    @Autowired
//    protected BlockService blockService;
//    @Autowired
//    protected TransactionService transactionService;

    @Autowired
    protected RedisTemplate<String,String> redisTemplate;

    /*@Autowired
    protected TicketService ticketService;*/

    @Autowired
    protected PlatOnClient platon;

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


//    protected Transaction getOneVoteTransaction( String chainId){
//        TransactionExample example = new TransactionExample();
//        example.createCriteria().andTxTypeEqualTo(TransactionTypeEnum.VOTING_PROPOSAL.code);
//        return null;
//    }

}
