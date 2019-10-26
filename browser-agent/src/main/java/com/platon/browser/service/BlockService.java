package com.platon.browser.service;

import com.platon.browser.client.PlatOnClient;
import com.platon.browser.dao.entity.Block;
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
import java.util.*;
import java.util.concurrent.*;

/**
 * @Auther: Chendongming
 * @Date: 2019/9/6 13:34
 * @Description: 区块采集服务
 */
@Service
public class BlockService {
    private static Logger logger = LoggerFactory.getLogger(BlockService.class);
    @Autowired
    private PlatOnClient client;
    private ExecutorService executor;

    // 并发采集的块信息，无序
    private static final Map<Long, CustomBlock> BLOCK_MAP = new ConcurrentHashMap<>();
    // 由于异常而未采集的区块号列表
    private static final Set<BigInteger> RETRY_NUMBER = new CopyOnWriteArraySet<>();
    List <CustomBlock> getSortedBlocks() {
        List<CustomBlock> sortedBlock = new LinkedList<>(BLOCK_MAP.values());
        sortedBlock.sort(Comparator.comparing(Block::getNumber));
        return sortedBlock;
    }
    private void reset() {
        BLOCK_MAP.clear();
        RETRY_NUMBER.clear();
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
        reset();
        // 把待采块放入重试列表，当作重试块号操作
        RETRY_NUMBER.addAll(blockNumbers);
        // 记录每次重试出异常的块号，方便放入下次重试
        Set<BigInteger> exceptionNumbers = new CopyOnWriteArraySet<>();
        while (!RETRY_NUMBER.isEmpty()){
            exceptionNumbers.clear();
            // 并行批量采集区块
            CountDownLatch latch = new CountDownLatch(RETRY_NUMBER.size());
            RETRY_NUMBER.forEach(blockNumber->
                executor.submit(()->{
                    try {
                        CustomBlock block = getBlock(blockNumber);
                        BLOCK_MAP.put(blockNumber.longValue(),block);
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
            RETRY_NUMBER.clear();
            // 把本轮异常区块号加入重试列表
            RETRY_NUMBER.addAll(exceptionNumbers);
        }
        return getSortedBlocks();
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
            block.updateWithBlock(pbb);
            return block;
        } catch (Exception e) {
            logger.error("搜集区块[{}]异常,将重试:", blockNumber, e);
            client.updateCurrentValidWeb3j();
            try {
                TimeUnit.SECONDS.sleep(1L);
            } catch (Exception ex) {
                logger.error("",ex);
            }
        }
    }

    /**
     * 取链上当前块号
     * @return
     */
    public BigInteger getLatestNumber() {
        while (true) try {
            return client.getWeb3j().platonBlockNumber().send().getBlockNumber();
        } catch (Exception e) {
            logger.error("取链上最新区块号失败,将重试{}:", e.getMessage());
            client.updateCurrentValidWeb3j();
            try {
                TimeUnit.SECONDS.sleep(1L);
            } catch (Exception ex) {
                logger.error("",ex);
            }
        }
    }
}
