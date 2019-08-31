package com.platon.browser.engine.handler.statistic;

import com.platon.browser.client.PlatonClient;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dto.CustomAddress;
import com.platon.browser.dto.CustomTransaction;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.handler.EventContext;
import com.platon.browser.engine.handler.EventHandler;
import com.platon.browser.engine.stage.AddressStage;
import com.platon.browser.exception.NoSuchBeanException;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;

import java.io.IOException;
import java.math.BigInteger;

import static com.platon.browser.engine.BlockChain.ADDRESS_CACHE;
import static com.platon.browser.engine.BlockChain.NETWORK_STAT_CACHE;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/17 20:09
 * @Description: 地址统计处理类
 */
@Component
public class AddressStatisticHandler implements EventHandler {
    private static Logger logger = LoggerFactory.getLogger(AddressStatisticHandler.class);
    @Autowired
    private BlockChain bc;
    @Autowired
    private BlockChainConfig chainConfig;
    @Autowired
    private PlatonClient client;
    @Override
    public void handle(EventContext context) {
        AddressStage addressStage = context.getAddressStage();
        CustomTransaction tx = context.getTransaction();

        String from = tx.getFrom(),to = tx.getTo();

        // 取from地址缓存，不存在则新建
        CustomAddress fromAddress;
        try {
            fromAddress = ADDRESS_CACHE.getAddress(from);
        } catch (NoSuchBeanException e) {
            logger.debug("缓存中没有from地址({})记录，添加一条",from);
            fromAddress = new CustomAddress();
            fromAddress.setAddress(from);
            // 添加至全量缓存
            ADDRESS_CACHE.add(fromAddress);
        }

        // 取to地址缓存，不存在则新建
        CustomAddress toAddress;
        try {
            toAddress = ADDRESS_CACHE.getAddress(to);
        } catch (NoSuchBeanException e) {
            logger.debug("缓存中没有to地址({})记录，添加一条",to);
            toAddress = new CustomAddress();
            toAddress.setAddress(to);
            // 添加至全量缓存
            ADDRESS_CACHE.add(toAddress);
        }

        // 更新与地址是from还是to无关的通用属性
        fromAddress.updateWithCustomTransaction(tx);
        toAddress.updateWithCustomTransaction(tx);

        addressStage.insertAddress(fromAddress);
        addressStage.insertAddress(toAddress);
        NETWORK_STAT_CACHE.setAddressQty(ADDRESS_CACHE.getAllAddress().size());
    }
}
