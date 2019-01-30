package com.platon.browser.filter;

import com.alibaba.fastjson.JSON;
import com.platon.browser.bean.BlockBean;
import com.platon.browser.client.PlatonClient;
import com.platon.browser.common.dto.AnalysisResult;
import com.platon.browser.common.util.CalculatePublicKey;
import com.platon.browser.common.util.TransactionAnalysis;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dto.EventRes;
import com.platon.browser.thread.AnalyseThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.platon.contracts.TicketContract;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.util.List;

import static com.platon.browser.utils.CacheTool.NODE_ID_TO_NAME;

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

    public Block analyse ( AnalyseThread.AnalyseParam param ) {
        BlockBean bean = new BlockBean();
        if (param.ethBlock!=null) {
            bean.init(param.ethBlock);

            // 设置需要使用当前上下文的属性
            bean.setChainId(platon.getChainId());
            String publicKey = null;
            try{
                publicKey = CalculatePublicKey.getPublicKey(param.ethBlock);
            }catch (Exception e){
                publicKey = "";
            }
            bean.setNodeId(publicKey);

            // 设置节点名称
            String nodeName = NODE_ID_TO_NAME.get(bean.getNodeId());
            bean.setNodeName(nodeName);

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
            for(Transaction transaction:param.transactions){
                if (null != param.transactionReceiptMap.get(transaction.getHash())) {
                    TransactionReceipt receipt = (TransactionReceipt) param.transactionReceiptMap.get(transaction.getHash());
                    sum = sum.add(receipt.getGasUsed().multiply(transaction.getGasPrice()));
                    AnalysisResult analysisResult = TransactionAnalysis.analysis(transaction.getInput(), true);
                    String type = TransactionAnalysis.getTypeName(analysisResult.getType());
                    if ("voteTicket".equals(type)) {
                        voteAmount=voteAmount.add(BigInteger.ONE);
                        //get tickVoteContract vote event
                        List<TicketContract.VoteTicketEventEventResponse> eventEventResponses = platon.getTicketContract().getVoteTicketEventEvents(receipt);
                        String event = eventEventResponses.get(0).param1;
                        EventRes eventRes = JSON.parseObject(event, EventRes.class);
                        //event objcet is jsonString , transform jsonObject <EventRes>
                        //EventRes get Data
                        bean.setBlockVoteNumber(Long.valueOf(eventRes.getData()));
                    } else if ("candidateDeposit".equals(type)) {
                        campaignAmount=campaignAmount.add(BigInteger.ONE);
                    }
                    bean.setBlockVoteAmount(voteAmount.longValue());
                    bean.setBlockCampaignAmount(campaignAmount.longValue());
                    bean.setActualTxCostSum(sum.toString());
                }
            }

        }
        return bean;
    }
}