package com.platon.browser.service.statistic;

import cn.hutool.core.collection.CollUtil;
import com.platon.browser.analyzer.statistic.StatisticsAddressAnalyzer;
import com.platon.browser.analyzer.statistic.StatisticsNetworkAnalyzer;
import com.platon.browser.bean.CollectionEvent;
import com.platon.browser.bean.EpochMessage;
import com.platon.browser.cache.AddressCache;
import com.platon.browser.dao.entity.Address;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.exception.NoSuchBeanException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;

/**
 * 统计入库参数服务
 *
 * @author chendai
 */
@Slf4j
@Service
public class StatisticService {

    @Resource
    private AddressCache addressCache;

    @Resource
    private StatisticsNetworkAnalyzer statisticsNetworkAnalyzer;

    @Resource
    private StatisticsAddressAnalyzer statisticsAddressAnalyzer;

    /**
     * 解析区块, 构造业务入库参数信息
     *
     * @return
     */
    public void analyze(CollectionEvent event) throws NoSuchBeanException {
        long startTime = System.currentTimeMillis();
        Block block = event.getBlock();
        EpochMessage epochMessage = event.getEpochMessage();
        // 地址统计
        Collection<Address> addressList = this.addressCache.getAll();
        if (block.getNum() == 0) {
            if (CollUtil.isNotEmpty(addressList)) {
                // 初始化内置地址，比如内置合约等
                this.statisticsAddressAnalyzer.analyze(event, block, epochMessage);
            }
            return;
        }
        this.statisticsNetworkAnalyzer.analyze(event, block, epochMessage);
        // 程序逻辑运行至此处，所有ppos相关业务逻辑已经分析完成，进行地址入库操作
        if (!addressList.isEmpty()) {
            this.statisticsAddressAnalyzer.analyze(event, block, epochMessage);
        }
        log.debug("处理耗时:{} ms", System.currentTimeMillis() - startTime);
    }

}
