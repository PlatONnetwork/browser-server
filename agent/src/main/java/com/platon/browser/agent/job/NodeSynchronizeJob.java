package com.platon.browser.agent.job;

import com.alibaba.fastjson.JSON;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.platon.browser.agent.contract.CandidateConstract;
import com.platon.browser.common.client.Web3jClient;
import com.platon.browser.common.constant.ConfigConst;
import com.platon.browser.common.dto.agent.CandidateDto;
import com.platon.browser.common.enums.MqMessageTypeEnum;
import com.platon.browser.common.spring.MQSender;
import com.platon.browser.dao.entity.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * User: dongqile
 * Date: 2018/10/25
 * Time: 18:07
 */
public class NodeSynchronizeJob extends AbstractTaskJob {

    private final static String contratAddress = "0x1000000000000000000000000000000000000001";


    @Autowired
    private MQSender mqSender;

    //1.查询预编译票池合约所有节点List列表
    //2.更新入队列
    @Override
    protected void doJob ( ShardingContext shardingContext ) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            URL resource = NodeSynchronizeJob.class.getClassLoader().getResource("platonbrowser.json");
            String path = resource.getPath();
            Credentials credentials = WalletUtils.loadCredentials( "88888888",path);
             Web3j web3j = Web3jClient.getWeb3jClient();
            List <CandidateDto> candidateDtoList = new ArrayList <>();

            CandidateConstract candidateConstract = CandidateConstract.load("0x1000000000000000000000000000000000000001",web3j,
                    credentials,DefaultGasProvider.GAS_PRICE, DefaultGasProvider.GAS_LIMIT);
            //nodeInfo rangking 1-200
            String nodeInfoList = candidateConstract.CandidateList().send();

            //nodeInfo rangking 1-25
            String verfiersList = candidateConstract.VerifiersList().send();
            if (verfiersList.length() > 0 && null != verfiersList) {
                logger.info("verfiersList info :", verfiersList);
                List <CandidateDto> verfiersDtoList = JSON.parseArray(verfiersList, CandidateDto.class);
                candidateDtoList.addAll(verfiersDtoList);
            }
            if (nodeInfoList.length() > 0 && null != nodeInfoList) {
                logger.info("nodeInfoList info :", verfiersList);
                List <CandidateDto> nodeInfoDtoList = JSON.parseArray(nodeInfoList, CandidateDto.class);
                //delete repeat element
                for (int i = 0; i < nodeInfoDtoList.size(); i++) {
                    for (int j = 0; j < candidateDtoList.size(); j++) {
                        if (nodeInfoDtoList.get(i).getCandidateId() != candidateDtoList.get(j).getCandidateId()) {
                            candidateDtoList.add(nodeInfoDtoList.get(i));
                        }
                    }
                }
                //nodeInfoList delete repeat element surplus 26-200 element
                for (CandidateDto candidateDto : nodeInfoDtoList) {
                    candidateDtoList.add(candidateDto);

                    logger.info("CandidateDto info : ", candidateDto, "CandidateID：{" ,candidateDto.getCandidateId(),"}，CandidateID：{",candidateDto.getDeposit(),"}，CandidateBlockNum :{",candidateDto.getBlockNumber(),"}");
                }
            }
            mqSender.send(ConfigConst.getChainId(), MqMessageTypeEnum.NODE.name(),candidateDtoList);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Synchronize node info exception!...", e.getMessage());
        }
    }
}