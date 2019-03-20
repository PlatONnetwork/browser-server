package com.platon.browser.job;

import com.alibaba.fastjson.JSON;
import com.platon.browser.client.PlatonClient;
import com.platon.browser.dao.entity.VoteTx;
import com.platon.browser.dao.entity.VoteTxExample;
import com.platon.browser.dao.mapper.CustomBlockMapper;
import com.platon.browser.dao.mapper.VoteTxMapper;
import com.platon.browser.service.RedisCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.platon.contracts.TicketContract;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User: dongqile
 * Date: 2019/3/18
 * Time: 11:20
 */
@Component
public class StatisticsJob {

    private static Logger logger = LoggerFactory.getLogger(StatisticsJob.class);

    @Autowired
    private VoteTxMapper voteTxMapper;
    @Autowired
    private PlatonClient platon;
    @Value("${platon.chain.active}")
    private String chainId;

    //@Scheduled(cron = "0/1 * * * * ?")
    protected void doJob () {
        //查询数据库未完成的投票交易列表
        VoteTxExample voteTxExample = new VoteTxExample();
        voteTxExample.createCriteria().andCompleteFlagEqualTo("N");
        List<VoteTx> voteTxList = voteTxMapper.selectByExample(voteTxExample);

        //根据hash组成string作为入参
        StringBuffer str = new StringBuffer();
        voteTxList.forEach(voteTx ->  {
           str.append(voteTx.getHash()).append(":");
        });
        String hashs = str.toString();

        //查询链上投票交易有效票
        TicketContract ticketContract = platon.getTicketContract(chainId);
        Map<String,Integer> resMap = new HashMap <>();
        try {
            String res = ticketContract.GetCandidateTicketCount(hashs).send();
            resMap = JSON.parseObject(res,Map.class);
        } catch (Exception e) {
            logger.error("根据hash列表查询有效票信息失败! ...");
        }

        //遍历判断
        //如果有效票数为0，证明该投票交易中票已经全部释放
        if(null != resMap){
            for(VoteTx voteTx : voteTxList){
                voteTx.setVaildCount(resMap.get(voteTx.getHash()).longValue());
                if(resMap.get(voteTx.getHash()) == 0){
                    voteTx.setDeadLine(new Date());
                    voteTx.setCompleteFlag("Y");
                }
            }
            voteTxMapper.batchInsert(voteTxList);
        }
    }


    @Autowired
    private RedisCacheService redisCacheService;
    @Autowired
    private CustomBlockMapper customBlockMapper;
    public static ExecutorService THREAD_POOL = Executors.newFixedThreadPool(6);
    @Scheduled(cron = "0/10 * * * * ?")
    protected void updateTransCount () {
        CountDownLatch latch = new CountDownLatch(6);
        THREAD_POOL.submit(()->{try {redisCacheService.updateTransCount(chainId);}finally {latch.countDown();}});
        THREAD_POOL.submit(()->{try {redisCacheService.updateTransCount24Hours(chainId);}finally {latch.countDown();}});
        THREAD_POOL.submit(()->{try {redisCacheService.updateAddressCount(chainId);}finally {latch.countDown();}});
        THREAD_POOL.submit(()->{try {redisCacheService.updateAvgBlockTransCount(chainId);}finally {latch.countDown();}});
        THREAD_POOL.submit(()->{try {redisCacheService.updateTicketPrice(chainId);}finally {latch.countDown();}});
        THREAD_POOL.submit(()->{try {redisCacheService.updateVoteCount(chainId);}finally {latch.countDown();}});
        try {latch.await();} catch (InterruptedException e) {e.printStackTrace();}
    }

    @Scheduled(cron = "0/30 * * * * ?")
    protected void updateBlockNodeName(){
        // 更新区块中的节点名称字段：node_name
        long updateBlockNodeNameBeginTime = System.currentTimeMillis();
        customBlockMapper.updateBlockNodeName(chainId);
        logger.debug("  |-Time Consuming(customBlockMapper.updateBlockNodeName()): {}ms",System.currentTimeMillis()-updateBlockNodeNameBeginTime);
    }
}