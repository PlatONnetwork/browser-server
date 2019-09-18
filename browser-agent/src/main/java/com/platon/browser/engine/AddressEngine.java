package com.platon.browser.engine;

import com.platon.browser.dao.mapper.CustomAddressMapper;
import com.platon.browser.dto.CustomAddress;
import com.platon.browser.dto.CustomTransaction;
import com.platon.browser.engine.cache.AddressCache;
import com.platon.browser.engine.cache.CacheHolder;
import com.platon.browser.engine.handler.EventContext;
import com.platon.browser.engine.handler.statistic.AddressStatisticHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/10 16:12
 * @Description: 地址分析引擎
 */
@Component
public class AddressEngine {
    private static Logger logger = LoggerFactory.getLogger(AddressEngine.class);

    private EventContext context = new EventContext();

    @Autowired
    private CustomAddressMapper customAddressMapper;
    @Autowired
    private CacheHolder cacheHolder;
    @Autowired
    private AddressStatisticHandler addressStatisticHandler;

    @PostConstruct
    private void init () {
        AddressCache addressCache= cacheHolder.getAddressCache();
    	logger.debug("init AddressEngine");
        // 初始化全量数据
        List<CustomAddress> addresses = customAddressMapper.selectAll();
        addresses.forEach(addressCache::add);
    }

    public void execute (CustomTransaction tx,BlockChain bc) {
        context.setTransaction(tx);
        addressStatisticHandler.handle(context);
    }
}
