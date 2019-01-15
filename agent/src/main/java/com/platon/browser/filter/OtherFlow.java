package com.platon.browser.filter;

import com.platon.browser.common.base.AppException;
import com.platon.browser.common.enums.ErrorCodeEnum;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.NodeRanking;
import com.platon.browser.job.ChainInfoFilterJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthPendingTransactions;
import org.web3j.protocol.core.methods.response.Transaction;

import java.util.List;
import java.util.Map;

/**
 * User: dongqile
 * Date: 2019/1/15
 * Time: 19:20
 */
@Component
public class OtherFlow {

    private static Logger log = LoggerFactory.getLogger(OtherFlow.class);


    @Value("${chain.id}")
    private String chainId;

    @Autowired
    private PendingFilter pendingFilter;

    @Autowired
    private StompPushFilter stompPushFilter;

    @Transactional
    public void doFilter(){
        Map<String,Object> threadLocalMap = ChainInfoFilterJob.map.get();
        EthPendingTransactions ethPendingTransactions = (EthPendingTransactions) threadLocalMap.get("ethPendingTransactions");
        List<NodeRanking> nodeRankings = (List <NodeRanking>) threadLocalMap.get("nodeRankings");
        Block block = (Block) threadLocalMap.get("block");
        try {
            if(ethPendingTransactions != null){
                boolean res = pendingFilter.pendingTxAnalysis(ethPendingTransactions);
                if (!res) {
                    log.error("Analysis PendingTx is null !!!....");
                }
            }
            log.debug("PengdingTx in the block are empty !!!...");
        } catch (Exception e) {
            log.error("PendingTx Filter exception", e);
            log.error("PendingTx analysis exception", e.getMessage());
            throw new AppException(ErrorCodeEnum.PENDINGTX_ERROR);
        }

        try {
            if(null != nodeRankings && nodeRankings.size() > 0 ){
                stompPushFilter.stompPush(block, nodeRankings);
            }
        } catch (Exception e) {
            log.error("Stomp Filter exception", e);
            log.error("push redis exception", e.getMessage());
            throw new AppException(ErrorCodeEnum.STOMP_ERROR);
        }
    }
}