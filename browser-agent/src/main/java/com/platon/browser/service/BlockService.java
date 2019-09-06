package com.platon.browser.service;

import com.platon.browser.bean.CollectResult;
import com.platon.browser.client.PlatonClient;
import com.platon.browser.dto.CustomBlock;
import com.platon.browser.exception.BlockCollectingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.PlatonBlock;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import static com.platon.browser.task.BlockSyncTask.THREAD_POOL;

/**
 * @Auther: Chendongming
 * @Date: 2019/9/6 13:34
 * @Description:
 */
@Service
public class BlockService {
    private static Logger logger = LoggerFactory.getLogger(BlockService.class);

    @Autowired
    private PlatonClient client;
    /**
     * 并行采集区块及交易，并转换为数据库结构
     *
     * @param blockNumbers 批量采集的区块号
     * @return void
     */
    public void collect(Set<BigInteger> blockNumbers) throws BlockCollectingException {
        // 清空重试区块号列表
        CollectResult.RETRY_NUMBERS.clear();
        // 并行批量采集区块
        CountDownLatch latch = new CountDownLatch(blockNumbers.size());
        blockNumbers.forEach(blockNumber->
                THREAD_POOL.submit(()->{
                    try {
                        Web3j web3j = client.getWeb3j();
                        PlatonBlock.Block initData = web3j.platonGetBlockByNumber(DefaultBlockParameter.valueOf(blockNumber),true).send().getBlock();
                        if(initData!=null) {
                            try{
                                CustomBlock block = new CustomBlock();
                                block.updateWithBlock(initData);
                                CollectResult.CONCURRENT_BLOCK_MAP.put(blockNumber.longValue(),block);
                            }catch (Exception ex){
                                logger.debug("初始化区块信息异常, 原因: {}", ex.getMessage());
                                throw ex;
                            }
                        }
                    } catch (Exception e) {
                        // 把出现异常的区块号加入重试列表
                        CollectResult.RETRY_NUMBERS.add(blockNumber);
                    }finally {
                        latch.countDown();
                    }
                })
        );

        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new BlockCollectingException("区块采集线程被中断:"+e.getMessage());
        }

        if (CollectResult.CONCURRENT_BLOCK_MAP.size() > 0) {
            // 查看已采块是否连续，把缺失的区块号放入重试列表
            Set<Long> collected = CollectResult.CONCURRENT_BLOCK_MAP.keySet();
            long start = Collections.min(collected), end = Collections.max(collected);
            for (long i = start; i < end; i++) {
                if (!collected.contains(i)) CollectResult.RETRY_NUMBERS.add(BigInteger.valueOf(i));
            }
        }

        if (CollectResult.RETRY_NUMBERS.size() > 0) {
            logger.debug("区块重试列表：{}", CollectResult.RETRY_NUMBERS);
            collect(CollectResult.RETRY_NUMBERS);
        }
    }

}
