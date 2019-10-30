//package com.platon.browser.collection.producer;//package com.platon.browser.refactor.queue.producer;
//
//import com.lmax.disruptor.RingBuffer;
//import com.platon.browser.queue.event.collection.CollectionBlockEvent;
//import com.platon.browser.queue.event.collection.CollectionBlockEventBody;
//
///**
// * 搜集区块事件生产者
// */
//public class CollectionBlockEventProducer implements EventProducer {
//    private final RingBuffer<CollectionBlockEvent> ringBuffer;
//    public CollectionBlockEventProducer(RingBuffer<CollectionBlockEvent> ringBuffer) {
//        this.ringBuffer = ringBuffer;
//    }
//
//    /**
//     * 发布事件，每调用一次就发布一次事件
//     * 它的参数会通过事件传递给消费者
//     */
//    @Override
//    public void publish(CollectionBlockEventBody eventBody) {
//        // 取下一事件槽
//        long sequence = ringBuffer.next();
//        try {
//            CollectionBlockEvent event = ringBuffer.get(sequence);
//            event.setBody(eventBody);
//        } finally {
//            ringBuffer.publish(sequence);
//        }
//    }
//}