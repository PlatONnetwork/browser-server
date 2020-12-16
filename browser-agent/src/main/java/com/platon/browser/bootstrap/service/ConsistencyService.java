package com.platon.browser.bootstrap.service;


import com.alaya.protocol.core.methods.response.PlatonBlock;
import com.platon.browser.bootstrap.queue.callback.ShutdownCallback;
import com.platon.browser.bootstrap.queue.publisher.BootstrapEventPublisher;
import com.platon.browser.client.ReceiptResult;
import com.platon.browser.collection.service.block.BlockService;
import com.platon.browser.collection.service.receipt.ReceiptService;
import com.platon.browser.complement.dao.mapper.SyncTokenInfoMapper;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.dao.mapper.NetworkStatMapper;
import com.platon.browser.dto.elasticsearch.TokenTxSummary;
import com.platon.browser.elasticsearch.BlockESRepository;
import com.platon.browser.elasticsearch.InnerTxESRepository;
import com.platon.browser.param.sync.AddressTokenQtyUpdateParam;
import com.platon.browser.param.sync.Erc20TokenAddressRelTxCountUpdateParam;
import com.platon.browser.param.sync.Erc20TokenTxCountUpdateParam;
import com.platon.browser.param.sync.NetworkStatTokenQtyUpdateParam;
import com.platon.browser.util.SleepUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @description: MySQL/ES/Redis启动一致性自检服务
 * @author: chendongming@juzix.net
 * @create: 2019-11-06 10:10:30
 **/
@Slf4j
@Service
public class ConsistencyService {
    @Autowired
    private NetworkStatMapper networkStatMapper;
    @Autowired
    private BlockESRepository blockESRepository;
    @Autowired
    private BlockService blockService;
    @Autowired
    private ReceiptService receiptService;
    @Autowired
    private BootstrapEventPublisher bootstrapEventPublisher;

    @Resource
    private InnerTxESRepository tokenTxESRepository;

    @Resource
    private SyncTokenInfoMapper syncTokenInfoMapper;

    private ShutdownCallback shutdownCallback = new ShutdownCallback();

    /**
     * 同步ARC20相关地址交易数统计数据
     */

    private void syncTxCount(){
        TokenTxSummary summary = tokenTxESRepository.groupContractTxCount();
        List<AddressTokenQtyUpdateParam> addressTokenQtyUpdateParams = summary.addressTokenQtyUpdateParamList();
        List<Erc20TokenAddressRelTxCountUpdateParam> erc20TokenAddressRelTxCountUpdateParams = summary.erc20TokenAddressRelTxCountUpdateParamList();
        List<Erc20TokenTxCountUpdateParam> erc20TokenTxCountUpdateParams = summary.erc20TokenTxCountUpdateParamList();
        NetworkStatTokenQtyUpdateParam networkStatTokenQtyUpdateParam = summary.networkStatTokenQtyUpdateParam();
        syncTokenInfoMapper.syncTokenTxCount(
            addressTokenQtyUpdateParams,
            erc20TokenTxCountUpdateParams,
            erc20TokenAddressRelTxCountUpdateParams,
            networkStatTokenQtyUpdateParam
        );
        log.info("同步ES中的Token交易数至Mysql数据库成功！");
    }

    /**
     * 开机自检，检查es、redis中的区块高度和交易序号是否和mysql数据库一致，以mysql的数据为准
     * @throws IOException
     */
    @Transactional(rollbackFor = Exception.class)
    public void post() throws IOException {
        NetworkStat networkStat = networkStatMapper.selectByPrimaryKey(1);
        if(networkStat==null) {
            return;
        }

        syncTxCount();

        // mysql中的最高块号
        long mysqlMaxBlockNum = networkStat.getCurNumber();
        log.warn("MYSQL最高区块号:{}",mysqlMaxBlockNum);
        // mysql中的最高交易序号ID
        long mysqlTransactionId = networkStat.getTxQty();
        log.warn("MYSQL最高交易序号:{}",mysqlTransactionId);
        // 计算同步开始区块号
        long esMaxBlockNum = mysqlMaxBlockNum;
        boolean exist =false;
        while (!exist){
            exist = blockESRepository.exists(String.valueOf(esMaxBlockNum));
            if(!exist) {
                esMaxBlockNum--;
                // 小于等于0时需要退出
                if(esMaxBlockNum<=0) {
                    exist=true;
                }
            }
        }
        log.warn("ES区块索引最高区块号:{}",esMaxBlockNum);

        if(esMaxBlockNum>=mysqlMaxBlockNum) {
            log.warn("MYSQL/ES/REDIS中的数据已同步!");
            return;
        }

        long startBlockNum = esMaxBlockNum+1;
        log.warn("MYSQL/ES/REDIS数据同步区间:[{},{}]",startBlockNum,mysqlMaxBlockNum);
        // 补充 [startBlockNum,mysqlMaxBlockNum] 闭区间内的区块和交易信息到ES和Redis
        shutdownCallback.setEndBlockNum(mysqlMaxBlockNum);
        if(startBlockNum<=0) startBlockNum=1;
        if(startBlockNum>mysqlMaxBlockNum) return;
        for (long number=startBlockNum;number<=mysqlMaxBlockNum;number++){
            // 异步获取区块
            CompletableFuture<PlatonBlock> blockCF = blockService.getBlockAsync(number);
            // 异步获取交易回执
            CompletableFuture<ReceiptResult> receiptCF = receiptService.getReceiptAsync(number);
            bootstrapEventPublisher.publish(blockCF,receiptCF,shutdownCallback);
        }
        while (!shutdownCallback.isDone()) SleepUtil.sleep(1L);
        bootstrapEventPublisher.shutdown();
        log.warn("MYSQL/ES/REDIS中的数据同步完成!");
    }
}
