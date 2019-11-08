package com.platon.browser.complement.service.param.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.platon.browser.common.collection.dto.CollectionBlock;
import com.platon.browser.common.collection.dto.EpochMessage;
import com.platon.browser.common.complement.cache.AddressCache;
import com.platon.browser.common.complement.dto.statistic.AddressStatChange;
import com.platon.browser.common.complement.dto.statistic.AddressStatItem;
import com.platon.browser.common.queue.collection.event.CollectionEvent;

public class StatisticsAddressConverter {
	
    @Autowired
    private AddressCache addressCache;
    
	
    public AddressStatChange convert(CollectionEvent event,CollectionBlock block, EpochMessage epochMessage) throws Exception {
    	
        List<AddressStatItem> addressStatItemList =   addressCache.getAll()
            	.stream()
            	.map(address->{ return AddressStatItem.builder()
            			.address(address.getAddress())
            			.type(address.getType())
            			.txQty(address.getTxQty())
            			.transferQty(address.getTransferQty())
            			.delegateQty(address.getDelegateQty())
            			.stakingQty(address.getStakingQty())
            			.proposalQty(address.getProposalQty())
            			.contractName(address.getContractName())
            			.contractCreate(address.getContractCreate())
            			.contractCreatehash(address.getContractCreatehash())
            			.build();})
            	.collect(Collectors.toList());
            
        	addressCache.cleanAll();;
       
            AddressStatChange addressStatChange = AddressStatChange.builder()
        		.addressStatItemList(addressStatItemList)
        		.build();
        return addressStatChange;
    }
}
