package com.platon.browser.agent.job;

import com.alibaba.fastjson.JSON;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.platon.browser.agent.contract.CandidateConstract;
import com.platon.browser.common.client.Web3jClient;
import com.platon.browser.common.constant.ConfigConst;
import com.platon.browser.common.dto.agent.CandidateDto;
import com.platon.browser.common.enums.MqMessageTypeEnum;
import com.platon.browser.common.spring.MQSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.gas.DefaultGasProvider;

import java.util.List;

/**
 * User: dongqile
 * Date: 2018/10/25
 * Time: 18:07
 */
public class NodeSynchronizeJob extends AbstractTaskJob{

    private final static  String contratAddress  = "0x1000000000000000000000000000000000000001";


    @Autowired
    private MQSender mqSender;

    //1.查询预编译票池合约所有节点List列表
    //2.更新入队列
    @Override
    protected void doJob ( ShardingContext shardingContext ) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
        Credentials credentials = null;
            Web3j web3j = Web3jClient.getWeb3jClient();
            CandidateConstract candidateConstract = CandidateConstract.load(contratAddress,web3j,credentials, DefaultGasProvider.GAS_PRICE, DefaultGasProvider.GAS_LIMIT);
            String nodeInfoList = candidateConstract.CandidateList().send();
            if(nodeInfoList.length() > 0 && null != nodeInfoList){
                logger.info("node info :",nodeInfoList);
                List<CandidateDto> candidateDtoList = JSON.parseArray(nodeInfoList, CandidateDto.class);
                mqSender.send(ConfigConst.getChainId(), MqMessageTypeEnum.NODE.name(),candidateDtoList);
                logger.info("node info ");
            }
        } catch (Exception e) {
            logger.error("Synchronize node info exception!...",e.getMessage());
        }
    }

}