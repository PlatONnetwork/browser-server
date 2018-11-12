package com.platon.browser.agent.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.platon.browser.common.base.AppException;
import com.platon.browser.common.constant.ConfigConst;
import com.platon.browser.common.enums.ErrorCodeEnum;
import com.platon.browser.dao.entity.PendingTx;
import com.platon.browser.dao.entity.PendingTxExample;
import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.dao.entity.TransactionExample;
import com.platon.browser.dao.mapper.PendingTxMapper;
import com.platon.browser.dao.mapper.TransactionMapper;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;

import java.util.List;


/**
 * User: dongqile
 * Date: 2018/11/12
 * Time: 17:40
 */
public class PendingTxUpdateTaskJob extends AbstractTaskJob {
    /**
     * 挂起交易更新任务
     * <p>
     * <p>
     * 1. 定时查询挂起交易列表
     * 2. 比对交易列表数据
     * 3. 相同表示挂起交易已出块，删除挂起交易列表中该数据
     */
    private static Logger log = LoggerFactory.getLogger(PendingTxUpdateTaskJob.class);

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private PendingTxMapper pendingTxMapper;

    @Override
    protected void doJob ( ShardingContext shardingContext ) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            //比对交易信息数据更新pending交易列表
            List <PendingTx> pendingTxeList = pendingTxMapper.selectByExample(new PendingTxExample());
            if (pendingTxeList.size() > 0) {
                for (PendingTx pendingTx : pendingTxeList) {
                    TransactionExample transactionExample = new TransactionExample();
                    transactionExample.createCriteria().andHashEqualTo(pendingTx.getHash()).andChainIdEqualTo(ConfigConst.getChainId());
                    List <Transaction> transactionList = transactionMapper.selectByExample(transactionExample);
                    if (transactionList.size() == 1) {
                        pendingTxMapper.deleteByPrimaryKey(pendingTx.getHash());
                        log.debug("PendingTx update..... ->{" + pendingTx.getHash() + "}");
                    }
                    if (transactionList.size() > 1) {
                        log.error("PendingTx comparison transaction repeat!!!... TxHash:{" + pendingTx.getHash() + "}");
                        throw new AppException(ErrorCodeEnum.PENDINGTX_REPEAT);
                    }
                    log.debug("PendingTx update list is null...,wait next time update...");
                }
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e.getStackTrace());
        } finally {
            stopWatch.stop();
            log.debug("PendingTxUpdateTaskJob-->{}", stopWatch.shortSummary());
        }
    }
}