package com.platon.browser.service;

import com.platon.browser.client.PlatonClient;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dto.CustomBlock;
import com.platon.browser.exception.BlockCollectingException;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.PlatonBlock;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * @Auther: Chendongming
 * @Date: 2019/9/6 13:34
 * @Description: 区块采集服务
 */
@Service
public class BlockService {
    private static Logger logger = LoggerFactory.getLogger(BlockService.class);
    @Autowired
    private PlatonClient client;
    private ExecutorService executor;

    @Data
    static class CollectResult {
        // 并发采集的块信息，无序
        final static Map<Long, CustomBlock> CONCURRENT_BLOCK_MAP = new ConcurrentHashMap<>();
        // 由于异常而未采集的区块号列表
        final static Set<BigInteger> RETRY_NUMBERS = new CopyOnWriteArraySet<>();
        // 已排序的区块信息列表
        private final static List<CustomBlock> SORTED_BLOCKS = new LinkedList<>();
        static List <CustomBlock> getSortedBlocks() {
            SORTED_BLOCKS.clear();
            SORTED_BLOCKS.addAll(CONCURRENT_BLOCK_MAP.values());
            SORTED_BLOCKS.sort(Comparator.comparing(Block::getNumber));
            return SORTED_BLOCKS;
        }
        static void reset() {
            CONCURRENT_BLOCK_MAP.clear();
            RETRY_NUMBERS.clear();
            SORTED_BLOCKS.clear();
        }
    }

    public void init(ExecutorService executor){
        this.executor = executor;
    }

    /**
     * 并行采集区块及交易，并转换为数据库结构
     * @param blockNumbers 批量采集的区块号
     * @return void
     */
    public List<CustomBlock> collect(Set<BigInteger> blockNumbers) throws InterruptedException {
        CollectResult.reset();
        // 先重置重试列表
        CollectResult.RETRY_NUMBERS.clear();
        // 把待采块放入重试列表，当作重试块号操作
        CollectResult.RETRY_NUMBERS.addAll(blockNumbers);
        // 记录每次重试出异常的块号，方便放入下次重试
        Set<BigInteger> exceptionNumbers = new CopyOnWriteArraySet<>();
        while (!CollectResult.RETRY_NUMBERS.isEmpty()){
            exceptionNumbers.clear();
            // 并行批量采集区块
            CountDownLatch latch = new CountDownLatch(CollectResult.RETRY_NUMBERS.size());
            CollectResult.RETRY_NUMBERS.forEach(blockNumber->
                executor.submit(()->{
                    try {
                        CustomBlock block = getBlock(blockNumber);
                        CollectResult.CONCURRENT_BLOCK_MAP.put(blockNumber.longValue(),block);
                    } catch (Exception e) {
                        logger.error("搜集区块[{}]异常,加入重试列表",blockNumber,e);
                        // 把出现异常的区块号加入异常块号列表
                        exceptionNumbers.add(blockNumber);
                    }finally {
                        latch.countDown();
                    }
                })
            );
            latch.await();
            // 清空重试列表
            CollectResult.RETRY_NUMBERS.clear();
            // 把本轮异常区块号加入重试列表
            CollectResult.RETRY_NUMBERS.addAll(exceptionNumbers);
        }
        return CollectResult.getSortedBlocks();
    }

    /**
     * 调用RPC接口获取区块
     * @param blockNumber
     * @return
     */
    public CustomBlock getBlock(BigInteger blockNumber) {
        while (true) try {
            Web3j web3j = client.getWeb3j();
            PlatonBlock.Block pbb = web3j.platonGetBlockByNumber(DefaultBlockParameter.valueOf(blockNumber), true).send().getBlock();
            if (pbb == null) throw new BlockCollectingException("原生区块[" + blockNumber + "]为空！");
            CustomBlock block = new CustomBlock();
            try {
                block.updateWithBlock(pbb);
            } catch (Exception ex) {
                throw new BlockCollectingException("初始化区块信息异常:" + ex.getMessage());
            }
            return block;
        } catch (Exception e) {
            logger.error("搜集区块[{}]异常,将重试:", blockNumber, e);
        }
    }

    /**
     * 取链上当前块号
     * @return
     */
    public BigInteger getLatestNumber() {
        while (true) try {
            return client.getWeb3j().platonBlockNumber().send().getBlockNumber();
        } catch (IOException e) {
            logger.error("取链上最新区块号失败,将重试{}:", e.getMessage());
        }
    }
}
