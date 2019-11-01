package com.platon.browser.client.result;

import com.platon.browser.utils.HexTool;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Data
public class ReceiptResult {
    private String jsonrpc;
    private int id;
    private List<Receipt> result;

    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(100);

    private Map<String,Receipt> map = new ConcurrentHashMap<>();

    /**
     * 并行解码Logs
     */
    public void resolve() throws InterruptedException {
        if(result.isEmpty()) return;
        CountDownLatch latch = new CountDownLatch(result.size());

        result.forEach(r->{
            map.put(HexTool.prefix(r.getTransactionHash()),r);
            EXECUTOR.submit(()->{
                try {
                    r.decodeLogs();
                }finally {
                    latch.countDown();
                }
            });
        });

        latch.await();
    }
}