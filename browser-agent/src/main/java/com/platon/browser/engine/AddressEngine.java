package com.platon.browser.engine;

import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.mapper.CustomAddressMapper;
import com.platon.browser.dto.CustomAddress;
import com.platon.browser.dto.CustomTransaction;
import com.platon.browser.engine.cache.AddressCache;
import com.platon.browser.engine.cache.CacheHolder;
import com.platon.browser.engine.handler.EventContext;
import com.platon.browser.engine.handler.statistic.AddressStatisticHandler;
import com.platon.browser.engine.stage.AddressStage;
import com.platon.browser.exception.NoSuchBeanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    @Autowired
    private BlockChainConfig chainConfig;

    @PostConstruct
    public void init () {
        AddressCache addressCache= cacheHolder.getAddressCache();
        AddressStage addressStage= cacheHolder.getStageData().getAddressStage();
    	logger.debug("init AddressEngine");
        // 初始化全量数据
        List<CustomAddress> addresses = customAddressMapper.selectAll();
        addresses.forEach(addressCache::add);

        // 检查内置地址是否在数据库中存在，不存在则入库并添加至缓存
        Set<String> innerAddresses = new HashSet<>(chainConfig.getInnerContractAddr());
        //PlatOn基金会账户地址
        innerAddresses.add(chainConfig.getPlatonFundAccountAddr());
        //开发者激励基金账户地址
        innerAddresses.add(chainConfig.getDeveloperIncentiveFundAccountAddr());
        innerAddresses.forEach(address->{
            try {
                addressCache.getAddress(address);
            } catch (NoSuchBeanException e) {
                CustomAddress customAddress = new CustomAddress();
                customAddress.setAddress(address);
                customAddress.setType(CustomAddress.TypeEnum.INNER_CONTRACT.getCode());
                // 放入地址缓存
                addressCache.add(customAddress);
                // 放入入库暂存
                addressStage.insertAddress(customAddress);
            }
        });
    }

    public void execute (CustomTransaction tx) {
        context.setTransaction(tx);
        addressStatisticHandler.handle(context);
    }
}
