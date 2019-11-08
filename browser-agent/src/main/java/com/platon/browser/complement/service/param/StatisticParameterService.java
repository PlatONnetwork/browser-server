package com.platon.browser.complement.service.param;

import com.platon.browser.common.collection.dto.EpochMessage;
import com.platon.browser.common.complement.cache.AddressCache;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.service.param.converter.StatisticsAddressConverter;
import com.platon.browser.complement.service.param.converter.StatisticsNetworkConverter;
import com.platon.browser.dao.entity.Address;
import com.platon.browser.elasticsearch.dto.Block;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

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
    public void getParameters(CollectionEvent event) throws Exception{
        Block block = event.getBlock();
        EpochMessage epochMessage = event.getEpochMessage();
        
        statisticsNetworkConverter.convert(event, block, epochMessage);
        
        // 地址统计
        Collection<Address> addressList = addressCache.getAll();
        if(!addressList.isEmpty()) {
        	statisticsAddressConverter.convert(event, block, epochMessage);
        }
    }

}
