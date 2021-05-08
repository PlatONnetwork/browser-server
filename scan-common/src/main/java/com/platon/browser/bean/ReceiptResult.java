package com.platon.browser.bean;

import com.platon.browser.utils.HexUtil;
import com.platon.protocol.core.Response;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

@Data
public class ReceiptResult extends Response<List<Receipt>> {
    private Map<String,Receipt> map = new ConcurrentHashMap<>();

    /**
     * 并行解码Logs
     */
    public void resolve(Long blockNumber, ExecutorService threadPool) throws InterruptedException {
        if(getResult().isEmpty()) return;
        CountDownLatch latch = new CountDownLatch(getResult().size());
        getResult().forEach(receipt->{
            map.put(HexUtil.prefix(receipt.getTransactionHash()),receipt);
            threadPool.submit(()->{
                try {
                    receipt.setBlockNumber(blockNumber);
                    receipt.decodeLogs();
                }finally {
                    latch.countDown();
                }
            });
        });
        latch.await();
    }


}