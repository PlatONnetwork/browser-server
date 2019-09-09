package com.platon.browser.task;

import com.platon.browser.bean.CollectResult;
import com.platon.browser.client.PlatonClient;
import com.platon.browser.dao.mapper.CustomBlockMapper;
import com.platon.browser.dto.CustomBlock;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.stage.BlockChainStage;
import com.platon.browser.exception.BlockCollectingException;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.service.BlockService;
import com.platon.browser.service.CandidateService;
import com.platon.browser.service.DbService;
import com.platon.browser.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/10 09:14
 * @Description: 区块和交易同步任务
 */
@Component
public class BlockSyncTask {
    private static Logger logger = LoggerFactory.getLogger(BlockSyncTask.class);
    public static ExecutorService THREAD_POOL;

    @Autowired
    private CustomBlockMapper customBlockMapper;
    @Autowired
    private DbService dbService;
    @Autowired
    private BlockChain blockChain;
    @Autowired
    private PlatonClient client;
    @Autowired
    private BlockService blockService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private CandidateService candidateService;

    // 已采集入库的最高块
    private long commitBlockNumber = 0;

    // 每一批次采集区块的数量
    @Value("${platon.web3j.collect.batch-size}")
    private int collectBatchSize;

    /**
     * 初始化已有业务数据
     */
    public void init () throws Exception {
        THREAD_POOL = Executors.newFixedThreadPool(collectBatchSize);
        blockService.init(THREAD_POOL);
        transactionService.init(THREAD_POOL);
        // 从数据库查询最高块号，赋值给commitBlockNumber
        Long maxBlockNumber = customBlockMapper.selectMaxBlockNumber();
        // 更新当前所在周期的区块奖励和结算周期质押奖励, 初始化共识验证人列表
        Long initBlockNumber = 1L;
        if (maxBlockNumber != null && maxBlockNumber > 0) {
            commitBlockNumber = maxBlockNumber;
            blockChain.updateReward(maxBlockNumber);
            initBlockNumber = maxBlockNumber;
        }else{
            blockChain.updateReward(0L);
        }
        candidateService.initValidator(initBlockNumber);
        candidateService.initVerifier(initBlockNumber);

        /*
         * 从第一块同步的时候，结算周期验证人和共识周期验证人是链上内置的
         * 查询内置共识周期验证人初始化blockChain的curValidator属性
         * 查询内置结算周期验证人初始化blockChain的curVerifier属性
          */
        if(maxBlockNumber==null){
            candidateService.init();
        }
    }

    public void start() throws Exception {
        while (true) {
            // 从(已采最高区块号+1)开始构造连续的指定数量的待采区块号列表
            Set<BigInteger> blockNumbers = new HashSet<>();
            // 当前链上最新区块号
            BigInteger curChainBlockNumber;
            try {
                curChainBlockNumber = client.getWeb3j().platonBlockNumber().send().getBlockNumber();
            } catch (IOException e) {
                throw new BlockCollectingException("取链上最新区块号失败:"+e.getMessage());
            }
            for (long blockNumber=commitBlockNumber+1; blockNumber<=(commitBlockNumber+collectBatchSize);blockNumber++) {
                // 如果块号>当前链上块号,则不再累加
                if(blockNumber>curChainBlockNumber.longValue()) break;
                blockNumbers.add(BigInteger.valueOf(blockNumber));
            }
            if(blockNumbers.size()==0){
                logger.info("当前链最高块({}),等待链出下一个块...",curChainBlockNumber);
                TimeUnit.SECONDS.sleep(1);
                continue;
            }
            // 并行采块 ξξξξξξξξξξξξξξξξξξξξξξξξξξξ
            // 采集前先重置结果容器
            CollectResult.reset();
            // 开始并行采集
            List<CustomBlock> blocks = blockService.collect(blockNumbers);
            // 采集不到区块则暂停1秒, 结束本次循环
            if(blocks.size()==0) {
                TimeUnit.SECONDS.sleep(1);
                continue;
            }
            // 并行分析 ξξξξξξξξξξξξξξξξξξξξξξξξξξξ
            transactionService.analyze(blocks);
            // 调用BlockChain实例, 串行分析每个区块，获取质押、提案相关业务数据
            for (CustomBlock block:blocks) blockChain.execute(block);
            BlockChainStage bizData = blockChain.exportResult();
            try {
                // 入库失败，立即停止，防止采集后续更高的区块号，导致不连续区块号出现
                dbService.batchSave(blocks, bizData);
            } catch (BusinessException e) {
                break;
            }
            // 记录已采入库最高区块号
            commitBlockNumber = blocks.get(blocks.size() - 1).getNumber();
            TimeUnit.SECONDS.sleep(1);
        }
    }
}
