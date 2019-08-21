package com.platon.browser.task;

import com.platon.browser.dto.CustomProposal;
import com.platon.browser.engine.BlockChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * User: dongqile
 * Date: 2019/8/21
 * Time: 11:04
 */
@Component
public class ProposalSupplySyn {
    private static Logger logger = LoggerFactory.getLogger(ProposalSupplySyn.class);

    @Autowired
    private BlockChain bc;

    private static boolean taskBeginflag = false;

    @PostConstruct
    private void waitLoad () {
        try {
            //延迟加载，启动时，等待引擎中全量数据加载完毕
            if (bc.PROPOSALS_CACHE.size() > 0) {
                taskBeginflag = true;
            } else {
                Thread.sleep(5000);
            }
        } catch (InterruptedException e) {
            logger.error("[ProposalSupplySyn] waitLoad exception!!!");
            e.printStackTrace();
        }
    }
    /**
     * 同步任务功能说明：
     *  a.http请求查询github治理提案的相关信息并补充
     *  b.根据platon底层rpc接口查询提案结果
     */
    @Scheduled(cron = "0/5 * * * * ?")
    protected void infoSyn () {
        try {
            if (taskBeginflag) {
                //获取全量数据
                Map <String, CustomProposal> proposalMap =  bc.PROPOSALS_CACHE;
                proposalMap.forEach((pipid,customProposal) ->{
                    //只需要补充两种治理类型 1.升级提案 2.取消提案
                    //升级提案
                    if(customProposal.getType().equals(CustomProposal.TypeEnum.UPGRADE.code)){

                    }
                    //取消提案
                    if(customProposal.getType().equals(CustomProposal.TypeEnum.CANCEL.code)){

                    }
                    //查询提案结果，查询类型1.投票中 2.预升级


                });
            } else {
                logger.debug("[ProposalSupplySyn]引擎中治理数据为空，跳过治理数据补充任务...");
            }
        } catch (Exception e) {
            logger.error("");
        }
    }
}