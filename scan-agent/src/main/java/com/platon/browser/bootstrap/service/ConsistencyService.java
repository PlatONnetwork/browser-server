package com.platon.browser.bootstrap.service;


import com.platon.browser.bean.ReceiptResult;
import com.platon.browser.bootstrap.BootstrapEventPublisher;
import com.platon.browser.bootstrap.ShutdownCallback;
import com.platon.browser.cache.DestroyContractCache;
import com.platon.browser.dao.custommapper.CustomAddressMapper;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.dao.mapper.NetworkStatMapper;
import com.platon.browser.service.block.BlockService;
import com.platon.browser.service.elasticsearch.EsBlockRepository;
import com.platon.browser.service.receipt.ReceiptService;
import com.platon.browser.utils.SleepUtil;
import com.platon.protocol.core.methods.response.PlatonBlock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @description: MySQL/ES/Redis启动一致性自检服务
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-06 10:10:30
 **/
@Slf4j
@Service
public class ConsistencyService {

    @Resource
    private NetworkStatMapper networkStatMapper;

    @Resource
    private EsBlockRepository ESBlockRepository;

    @Resource
    private BlockService blockService;

    @Resource
    private ReceiptService receiptService;

    @Resource
    private BootstrapEventPublisher bootstrapEventPublisher;

    @Resource
    private CustomAddressMapper customAddressMapper;

    @Resource
    private DestroyContractCache destroyContractCache;

    private ShutdownCallback shutdownCallback = new ShutdownCallback();


    /**
     * 开机自检,一致性开机自检子流程,检查es、redis中的区块高度和交易序号是否和mysql数据库一致，以mysql的数据为准
     *
     * @param traceId
     * @return void
     * @date 2021/4/19
     */
    @Transactional(rollbackFor = Exception.class)
    public void post(String traceId) throws IOException {
        NetworkStat networkStat = networkStatMapper.selectByPrimaryKey(1);
        if (networkStat == null) {
            return;
        }

        // mysql中的最高块号
        long mysqlMaxBlockNum = networkStat.getCurNumber();
        log.warn("MYSQL最高区块号:{}", mysqlMaxBlockNum);
        // mysql中的最高交易序号ID
        long mysqlTransactionId = networkStat.getTxQty();
        log.warn("MYSQL最高交易序号:{}", mysqlTransactionId);
        // 计算同步开始区块号
        long esMaxBlockNum = mysqlMaxBlockNum;
        boolean exist = false;
        while (!exist) {
            exist = ESBlockRepository.exists(String.valueOf(esMaxBlockNum));
            if (!exist) {
                esMaxBlockNum--;
                // 小于等于0时需要退出
                if (esMaxBlockNum <= 0) {
                    exist = true;
                }
            }
        }
        log.warn("ES区块索引最高区块号:{}", esMaxBlockNum);

        if (esMaxBlockNum >= mysqlMaxBlockNum) {
            log.warn("MYSQL/ES/REDIS中的数据已同步!");
            return;
        }

        long startBlockNum = esMaxBlockNum + 1;
        log.warn("MYSQL/ES/REDIS数据同步区间:[{},{}]", startBlockNum, mysqlMaxBlockNum);
        // 补充 [startBlockNum,mysqlMaxBlockNum] 闭区间内的区块和交易信息到ES和Redis
        shutdownCallback.setEndBlockNum(mysqlMaxBlockNum);
        if (startBlockNum <= 0) startBlockNum = 1;
        if (startBlockNum > mysqlMaxBlockNum) return;
        for (long number = startBlockNum; number <= mysqlMaxBlockNum; number++) {
            // 异步获取区块
            CompletableFuture<PlatonBlock> blockCF = blockService.getBlockAsync(number);
            // 异步获取交易回执
            CompletableFuture<ReceiptResult> receiptCF = receiptService.getReceiptAsync(number);
            bootstrapEventPublisher.publish(blockCF, receiptCF, shutdownCallback, traceId);
        }
        while (!shutdownCallback.isDone()) SleepUtil.sleep(1L);
        bootstrapEventPublisher.shutdown();
        log.warn("MYSQL/ES/REDIS中的数据同步完成!");
    }

    /**
     * 加载销毁的合约到内存
     *
     * @param :
     * @return: void
     * @date: 2021/10/14
     */
    public void loadDestroyContract() {
        List<String> list = customAddressMapper.findContractDestroy(null);
        destroyContractCache.getDestroyContracts().addAll(list);
    }

}
