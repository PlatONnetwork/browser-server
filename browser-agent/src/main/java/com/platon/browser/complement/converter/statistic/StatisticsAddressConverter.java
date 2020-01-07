package com.platon.browser.complement.converter.statistic;

import com.platon.browser.common.collection.dto.EpochMessage;
import com.platon.browser.common.complement.cache.AddressCache;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.dao.mapper.StatisticBusinessMapper;
import com.platon.browser.complement.dao.param.statistic.AddressStatChange;
import com.platon.browser.complement.dao.param.statistic.AddressStatItem;
import com.platon.browser.dao.entity.Address;
import com.platon.browser.dao.entity.AddressExample;
import com.platon.browser.dao.mapper.AddressMapper;
import com.platon.browser.elasticsearch.dto.Block;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@Service
public class StatisticsAddressConverter {
	
    @Autowired
    private AddressCache addressCache;
    @Autowired
    private StatisticBusinessMapper statisticBusinessMapper;
    @Autowired
	private AddressMapper addressMapper;
    
	
    public void convert(CollectionEvent event, Block block, EpochMessage epochMessage){
		long startTime = System.currentTimeMillis();
		log.debug("block({}),transactions({}),consensus({}),settlement({}),issue({})",block.getNum(),event.getTransactions().size(),epochMessage.getConsensusEpochRound(),epochMessage.getSettleEpochRound(),epochMessage.getIssueEpochRound());
		// 使用缓存中的地址统计信息构造入库参数列表
		List<AddressStatItem> itemFromCache = new ArrayList<>();
		List<String> addresses = new ArrayList <>();
		addressCache.getAll().forEach(cache->{
			AddressStatItem item = AddressStatItem.builder()
					.address(cache.getAddress())
					.type(cache.getType())
					.txQty(cache.getTxQty())
					.transferQty(cache.getTransferQty())
					.delegateQty(cache.getDelegateQty())
					.stakingQty(cache.getStakingQty())
					.proposalQty(cache.getProposalQty())
					.contractName(cache.getContractName())
					.contractCreate(cache.getContractCreate())
					.contractCreatehash(cache.getContractCreatehash())
					.haveReward(cache.getHaveReward())
					.build();
			itemFromCache.add(item);
			addresses.add(cache.getAddress());
		});
		// 清空地址缓存
		addressCache.cleanAll();
		// 从数据库中查询出与缓存中对应的地址信息
		AddressExample condition = new AddressExample();
		condition.createCriteria().andAddressIn(addresses);
		List<Address> itemFromDb = addressMapper.selectByExample(condition);
		// 地址与详情进行映射
		Map <String,Address> dbMap = new HashMap <>();
		itemFromDb.forEach(item -> dbMap.put(item.getAddress(),item));
		// 合并数据库与缓存中的值
		itemFromCache.forEach(fromCache -> {
			Address fromDb =  dbMap.get(fromCache.getAddress());
			if(null != fromDb){
				fromCache.setTxQty(fromDb.getTxQty() + fromCache.getTxQty()); // 交易数量
				fromCache.setTransferQty(fromDb.getTransferQty() + fromCache.getTransferQty()); // 转账数量
				fromCache.setDelegateQty(fromDb.getDelegateQty() + fromCache.getDelegateQty()); // 委托数量
				fromCache.setStakingQty(fromDb.getStakingQty() + fromCache.getStakingQty()); // 质押数量
				fromCache.setProposalQty(fromDb.getProposalQty() + fromCache.getProposalQty()); // 提案数量
				fromCache.setHaveReward(fromDb.getHaveReward().add(fromCache.getHaveReward())); // 已领取委托奖励总额
			}
		});
		// 使用合并后的信息构造地址入库参数
        AddressStatChange addressStatChange = AddressStatChange.builder()
        		.addressStatItemList(itemFromCache)
        		.build();
        statisticBusinessMapper.addressChange(addressStatChange);

		log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);
    }
}
