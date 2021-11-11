package com.platon.browser.handler;

import cn.hutool.json.JSONUtil;
import com.lmax.disruptor.EventHandler;
import com.platon.browser.analyzer.BlockAnalyzer;
import com.platon.browser.bean.BlockEvent;
import com.platon.browser.bean.CollectionBlock;
import com.platon.browser.bean.ReceiptResult;
import com.platon.browser.exception.BeanCreateOrUpdateException;
import com.platon.browser.exception.BlankResponseException;
import com.platon.browser.exception.ContractInvokeException;
import com.platon.browser.publisher.CollectionEventPublisher;
import com.platon.browser.utils.CommonUtil;
import com.platon.protocol.core.methods.response.PlatonBlock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 区块事件处理器
 */
@Slf4j
@Component
public class BlockEventHandler implements EventHandler<BlockEvent> {

    @Resource
    private CollectionEventPublisher collectionEventPublisher;

    @Resource
    private BlockAnalyzer blockAnalyzer;

    /**
     * 重试次数
     */
    private AtomicLong retryCount = new AtomicLong(0);

    @Override
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, label = "BlockEventHandler")
    public void onEvent(BlockEvent event, long sequence, boolean endOfBatch) throws ExecutionException, InterruptedException, BeanCreateOrUpdateException, IOException, ContractInvokeException, BlankResponseException {
        surroundExec(event, sequence, endOfBatch);
    }

    private void surroundExec(BlockEvent event, long sequence, boolean endOfBatch) throws InterruptedException, ExecutionException, ContractInvokeException, BeanCreateOrUpdateException, BlankResponseException {
        CommonUtil.putTraceId(event.getTraceId());
        long startTime = System.currentTimeMillis();
        exec(event, sequence, endOfBatch);
        log.debug("处理耗时:{} ms", System.currentTimeMillis() - startTime);
        CommonUtil.removeTraceId();
    }

    private void exec(BlockEvent event, long sequence, boolean endOfBatch) throws InterruptedException, ExecutionException, BlankResponseException, BeanCreateOrUpdateException, ContractInvokeException {
        try {
            PlatonBlock.Block rawBlock = event.getBlockCF().get().getBlock();
            if (retryCount.incrementAndGet() > 1) {
                log.error("重试次数[{}],该区块[{}]重复处理，可能会引起数据重复统计，event对象数据为[{}]", retryCount.get(), rawBlock.getNumber(), JSONUtil.toJsonStr(event));
            }
            ReceiptResult receiptResult = event.getReceiptCF().get();
            log.info("当前区块[{}]有[{}]笔交易", rawBlock.getNumber(), CommonUtil.ofNullable(() -> rawBlock.getTransactions().size()).orElse(0));
            // 分析区块
            CollectionBlock block = blockAnalyzer.analyze(rawBlock, receiptResult);
            block.setReward(event.getEpochMessage().getBlockReward().toString());
            collectionEventPublisher.publish(block, block.getTransactions(), event.getEpochMessage(), event.getTraceId());
            // 释放对象引用
            event.releaseRef();
            retryCount.set(0);
        } catch (Exception e) {
            log.error("区块事件处理异常", e);
            throw e;
        }
    }

}