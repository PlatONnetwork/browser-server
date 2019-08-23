package com.platon.browser.engine;

import com.platon.browser.dao.mapper.CustomAddressMapper;
import com.platon.browser.dto.CustomAddress;
import com.platon.browser.dto.CustomTransaction;
import com.platon.browser.engine.cache.AddressCache;
import com.platon.browser.engine.stage.AddressStage;
import com.platon.browser.exception.NoSuchBeanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * User: dongqile
 * Date: 2019/8/14
 * Time: 17:14
 */
@Component
public class AddressExecute {
    private static Logger logger = LoggerFactory.getLogger(AddressExecute.class);

    @Autowired
    private CustomAddressMapper customAddressMapper;

    private AddressStage addressStage = BlockChain.STAGE_DATA.getAddressStage();
    // 全量数据，需要根据业务变化，保持与数据库一致
    private AddressCache addressCache = BlockChain.ADDRESS_CACHE;

    @PostConstruct
    private void init () {
        // 初始化全量数据
        List<CustomAddress> addresses = customAddressMapper.selectAll();
        addresses.forEach(address -> addressCache.add(address));
    }

    public void execute (CustomTransaction tx) {
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

        // 更新与地址是from还是to无关的通用属性
        fromAddress.updateWithCustomTransaction(tx);
        toAddress.updateWithCustomTransaction(tx);

        addressStage.insertAddress(fromAddress);
        addressStage.insertAddress(toAddress);
    }
}
