package com.platon.browser.complement.service.param;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platon.browser.common.collection.dto.CollectionBlock;
import com.platon.browser.common.collection.dto.EpochMessage;
import com.platon.browser.common.complement.cache.AddressCache;
import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.common.complement.cache.NodeCache;
import com.platon.browser.common.complement.dto.BusinessParam;
import com.platon.browser.common.complement.dto.statistic.AddressStatChange;
import com.platon.browser.common.complement.dto.statistic.AddressStatItem;
import com.platon.browser.common.complement.dto.statistic.NetworkStatChange;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.common.utils.CalculateUtils;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.NetworkStat;

/**
 * 统计入库参数服务 
 * @author chendai
 */
@Service
public class StatisticParameterService {

    @Autowired
    private BlockChainConfig chainConfig;
    
    @Autowired
    private NodeCache nodeCache;
    
    @Autowired
    private NetworkStatCache networkStatCache;
    
    @Autowired
    private AddressCache addressCache;

    /**
     * 解析区块, 构造业务入库参数信息
     * @return
     */
    public List<BusinessParam> getParameters(CollectionEvent event) throws Exception{
        List<BusinessParam> businessParams = new ArrayList<>();
        CollectionBlock block = event.getBlock();
        EpochMessage epochMessage = event.getEpochMessage();
        
        // 网络统计
        NetworkStat networkStat = networkStatCache.getNetworkStat();
        NetworkStatChange networkStatChange = NetworkStatChange.builder()
        	.id(1)
        	.curNumber(block.getNum())
        	.nodeId(block.getNodeId())
        	.nodeName(nodeCache.getNode(block.getNodeId()).getNodeName())
        	.txQty(networkStat.getTxQty())
        	.curTps(networkStat.getCurTps())
        	.maxTps(networkStat.getMaxTps())
        	.proposalQty(networkStat.getProposalQty())
        	.blockReward(new BigDecimal(epochMessage.getBlockReward()))
        	.stakingReward(new BigDecimal(epochMessage.getStakeReward()))
        	.addIssueBegin(CalculateUtils.calculateAddIssueBegin(chainConfig.getAddIssuePeriodBlockCount(), epochMessage.getIssueEpochRound()))
        	.addIssueEnd(CalculateUtils.calculateAddIssueEnd(chainConfig.getAddIssuePeriodBlockCount(), epochMessage.getIssueEpochRound()))
        	.nextSettle(CalculateUtils.calculateNextSetting(chainConfig.getSettlePeriodBlockCount(), epochMessage.getSettleEpochRound(), epochMessage.getCurrentBlockNumber()))
        	.build();
        businessParams.add(networkStatChange);
        
        // 地址统计
        if(!addressCache.getAddressMap().isEmpty()) {
            List<AddressStatItem> addressStatItemList =  addressCache
                	.getAddressMap()
                	.values()
                	.stream()
                	.map(address->{ return AddressStatItem.builder()
                			.address(address.getAddress())
                			.type(address.getType())
                			.txQty(address.getTxQty())
                			.transferQty(address.getTransferQty())
                			.delegateQty(address.getDelegateQty())
                			.stakingQty(address.getStakingQty())
                			.contractName(address.getContractName())
                			.contractCreate(address.getContractCreate())
                			.contractCreatehash(address.getContractCreatehash())
                			.build();})
                	.collect(Collectors.toList());
                addressCache.getAddressMap().clear();
           
                AddressStatChange addressStatChange = AddressStatChange.builder()
            		.addressStatItemList(addressStatItemList)
            		.build();
                businessParams.add(addressStatChange);
        }
        
        return businessParams;
    }
}
