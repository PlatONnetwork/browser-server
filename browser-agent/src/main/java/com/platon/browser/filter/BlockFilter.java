package com.platon.browser.filter;

import com.alibaba.fastjson.JSON;
import com.platon.browser.bean.BlockBean;
import com.platon.browser.client.PlatonClient;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dto.AnalysisResult;
import com.platon.browser.dto.EventRes;
import com.platon.browser.dto.agent.CandidateDto;
import com.platon.browser.enums.TransactionTypeEnum;
import com.platon.browser.thread.AnalyseThread;
import com.platon.browser.util.CalculatePublicKey;
import com.platon.browser.util.TransactionAnalysis;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.platon.contracts.CandidateContract;
import org.web3j.platon.contracts.TicketContract;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static com.platon.browser.utils.CacheTool.NODEID_TO_NAME;

/**
 * User: dongqile
 * Date: 2019/1/7
 * Time: 14:27
 */
@Component
public class BlockFilter {
    private static Logger logger = LoggerFactory.getLogger(BlockFilter.class);
    @Autowired
    private PlatonClient platon;
    @Value("${platon.chain.active}")
    private String chainId;

    public Block analyse ( AnalyseThread.AnalyseParam param ) {
        long beginTime = System.currentTimeMillis();
        BlockBean bean = new BlockBean();
        if (param.ethBlock != null) {
            bean.init(param.ethBlock);

            TicketContract ticketContract = platon.getTicketContract(chainId);
            String price = null;
            try {
                price = ticketContract.GetTicketPrice().send();
                bean.setVotePrice(null != price ? price : "0");
            } catch (Exception e) {
                logger.error("获取票价异常");
            }

            // 设置需要使用当前上下文的属性
            bean.setChainId(chainId);
            String publicKey = null;
            try {
                publicKey = CalculatePublicKey.getPublicKey(param.ethBlock);
                if (StringUtils.isNotBlank(publicKey) && !publicKey.startsWith("0x")) {
                    publicKey = "0x" + publicKey;
                }
            } catch (Exception e) {
                publicKey = "";
            }
            bean.setNodeId(publicKey);


            // 设置节点名称
            String nodeName = NODEID_TO_NAME.get(bean.getNodeId());
            if (null != nodeName) bean.setNodeName(nodeName);
            else bean.setNodeName("GenesisNode");


            //设置在当前区块高度中的节点分红比例
            try {
                CandidateContract candidateContract = platon.getCandidateContract(chainId);
                String nodeInfo = candidateContract.GetCandidateDetails(bean.getNodeId()).send();
                List<CandidateDto> candidateDtos = JSON.parseArray(nodeInfo, CandidateDto.class);
                if(candidateDtos.size()>0){
                    bean.setRewardRatio(BigDecimal.valueOf(candidateDtos.get(0).getFee()).divide(BigDecimal.valueOf(10000), 4, BigDecimal.ROUND_FLOOR).doubleValue());
                }else {
                    bean.setRewardRatio(0.0);
                }
            } catch (Exception e) {
                bean.setRewardRatio(0.0);
            }


            if (param.transactions.isEmpty() && param.transactionReceipts.isEmpty()) {
                // 如果交易及
                bean.setActualTxCostSum("0");
                bean.setBlockVoteAmount(0L);
                bean.setBlockCampaignAmount(0L);
                bean.setBlockVoteNumber(0L);
                return bean;
            }

            //actuakTxCostSum
            BigInteger sum = new BigInteger("0");
            //blockVoteAmount
            BigInteger voteAmount = new BigInteger("0");
            //blockCampaignAmount
            BigInteger campaignAmount = new BigInteger("0");
            bean.setBlockVoteNumber(0L);


            for (Transaction transaction : param.transactions) {
                if (null != param.transactionReceiptMap.get(transaction.getHash())) {
                    TransactionReceipt receipt = (TransactionReceipt) param.transactionReceiptMap.get(transaction.getHash());
                    sum = sum.add(receipt.getGasUsed().multiply(transaction.getGasPrice()));
                    AnalysisResult analysisResult = new AnalysisResult();
                    try {
                        analysisResult = TransactionAnalysis.analysis(transaction.getInput(), false);
                    } catch (Exception e) {
                        logger.error("BlockFilter Analysis Exception", e.getMessage());
                    }
                    if (org.springframework.util.StringUtils.isEmpty(analysisResult)) {
                        String type = TransactionAnalysis.getTypeName(analysisResult.getType());
                        if (TransactionTypeEnum.TRANSACTION_VOTE_TICKET.code.equals(type)) {
                            voteAmount = voteAmount.add(BigInteger.ONE);
                            if (receipt.getStatus().equals("0x1")) {
                                //get tickVoteContract vote event
                                List <TicketContract.VoteTicketEventEventResponse> eventEventResponses = ticketContract.getVoteTicketEventEvents(receipt);
                                String event = eventEventResponses.get(0).param1;
                                EventRes eventRes = JSON.parseObject(event, EventRes.class);
                                //event objcet is jsonString , transform jsonObject <EventRes>
                                //EventRes get Data
                                String res = eventRes.getData();
                                String[] strs = res.split(":");
                                bean.setBlockVoteNumber(Long.valueOf(strs[0]));
                            }
                        } else if (TransactionTypeEnum.TRANSACTION_CANDIDATE_DEPOSIT.code.equals(type)) {
                            campaignAmount = campaignAmount.add(BigInteger.ONE);
                        }
                    }
                    bean.setBlockVoteAmount(voteAmount != null ? voteAmount.longValue() : new BigInteger("0").longValue());
                    bean.setBlockCampaignAmount(voteAmount != null ? campaignAmount.longValue() : new BigInteger("0").longValue());
                    bean.setActualTxCostSum(sum.toString());
                }

            }
        }
        logger.debug("Time Consuming: {}ms", System.currentTimeMillis() - beginTime);
        return bean;
    }
}
