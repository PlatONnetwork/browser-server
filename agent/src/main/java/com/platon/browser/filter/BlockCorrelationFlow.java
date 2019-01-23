package com.platon.browser.filter;

import com.platon.browser.common.base.AppException;
import com.platon.browser.common.enums.ErrorCodeEnum;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.job.DataCollectorJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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

    public final static ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(10);

    public void doFilter (DataCollectorJob.AnalysisParam param) {

        try {
            /// 分析区块数据并入库
            long startTime = System.currentTimeMillis();
            Block block = blockFilter.analysis(param);
            logger.debug("BlockFilter.analysis()              :--->{}",System.currentTimeMillis()-startTime);

            if(block==null) block = new Block();
            Block blockRef = block;

            // 分析节点数据并入库
            EXECUTOR_SERVICE.submit(()->{
                long startTime0 = System.currentTimeMillis();
                try{
                    nodeFilter.analysis(param,blockRef);
                    logger.debug("NodeFilter.analysis()               :--->{}",System.currentTimeMillis()-startTime0);
                }catch (Exception e){
                    e.printStackTrace();
                }
            });

            // 分析交易数据并入库
            EXECUTOR_SERVICE.submit(()->{
                long startTime1 = System.currentTimeMillis();
                try{
                    transactionFilter.analysis(param,blockRef.getTimestamp().getTime());
                    logger.debug("TransactionFilter.analysis()        :--->{}",System.currentTimeMillis()-startTime1);
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

