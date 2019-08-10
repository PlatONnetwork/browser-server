package com.platon.browser.task;

import com.alibaba.fastjson.JSON;
import com.platon.browser.client.PlatonClient;
import com.platon.browser.dao.entity.Proposal;
import com.platon.browser.dao.entity.Vote;
import com.platon.browser.dto.BlockInfo;
import com.platon.browser.engine.BlockChain;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.PlatonBlock;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.*;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/10 09:14
 * @Description:
 */
@Component
public class BlockSyncTask {
    private static Logger logger = LoggerFactory.getLogger(BlockSyncTask.class);

    private static ExecutorService THREAD_POOL;

    @Autowired
    private BlockChain blockChain;

    @Autowired
    private PlatonClient client;

    // 已采集入库的最高块
    private long commitBlockNumber=0;

    // 每一批次采集区块的数量
    @Value("${platon.web3j.collect.batch-size}")
    private int collectBatchSize;

    /**
     * 初始化已有业务数据
     */
    @PostConstruct
    public void init(){
        THREAD_POOL = Executors.newFixedThreadPool(collectBatchSize);
        // 从数据库查询最高块号，赋值给commitBlockNumber
    }

    public void start() throws InterruptedException {

        while (true){

            // 构造连续的待采区块号列表
            List<BigInteger> blockNumbers = new ArrayList<>();
            for (long blockNumber=(commitBlockNumber+1);blockNumber<=(commitBlockNumber+collectBatchSize);blockNumber++){
                blockNumbers.add(BigInteger.valueOf(blockNumber));
            }

            // 并行采集区块
            List<BlockInfo> blocks = getBlockAndTransaction(blockNumbers);
            // 对区块和交易做分析
            analyzeBlockAndTransaction(blocks);

            // 调用BlockChain实例，分析质押、提案相关业务数据
            List<Vote> addVotes = new ArrayList<>();
            Set<Proposal> addProposals = new HashSet<>();
            Set<Proposal> updateProposals = new HashSet<>();
            blocks.forEach(block->{

                blockChain.execute(block);


            });


            commitBlockNumber=commitBlockNumber+collectBatchSize;
            TimeUnit.MILLISECONDS.sleep(500);
        }

    }

    /**
     * 并行采集区块及交易，并转换为数据库结构
     * @param blockNumbers 批量采集的区块号
     * @return
     */
    private List<BlockInfo> getBlockAndTransaction(List<BigInteger> blockNumbers){
        @Data
        class Error{
            public Error(BigInteger blockNumber,String msg){
                this.blockNumber=blockNumber;
                this.msg=msg;
            }
            BigInteger blockNumber;
            String msg;
        }
        @Data
        class Result{
            List<BlockInfo> blocks = new CopyOnWriteArrayList<>();
            List<Error> errors = new CopyOnWriteArrayList<>();
        }
        Result result = new Result();

        // 并行批量采集区块
        CountDownLatch latch = new CountDownLatch(blockNumbers.size());
        blockNumbers.forEach(blockNumber->
            THREAD_POOL.submit(()->{
                try {
                    PlatonBlock.Block initData = client.getWeb3j().platonGetBlockByNumber(DefaultBlockParameter.valueOf(blockNumber),true).send().getBlock();
                    result.blocks.add(new BlockInfo(initData));
                } catch (Exception e) {
                    Error error = new Error(blockNumber,e.getMessage());
                    result.errors.add(error);
                    logger.error("采集区块[{}]出错!{}",blockNumber,e.getMessage());
                }finally {
                    latch.countDown();
                }
            })
        );

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(result.errors.size()>0){
            // 采集出错，终止程序
            throw new RuntimeException("区块采集出错："+JSON.toJSONString(result.errors));
        }

        // 按区块号从大到小排序
        Collections.sort(result.blocks,(c1,c2)->{
            if (c1.getNumber().compareTo(c2.getNumber())>0) return 1;
            if (c1.getNumber().compareTo(c2.getNumber())<0) return -1;
            return 0;
        });

        return result.blocks;

    }

    /**
     * 并行分析区块
     */
    private void analyzeBlockAndTransaction(List<BlockInfo> blocks){

        // 对需要复杂分析的区块或交易信息，开启并行处理

    }

    private void batchSaveResult(){
        // 串行批量入库

    }
}
