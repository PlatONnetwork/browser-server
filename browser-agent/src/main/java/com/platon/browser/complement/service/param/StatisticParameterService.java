package com.platon.browser.complement.service.param;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platon.browser.common.collection.dto.CollectionBlock;
import com.platon.browser.common.collection.dto.EpochMessage;
import com.platon.browser.common.complement.cache.AddressCache;
import com.platon.browser.common.complement.dto.BusinessParam;
import com.platon.browser.common.complement.dto.statistic.AddressStatChange;
import com.platon.browser.common.complement.dto.statistic.NetworkStatChange;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.service.param.converter.StatisticsAddressConverter;
import com.platon.browser.complement.service.param.converter.StatisticsNetworkConverter;
import com.platon.browser.dao.entity.Address;

/**
 * 统计入库参数服务 
 * @author chendai
 */
@Service
public class StatisticParameterService {
    
    @Autowired
    private AddressCache addressCache;
	@Autowired
	private StatisticsNetworkConverter statisticsNetworkConverter;
	@Autowired
	private StatisticsAddressConverter statisticsAddressConverter;

    /**
     * 解析区块, 构造业务入库参数信息
     * @return
     */
    public List<BusinessParam> getParameters(CollectionEvent event) throws Exception{
        List<BusinessParam> businessParams = new ArrayList<>();
        CollectionBlock block = event.getBlock();
        EpochMessage epochMessage = event.getEpochMessage();
        NetworkStatChange networkStatChange = statisticsNetworkConverter.convert(event, block, epochMessage);
        businessParams.add(networkStatChange);
        
        // 地址统计
        Collection<Address> addressList = addressCache.getAll();
        if(addressList.size()> 0) {
        	AddressStatChange addressStatChange = statisticsAddressConverter.convert(event, block, epochMessage);
            businessParams.add(addressStatChange);
        }
        
        return businessParams;
    }

}
