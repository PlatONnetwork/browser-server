package com.platon.browser.client;

import com.platon.browser.utils.HexTool;
import lombok.Data;
import org.web3j.protocol.core.Response;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import static com.platon.browser.client.PlatOnClient.LOG_DECODE_EXECUTOR;

@Data
public class ReceiptResult extends Response<List<Receipt>> {
    private Map<String,Receipt> map = new ConcurrentHashMap<>();

    /**
     * 并行解码Logs
     */
    public void resolve(Long blockNumber) throws InterruptedException {
        if(getResult().isEmpty()) return;
        CountDownLatch latch = new CountDownLatch(getResult().size());
        getResult().forEach(r->{
            map.put(HexTool.prefix(r.getTransactionHash()),r);
            LOG_DECODE_EXECUTOR.submit(()->{
                try {
                    r.setBlockNumber(blockNumber);
                    r.decodeLogs();
                }finally {
                    latch.countDown();
                }
            });
        });
        latch.await();
    }
}