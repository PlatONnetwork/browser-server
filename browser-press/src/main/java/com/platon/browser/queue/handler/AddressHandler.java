package com.platon.browser.queue.handler;

import com.lmax.disruptor.EventHandler;
import com.platon.browser.dao.entity.Address;
import com.platon.browser.dao.mapper.AddressMapper;
import com.platon.browser.queue.event.AddressEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AddressHandler extends AbstractHandler implements EventHandler <AddressEvent> {

    @Autowired
    private AddressMapper addressMapper;
    @PostConstruct
    private void init(){this.setLogger(log);}

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void onEvent ( AddressEvent event, long sequence, boolean endOfBatch ) {
        long startTime = System.currentTimeMillis();

        List <Address> addresses = event.getAddressList();
        Map<String,Address> addressMap = new HashMap<>();

        addresses.forEach(address -> addressMap.put(address.getAddress(),address));
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
        });

        List<Address> addressList = new ArrayList<>(addressMap.values());
        addressMapper.batchInsert(addressList);
        long endTime = System.currentTimeMillis();
        printTps("地址",addresses.size(),startTime,endTime);
    }
}