package com.platon.browser.common.queue.complement.publisher;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorThreeArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.platon.browser.common.queue.complement.event.ComplementEvent;
import com.platon.browser.common.queue.complement.handler.IComplementEventHandler;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 补充事件发布者
 */
@Slf4j
@Component
public class ComplementEventPublisher {
    private static final EventTranslatorThreeArg<ComplementEvent, Block,List<Transaction>,List<NodeOpt>>
    TRANSLATOR = (event, sequence, block,transactions,nodeOpts)->event
            .setBlock(block)
            .setTransactions(transactions)
            .setNodeOpts(nodeOpts);
    private RingBuffer<ComplementEvent> ringBuffer;
    // 指定环形队列大小,必须是2的指数倍
    @Value("${disruptor.queue.complement.buffer-size}")
    private int ringBufferSize;
    private EventFactory<ComplementEvent> eventFactory = () -> ComplementEvent.builder().build();
    @Autowired
    private IComplementEventHandler handler;

    @PostConstruct
    private void init(){
        Disruptor<ComplementEvent> disruptor = new Disruptor<>(eventFactory, ringBufferSize, DaemonThreadFactory.INSTANCE);
        // 设置事件处理器
        disruptor.handleEventsWith(handler);
        // 启动Disruptor,让所有生产和消费线程运行
        disruptor.start();
        // 获取环形缓冲引用，用于发布事件
        ringBuffer = disruptor.getRingBuffer();
    }

    public void publish(Block block, List<Transaction> transactions, List<NodeOpt> nodeOpts) {
        ringBuffer.publishEvent(TRANSLATOR, block,transactions,nodeOpts);
    }
}
