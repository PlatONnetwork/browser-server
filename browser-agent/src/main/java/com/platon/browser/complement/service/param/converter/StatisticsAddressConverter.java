package com.platon.browser.complement.service.param.converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.platon.browser.dao.entity.Address;
import com.platon.browser.dao.entity.AddressExample;
import com.platon.browser.dao.mapper.AddressMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platon.browser.common.collection.dto.CollectionBlock;
import com.platon.browser.common.collection.dto.EpochMessage;
import com.platon.browser.common.complement.cache.AddressCache;
import com.platon.browser.common.complement.param.statistic.AddressStatChange;
import com.platon.browser.common.complement.param.statistic.AddressStatItem;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.mapper.StatisticBusinessMapper;

@Service
public class StatisticsAddressConverter {
	
    @Autowired
    private AddressCache addressCache;
    @Autowired
    private StatisticBusinessMapper statisticBusinessMapper;
    @Autowired
	private AddressMapper addressMapper;
    
	
    public void convert(CollectionEvent event,CollectionBlock block, EpochMessage epochMessage) throws Exception {
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
            			.build();
            	})
            	.collect(Collectors.toList());

		List<String> modifyAddList = new ArrayList <>();
		addressStatItemList.forEach(addressStatItem -> modifyAddList.add(addressStatItem.getAddress()));
		addressCache.cleanAll();
		AddressExample addressExample = new AddressExample();
		addressExample.createCriteria().andAddressIn(modifyAddList);
		List<Address> addressList = addressMapper.selectByExample(addressExample);

		Map <String,Address> dbMap = new HashMap <>();
		addressList.forEach(address -> {
			dbMap.put(address.getAddress(),address);
		});
		addressStatItemList.forEach(address -> {
			Address dbAddres =  dbMap.get(address.getAddress());
			if(null != dbAddres){
				address.setTxQty(dbAddres.getTxQty() + address.getTxQty());
				address.setTransferQty(dbAddres.getTransferQty() + address.getTransferQty());
				address.setDelegateQty(dbAddres.getDelegateQty() + address.getDelegateQty());
				address.setStakingQty(dbAddres.getStakingQty() + address.getStakingQty());
				address.setProposalQty(dbAddres.getProposalQty() + address.getProposalQty());
			}
		});


        AddressStatChange addressStatChange = AddressStatChange.builder()
        		.addressStatItemList(addressStatItemList)
        		.build();
        statisticBusinessMapper.addressChange(addressStatChange);
    }
}
