package com.platon.browser.complement.handler;

import com.platon.browser.common.collection.dto.CollectionTransaction;
import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.common.complement.param.BusinessParam;
import com.platon.browser.common.complement.dto.ComplementNodeOpt;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.common.queue.collection.handler.ICollectionEventHandler;
import com.platon.browser.common.queue.complement.publisher.ComplementEventPublisher;
import com.platon.browser.complement.service.param.BlockParameterService;
import com.platon.browser.complement.service.param.StatisticParameterService;
import com.platon.browser.complement.service.param.TransactionParameterService;
import com.platon.browser.elasticsearch.dto.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 区块事件处理器
 */
@Slf4j
public class CollectionEventHandler implements ICollectionEventHandler {

    @Autowired
    private TransactionParameterService transactionParameterService;
    @Autowired
    private BlockParameterService blockParameterService;
    @Autowired
    private StatisticParameterService statisticParameterService;
    @Autowired
    private ComplementEventPublisher complementEventPublisher;

    @Autowired
    private NetworkStatCache networkStatCache;

    // 交易序号id
    private long transactionId = 0;

    private Long preBlockNum=0L;
    public void onEvent(CollectionEvent event, long sequence, boolean endOfBatch) throws Exception {
        log.debug("CollectionEvent处理:{}(event(block({}),transactions({})),sequence({}),endOfBatch({}))",
                Thread.currentThread().getStackTrace()[1].getMethodName(),event.getBlock().getNum(),event.getTransactions().size(),sequence,endOfBatch);
        if(preBlockNum!=0L&&(event.getBlock().getNum()-preBlockNum!=1)) throw new AssertionError();

        // 使用已入库的交易数量初始化交易ID初始值
        if(transactionId==0) transactionId=networkStatCache.getNetworkStat().getTxQty();
        try {
            // 确保交易从小到大的索引顺序
            event.getTransactions().sort(Comparator.comparing(Transaction::getIndex));
            for (CollectionTransaction tx : event.getTransactions()) tx.setId(++transactionId);

            // 根据区块号解析出业务参数
            List<BusinessParam> param1 = blockParameterService.getParameters(event);
            // 根据交易解析出业务参数
            List<BusinessParam> param2 = transactionParameterService.getParameters(event);
            // 统计业务参数
            List<BusinessParam> param3 = statisticParameterService.getParameters(event);
          
            param1.addAll(param2);
            param1.addAll(param3);

            // TODO: 根据交易解析出节点操作日志记录
            List<ComplementNodeOpt> nodeOpts = new ArrayList<>();

            complementEventPublisher.publish(event,nodeOpts,param1);

            preBlockNum=event.getBlock().getNum();
        }catch (Exception e){
            log.error("{}",e);
            throw e;
        }
    }
}