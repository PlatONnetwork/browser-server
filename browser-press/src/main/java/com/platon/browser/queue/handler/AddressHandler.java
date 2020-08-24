package com.platon.browser.queue.handler;

import com.alibaba.fastjson.JSONObject;
import com.platon.browser.dao.entity.Address;
import com.platon.browser.dao.entity.AddressExample;
import com.platon.browser.dao.mapper.AddressMapper;
import com.platon.browser.queue.event.AddressEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Auther: dongqile
 * @Date: 2019/12/23
 * @Description:
 */
@Slf4j
@Component
public class AddressHandler extends AbstractHandler<AddressEvent> {

    @Autowired
    private AddressMapper addressMapper;
    @PostConstruct
    private void init(){this.setLogger(log);}

    @Setter
    @Getter
    @Value("${disruptor.queue.address.batch-size}")
    private volatile int batchSize;

    @Value("${platon.addressMaxCount}")
    private long addressMaxCount;

    private Set<Address> stage = new HashSet<>();

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void onEvent ( AddressEvent event, long sequence, boolean endOfBatch ) {
        long startTime = System.currentTimeMillis();
        stage.addAll(event.getAddressList());
        log.info("stat:{},batchSize:{},getTotalCount():{},addressMaxCount:{}",stage.size(),batchSize,getTotalCount(),addressMaxCount);
        if(stage.size()<batchSize){
            // 如果暂存数量小于批次
            if(getTotalCount()>addressMaxCount){
                // 且当前地址数未达到指定数量
                return;
            }
        }
        Map<String,Address> addressMap = new HashMap<>();
        stage.forEach(address -> addressMap.put(address.getAddress(),address));
        addressMap.values().forEach(address -> {
            address.setBalance(BigDecimal.ZERO);
            address.setCandidateCount(0);
            address.setCreateTime(new Date());
            address.setDelegateHes(BigDecimal.ZERO);
            address.setDelegateLocked(BigDecimal.ZERO);
            address.setDelegateQty(0);
            address.setDelegateReleased(BigDecimal.ZERO);
            address.setDelegateValue(BigDecimal.ZERO);
            address.setProposalQty(0);
            address.setRedeemedValue(BigDecimal.ZERO);
            address.setRestrictingBalance(BigDecimal.ZERO);
            address.setStakingQty(0);
            address.setStakingValue(BigDecimal.ZERO);
            address.setTransferQty(0);
            address.setTxQty(0);
            address.setUpdateTime(new Date());
            address.setType(1);
            address.setContractName("");
            address.setContractCreate("");
            address.setContractCreatehash("");
            address.setHaveReward(BigDecimal.ZERO);
        });

        AddressExample example = new AddressExample();
        example.createCriteria().andAddressIn(new ArrayList<>(addressMap.keySet()));
        List<Address> existList = addressMapper.selectByExample(example);
        List<String> existAddresses = new ArrayList<>();
        existList.forEach(address -> existAddresses.add(address.getAddress()));

        addressMap.keySet().removeAll(existAddresses);
        List<Address> addressList = new ArrayList<>(addressMap.values());

        try {
        	if(getTotalCount()<addressMaxCount){
                if(!addressList.isEmpty()) addressMapper.batchInsert(addressList);
                long endTime = System.currentTimeMillis();
                printTps("地址",addressList.size(),startTime,endTime);
            }
		} catch (Exception e) {
			log.error("insert address error", e);
		}
        
        stage.clear();
    }
}