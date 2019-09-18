package com.platon.browser.engine.handler.statistic;

import com.platon.browser.dto.CustomAddress;
import com.platon.browser.dto.CustomNetworkStat;
import com.platon.browser.dto.CustomTransaction;
import com.platon.browser.engine.cache.AddressCache;
import com.platon.browser.engine.cache.CacheHolder;
import com.platon.browser.engine.handler.EventContext;
import com.platon.browser.engine.handler.EventHandler;
import com.platon.browser.engine.stage.AddressStage;
import com.platon.browser.enums.ContractDescEnum;
import com.platon.browser.exception.NoSuchBeanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/17 20:09
 * @Description: 地址统计处理类
 */
@Component
public class AddressStatisticHandler implements EventHandler {
    private static Logger logger = LoggerFactory.getLogger(AddressStatisticHandler.class);
    @Autowired
    private CacheHolder cacheHolder;
    @Override
    public void handle(EventContext context) {
        AddressCache addressCache = cacheHolder.getAddressCache();
        CustomNetworkStat networkStatCache = cacheHolder.getNetworkStatCache();
        AddressStage addressStage = context.getAddressStage();
        CustomTransaction tx = context.getTransaction();

        String from = tx.getFrom(),to = tx.getTo();

        // 取from地址缓存，不存在则新建
        CustomAddress fromAddress;
        try {
            fromAddress = addressCache.getAddress(from);
        } catch (NoSuchBeanException e) {
            logger.debug("缓存中没有from地址({})记录，添加一条",from);
            fromAddress = new CustomAddress();
            fromAddress.setAddress(from);
            // 添加至全量缓存
            addressCache.add(fromAddress);
        }

        // 取to地址缓存，不存在则新建
        CustomAddress toAddress;
        try {
            toAddress = addressCache.getAddress(to);
        } catch (NoSuchBeanException e) {
            logger.debug("缓存中没有to地址({})记录，添加一条",to);
            toAddress = new CustomAddress();
            toAddress.setAddress(to);
            // 添加至全量缓存
            addressCache.add(toAddress);
        }

        ContractDescEnum cde = ContractDescEnum.MAP.get(from);
        if(cde!=null){
            fromAddress.setContractName(cde.getContractName());
            fromAddress.setContractCreate(cde.getCreator());
            fromAddress.setContractCreatehash(cde.getContractHash());
        }
        cde = ContractDescEnum.MAP.get(to);
        if(cde!=null){
            toAddress.setContractName(cde.getContractName());
            toAddress.setContractCreate(cde.getCreator());
            toAddress.setContractCreatehash(cde.getContractHash());
        }

        // 更新与地址是from还是to无关的通用属性
        fromAddress.updateWithCustomTransaction(tx);
        toAddress.updateWithCustomTransaction(tx);

        addressStage.insertAddress(fromAddress);
        addressStage.insertAddress(toAddress);
        networkStatCache.setAddressQty(addressCache.getAllAddress().size());
    }
}
