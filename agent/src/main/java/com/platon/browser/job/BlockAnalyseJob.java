package com.platon.browser.job;

import com.github.pagehelper.PageHelper;
import com.platon.browser.config.ChainsConfig;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.BlockExample;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.thread.AnalyseThread;
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

/**
 * User: dongqile
 * Date: 2018/10/24
 * Time: 17:28
 */
@Component
public class BlockAnalyseJob {

    private static Logger logger = LoggerFactory.getLogger(BlockAnalyseJob.class);

    @Autowired
    private BlockMapper blockMapper;

    @Value("${chain.id}")
    private String chainId;
    @Value("${platon.thread.batch.size}")
    private int threadBatchSize;

    @Autowired
    private ChainsConfig chainsConfig;

    // 起始区块号
    private long beginNumber=1;

    private Web3j web3j;

    @Autowired
    private AnalyseThread analyseThread;

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

    /**
     * 分析区块及其内部的交易数据
     */
    @Scheduled(cron="0/1 * * * * ?")
    protected void analyseBlock () {
        logger.debug("*** In the BlockAnalyseJob *** ");
        try {
            // 需要并发处理的区块数据
            List<EthBlock> concurrentBlocks = new ArrayList<>();
            // 结束区块号
            BigInteger endNumber = web3j.ethBlockNumber().send().getBlockNumber();
            while (beginNumber<=endNumber.longValue()){
                EthBlock ethBlock = web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(BigInteger.valueOf(beginNumber)),true).send();
                concurrentBlocks.add(ethBlock);
                if(
                    (concurrentBlocks.size()>=threadBatchSize) || // 如果并发区块数量达到线程处理阈值，开启线程处理
                    ((endNumber.longValue()-beginNumber)<threadBatchSize) // 结束区块号与起始区块号之差小于2，也进入线程批量处理,防止追上后响应过慢
                ){
                    analyseThread.analyse(concurrentBlocks);
                    concurrentBlocks.clear();
                }
                beginNumber++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.debug("*** End the BlockAnalyseJob *** ");
    }

}