package com.platon.browser.persistence.queue.publisher;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorThreeArg;
import com.lmax.disruptor.EventTranslatorTwoArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.persistence.queue.event.PersistenceEvent;
import com.platon.browser.persistence.queue.handler.PersistenceEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ThreadFactory;

/**
 * 区块事件生产者
 */
@Slf4j
@Component
public class PersistenceEventPublisher {
    private static final EventTranslatorThreeArg<PersistenceEvent, Block, List<Transaction>,List<NodeOpt>>
    TRANSLATOR = (event, sequence, block,transactions,nodeOpts)->event.setBlock(block).setTransactions(transactions).setNodeOpts(nodeOpts);
    private RingBuffer<PersistenceEvent> ringBuffer;
    // 指定环形队列大小,必须是2的指数倍
    @Value("${disruptor.queue.persistence.buffer-size}")
    private int ringBufferSize;
    private EventFactory<PersistenceEvent> eventFactory = () -> PersistenceEvent.builder().build();
    @Autowired
    private PersistenceEventHandler handler;
    // 事件处理线程生产工厂
    ThreadFactory consumeThreadFactory = Thread::new;

    @PostConstruct
    private void init(){
        Disruptor<PersistenceEvent> disruptor = new Disruptor<>(eventFactory, ringBufferSize, consumeThreadFactory);
        // 设置事件处理器
        disruptor.handleEventsWith(handler);
        // 启动Disruptor,让所有生产和消费线程运行
        disruptor.start();
        // 获取环形缓冲引用，用于发布事件
        ringBuffer = disruptor.getRingBuffer();
    }

    public void publish(Block block, List<Transaction> transactions, List<NodeOpt> nodeOpts){
        ringBuffer.publishEvent(TRANSLATOR, block,transactions,nodeOpts);
    }
}
