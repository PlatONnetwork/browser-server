package com.platon.browser.job;

import com.platon.browser.client.PlatonClient;
import com.platon.browser.dao.mapper.CustomBlockMapper;
import com.platon.browser.service.cache.StatisticCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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

    /*@Autowired
    private VoteTxMapper voteTxMapper;*/
    @Autowired
    private PlatonClient platon;
    @Value("${platon.chain.active}")
    private String chainId;

    //@Scheduled(cron = "0/1 * * * * ?")
    /*protected void doJob () {
        //查询数据库未完成的投票交易列表
        VoteTxExample voteTxExample = new VoteTxExample();
        voteTxExample.createCriteria().andCompleteFlagEqualTo("N");
        List<VoteTx> voteTxList = voteTxMapper.selectByExample(voteTxExample);

        //根据hash组成string作为入参
        StringBuffer str = new StringBuffer();
        voteTxList.forEach(voteTx -> str.append(voteTx.getHash()).append(":"));
        String hashs = str.toString();
        hashs = hashs.substring(0,hashs.lastIndexOf(":"));
        //查询链上投票交易有效票
        TicketContract ticketContract = platon.getTicketContract(chainId);
        Map<String,Integer> resMap = new HashMap <>();
        try {
            String res = ticketContract.GetTicketCountByTxHash(hashs).send();
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
    }*/


    @Autowired
    private StatisticCacheService statisticCacheService;
    @Autowired
    private CustomBlockMapper customBlockMapper;
    public static ExecutorService THREAD_POOL = Executors.newFixedThreadPool(6);
    @Scheduled(cron = "0/30 * * * * ?")
    protected void updateTransCount () {
        CountDownLatch latch = new CountDownLatch(6);
        THREAD_POOL.submit(()->{try {statisticCacheService.updateTransCount(chainId);}finally {latch.countDown();}});
        THREAD_POOL.submit(()->{try {statisticCacheService.updateTransCount24Hours(chainId);}finally {latch.countDown();}});
        THREAD_POOL.submit(()->{try {statisticCacheService.updateAddressCount(chainId);}finally {latch.countDown();}});
        THREAD_POOL.submit(()->{try {statisticCacheService.updateAvgBlockTransCount(chainId);}finally {latch.countDown();}});
        THREAD_POOL.submit(()->{try {statisticCacheService.updateTicketPrice(chainId);}finally {latch.countDown();}});
        THREAD_POOL.submit(()->{try {statisticCacheService.updateVoteCount(chainId);}finally {latch.countDown();}});
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
