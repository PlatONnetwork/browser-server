package com.platon.browser.bean;

import com.platon.protocol.core.Response;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class ReceiptResult extends Response<List<Receipt>> {
    //private Map<String,Receipt> map = new ConcurrentHashMap<>();


    /**
     * 把区块所有的交易receipt转成map,txHash为key
     */
    public Map<String, Receipt> getReceiptMap() {
        return getResult().stream().collect(Collectors.toMap(Receipt::getTransactionHash, receipt -> receipt));
    }

    /**
     * 并行解码Logs
     * //2023-08-31，不需要在这里解码log，因为根本不着调如何解码。
     */
    /*public void resolve(Long blockNumber, ExecutorService threadPool) throws InterruptedException {
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
*/

}
