package com.platon.browser.collection.producer;//package com.platon.browser.refactor.queue.producer;
//
//import com.lmax.disruptor.RingBuffer;
//import com.platon.browser.client.result.ReceiptResult;
//import com.platon.browser.refactor.queue.callback.ConsumeCallback;
//import com.platon.browser.refactor.queue.event.CollectionBlockEvent;
//import org.web3j.protocol.core.methods.response.PlatonBlock;
//
//import java.util.concurrent.CompletableFuture;
//
///**
// * 搜集区块事件生产者
// */
//public class CollectionBlockEventProducer {
//    private final RingBuffer<CollectionBlockEvent> ringBuffer;
//    public CollectionBlockEventProducer(RingBuffer<CollectionBlockEvent> ringBuffer) {
//        this.ringBuffer = ringBuffer;
//    }
//
//    /**
//     * onData用来发布事件，每调用一次就发布一次事件
//     * 它的参数会通过事件传递给消费者
//     */
//    public void onData(
//            CompletableFuture<PlatonBlock> blockCF,
//            ConsumeCallback<PlatonBlock> blockCB,
//            CompletableFuture<ReceiptResult> receiptCF,
//            ConsumeCallback<ReceiptResult> receiptCB
//    ) {
//        // 取下一事件槽
//        long sequence = ringBuffer.next();
//        try {
//            CollectionBlockEvent event = ringBuffer.get(sequence);
//            event.getBody().setBlockCF(blockCF);
//            event.getBody().setBlockCB(blockCB);
//            event.getBody().setReceiptCF(receiptCF);
//            event.getBody().setReceiptCB(receiptCB);
//        } finally {
//            ringBuffer.publish(sequence);
//        }
//    }
//}