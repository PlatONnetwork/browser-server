package com.platon.browser.publisher;

import com.lmax.disruptor.RingBuffer;
import com.platon.browser.config.DisruptorConfig;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * @description:
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-16 09:42:02
 **/
@Slf4j
public abstract class AbstractPublisher<T> {
    @Resource
    protected DisruptorConfig config;
    private static final Map<String,AbstractPublisher> PUBLISHER_MAP = new TreeMap<>();
    public void register(String name,AbstractPublisher<T> publisher){
        PUBLISHER_MAP.put(name,publisher);
    }
    public void unregister(String name){
        PUBLISHER_MAP.remove(name);
    }
    public static Map<String,AbstractPublisher> getPublisherMap(){
        return Collections.unmodifiableMap(PUBLISHER_MAP);
    }

    protected RingBuffer<T> ringBuffer;
    public abstract int getRingBufferSize();
    public String info(){
        return String.format("RingBufferSize(%s),RemainingCapacity(%s)",getRingBufferSize(),ringBuffer.remainingCapacity());
    }
}
