package com.platon.browser.filter;

import com.platon.browser.common.base.AppException;
import com.platon.browser.common.enums.ErrorCodeEnum;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.NodeRanking;
import com.platon.browser.dao.entity.TransactionExample;
import com.platon.browser.dao.mapper.TransactionMapper;
import com.platon.browser.job.DataCollectorJob;
import com.platon.browser.service.RedisCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User: dongqile
 * Date: 2019/1/15
 * Time: 14:34
 */
@Component
public class BlockCorrelationFlow {

    private static Logger logger = LoggerFactory.getLogger(BlockCorrelationFlow.class);

    @Value("${chain.id}")
    private String chainId;

    @Autowired
    private BlockFilter blockFilter;
    @Autowired
    private TransactionFilter transactionFilter;
    @Autowired
    private NodeFilter nodeFilter;
    @Autowired
    private PendingFilter pendingFilter;

    @Autowired
    private RedisCacheService redisCacheService;
    @Autowired
    private TransactionMapper transactionMapper;

    private final static ExecutorService executorService = Executors.newFixedThreadPool(10);

    public void doFilter (DataCollectorJob.AnalysisParam param) {

        try {
            /// 分析区块数据并入库
            long startTime = System.currentTimeMillis();
            Block block = blockFilter.analysis(param);
            logger.debug("BlockFilter.analysis()              :--->{}",System.currentTimeMillis()-startTime);
            Set <Block> set = new HashSet <>();
            set.add(block);

            if(block==null) block = new Block();
            Block blockRef = block;

            // 分析节点数据并入库
            executorService.submit(()->{
                long startTime0 = System.currentTimeMillis();
                try{
                    List<NodeRanking> nodeRankings = nodeFilter.analysis(param,blockRef);
                    logger.debug("NodeFilter.analysis()               :--->{}",System.currentTimeMillis()-startTime0);
                    executorService.submit(()->{
                        try {
                            Set <NodeRanking> nodes = new HashSet <>(nodeRankings);
                            redisCacheService.updateNodePushCache(chainId, nodes);
                            redisCacheService.updateStatisticsCache(chainId, blockRef, nodeRankings);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            });

            // 更新区块缓存
            executorService.submit(()->redisCacheService.updateBlockCache(chainId, set));

            // 分析交易数据并入库
            executorService.submit(()->{
                long startTime1 = System.currentTimeMillis();
                try{
                    List<String> txHash = transactionFilter.analysis(param,blockRef.getTimestamp().getTime());
                    logger.debug("TransactionFilter.analysis()        :--->{}",System.currentTimeMillis()-startTime1);
                    try {
                        if (txHash.size()>0) {
                            executorService.submit(()->{
                                TransactionExample condition = new TransactionExample();
                                condition.createCriteria().andChainIdEqualTo(chainId).andHashIn(txHash);
                                List<com.platon.browser.dao.entity.Transaction> dbTrans = transactionMapper.selectByExample(condition);
                                redisCacheService.updateTransactionCache(chainId,new HashSet <>(dbTrans));
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new AppException(ErrorCodeEnum.BLOCKCHAIN_ERROR);
        }
    }

}

