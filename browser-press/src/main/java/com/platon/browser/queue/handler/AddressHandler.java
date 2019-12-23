package com.platon.browser.queue.handler;

import com.lmax.disruptor.EventHandler;
import com.platon.browser.dao.entity.Address;
import com.platon.browser.dao.mapper.AddressMapper;
import com.platon.browser.queue.event.AddressEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Auther: dongqile
 * @Date: 2019/12/23
 * @Description:
 */
@Slf4j
@Component
public class AddressHandler  implements EventHandler <AddressEvent> {

    @Autowired
    private AddressMapper addressMapper;

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void onEvent ( AddressEvent event, long sequence, boolean endOfBatch ) throws Exception {
        List <Address> addresses = event.getAddressList();
        addresses.forEach(address -> {
            address.setBalance(BigDecimal.ZERO);
            address.setCandidateCount(new Integer(0));
            address.setCreateTime(new Date());
            address.setDelegateHes(BigDecimal.ZERO);
            address.setDelegateLocked(BigDecimal.ZERO);
            address.setDelegateQty(new Integer(0));
            address.setDelegateReleased(BigDecimal.ZERO);
            address.setDelegateValue(BigDecimal.ZERO);
            address.setProposalQty(new Integer(0));
            address.setRedeemedValue(BigDecimal.ZERO);
            address.setRestrictingBalance(BigDecimal.ZERO);
            address.setStakingQty(new Integer(0));
            address.setStakingValue(BigDecimal.ZERO);
            address.setTransferQty(new Integer(0));
            address.setTxQty(new Integer(0));
            address.setUpdateTime(new Date());
            address.setType(1);
        });
        addressMapper.batchInsert(addresses);
    }
}