package com.platon.browser.job;

import com.github.pagehelper.PageHelper;
import com.platon.browser.config.ChainsConfig;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.BlockExample;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dao.mapper.NodeRankingMapper;
import com.platon.browser.filter.AnalyseFlow;
import com.platon.browser.service.RedisCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthBlock;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * User: dongqile
 * Date: 2018/10/24
 * Time: 17:28
 */
@Component
public class DataCollectorJob {

    private static Logger logger = LoggerFactory.getLogger(DataCollectorJob.class);

    @Autowired
    private BlockMapper blockMapper;

    @Value("${chain.id}")
    private String chainId;
    @Value("${platon.thread.batch.size}")
    private int threadBatchSize;

    @Autowired
    private ChainsConfig chainsConfig;

    private long beginNumber=1;

    private Web3j web3j;

    @Autowired
    private NodeRankingMapper nodeRankingMapper;

    @Autowired
    private RedisCacheService redisCacheService;

    @Autowired
    private AnalyseFlow analyseFlow;

    @PostConstruct
    public void init () {
        BlockExample condition = new BlockExample();
        condition.createCriteria().andChainIdEqualTo(chainId);
        condition.setOrderByClause("number desc");
        PageHelper.startPage(1, 1);
        List <Block> blocks = blockMapper.selectByExample(condition);
        // 1、首先从数据库查询当前链的最高块号，作为采集起始块号
        // 2、如果查询不到则从0开始
        if (blocks.size() == 0) {
            beginNumber = 1L;
        } else {
            beginNumber = blocks.get(0).getNumber()+1;
        }
        web3j = chainsConfig.getWeb3j(chainId);
    }

    @Scheduled(cron="0/1 * * * * ?")
    protected void doJob () {
        try {
            // 需要并发处理的区块数据
            List<EthBlock> concurrentBlocks = new ArrayList<>();
            BigInteger endNumber = web3j.ethBlockNumber().send().getBlockNumber();
            while (beginNumber<=endNumber.longValue()){
                long startTime = System.currentTimeMillis();
                EthBlock ethBlock = web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(BigInteger.valueOf(beginNumber)),true).send();
                logger.debug("RPC web3j.ethGetBlockByNumber()--->{}",System.currentTimeMillis()-startTime);

                concurrentBlocks.add(ethBlock);
                if(concurrentBlocks.size()>=threadBatchSize){
                    analyseFlow.analyse(concurrentBlocks);
                    concurrentBlocks.clear();
                }

                beginNumber++;
            }
            try {
                TimeUnit.MICROSECONDS.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}