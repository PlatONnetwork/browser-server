package com.platon.browser.queue.handler;

import cn.hutool.core.convert.Convert;
import com.platon.browser.dao.entity.Address;
import com.platon.browser.dao.entity.Delegation;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.queue.event.TransactionEvent;
import com.platon.browser.queue.publisher.AddressPublisher;
import com.platon.browser.queue.publisher.DelegationPublisher;
import com.platon.browser.service.BlockResult;
import com.platon.browser.service.DataGenService;
import com.platon.browser.service.elasticsearch.EsTransactionService;
import com.platon.browser.service.redis.RedisStatisticService;
import com.platon.browser.service.redis.RedisTransactionService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 区块事件处理器
 */
@Slf4j
@Component
public class TransactionHandler extends AbstractHandler<TransactionEvent> {

    @Autowired
    private DataGenService dataGenService;

    @Autowired
    private EsTransactionService esTransactionService;

    @Autowired
    private RedisTransactionService redisTransactionService;

    @Autowired
    private AddressPublisher addressPublisher;

    @Autowired
    private DelegationPublisher delegationPublisher;

    @Setter
    @Getter
    @Value("${disruptor.queue.transaction.batch-size}")
    private volatile int batchSize;

    @Autowired
    private RedisStatisticService redisStatisticService;

    private StageCache<Transaction> stage = new StageCache<>();

    @PostConstruct
    private void init() {
        stage.setBatchSize(batchSize);
        this.setLogger(log);
    }

    @Override
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void onEvent(TransactionEvent event, long sequence, boolean endOfBatch) throws IOException, InterruptedException {
        long startTime = System.currentTimeMillis();
        Set<Transaction> cache = stage.getData();
        try {
            cache.addAll(event.getTransactionList());
            if (cache.size() < batchSize) {
                return;
            }
            esTransactionService.save(stage);
            redisTransactionService.save(cache, false);
            long endTime = System.currentTimeMillis();
            printTps("交易", cache.size(), startTime, endTime);

            // 地址数量未达到指定数量，则继续入库
            List<Address> addressList = new ArrayList<>();
            List<Delegation> delegationList = new ArrayList<>();
            cache.forEach(tx -> {
                Address address = new Address();
                address.setAddress(tx.getFrom());
                address.setType(1);
                addressList.add(address);
                address = new Address();
                address.setAddress(tx.getTo());
                address.setType(1);
                addressList.add(address);
                if (tx.getType() == Transaction.TypeEnum.ERC20_CONTRACT_CREATE.getCode()) {
                    address = new Address();
                    address.setAddress(tx.getContractAddress());
                    address.setType(5);
                    addressList.add(address);
                }
                if (tx.getType() == Transaction.TypeEnum.ERC721_CONTRACT_CREATE.getCode()) {
                    address = new Address();
                    address.setAddress(tx.getContractAddress());
                    address.setType(6);
                    addressList.add(address);
                }

                if (
                        tx.getTypeEnum() == Transaction.TypeEnum.DELEGATE_CREATE ||
                                tx.getTypeEnum() == Transaction.TypeEnum.DELEGATE_EXIT
                ) {
                    Set<Integer> set = BlockResult.getNoRepetitionRandom(1, 1000, 4);
                    for (Integer nodeId : set) {
                        Delegation delegation = dataGenService.getDelegation(tx);
                        delegation.setNodeId(BlockResult.createNodeId(nodeId));
                        delegationList.add(delegation);
                    }
                }

            });
            dataGenService.getNetworkStat().setCurNumber(event.getBlockNum().longValue());
            dataGenService.getNetworkStat().setTxQty(Convert.toInt(this.getTotalCount()));
            redisStatisticService.save(Collections.singleton(dataGenService.getNetworkStat()), true);
            addressPublisher.publish(addressList);
            delegationPublisher.publish(delegationList);
            cache.clear();
        } catch (Exception e) {
            log.error("", e);
            throw e;
        }
    }

}