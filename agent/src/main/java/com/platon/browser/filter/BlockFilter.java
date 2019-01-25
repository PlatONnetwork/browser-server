package com.platon.browser.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.platon.browser.bean.BlockBean;
import com.platon.browser.client.Web3jClient;
import com.platon.browser.common.dto.AnalysisResult;
import com.platon.browser.common.util.TransactionAnalysis;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dto.EventRes;
import com.platon.browser.thread.AnalyseThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.web3j.platon.contracts.TicketContract;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import static com.platon.browser.job.NodeAnalyseJob.NODE_ID_TO_NAME;

/**
 * User: dongqile
 * Date: 2019/1/7
 * Time: 14:27
 */
@Component
public class BlockFilter {

    private static Logger log = LoggerFactory.getLogger(BlockFilter.class);

    @Autowired
    private Web3jClient web3jClient;

    @Value("${chain.id}")
    private String chainId;

    @Value("${platon.redis.key.block}")
    private String blockCacheKeyTemplate;

    @Value("${platon.redis.key.max-item}")
    private long maxItemNum;

    public Block analyse ( AnalyseThread.AnalyseParam param ) {

        long startTime = System.currentTimeMillis();

        EthBlock ethBlock = param.ethBlock;
        List <Transaction> transactionsList = param.transactions;
        List <TransactionReceipt> transactionReceiptList = param.transactionReceipts;
        BigInteger publicKey = param.publicKey;
        Map <String, Object> transactionReceiptMap = param.transactionReceiptMap;

        BlockBean block = new BlockBean();
        //log.debug("[EthBlock info :]" + JSON.toJSONString(ethBlock));
        log.debug("[List <TransactionReceipt> info :]" + JSONArray.toJSONString(transactionReceiptList));
        log.debug("[ List <Transaction> info :]" + JSONArray.toJSONString(transactionsList));
        if (!StringUtils.isEmpty(ethBlock)) {
            block.init(ethBlock);

            // 设置需要使用当前上下文的属性
            block.setChainId(chainId);
            block.setNodeId(publicKey.toString(16));
            // 设置节点名称
            String nodeName = NODE_ID_TO_NAME.get(block.getNodeId());
            block.setNodeName(nodeName);

            if (transactionsList.isEmpty() && transactionReceiptList.isEmpty()) {
                block.setActualTxCostSum("0");
                block.setBlockVoteAmount(0L);
                block.setBlockCampaignAmount(0L);
                block.setBlockVoteNumber(0L);
                log.debug("[Block info :]" + JSON.toJSONString(block));
                log.debug("[this block is An empty block , transaction null !!!...]");
                log.debug("[exit BlockFilter !!!...]");
                if((System.currentTimeMillis()-startTime)>0) log.debug("BlockFilter.analysis(): SECTION 3 -> {}",System.currentTimeMillis()-startTime);
//                blockMapper.insert(block);
                return block;
            }


            //actuakTxCostSum
            BigInteger sum = new BigInteger("0");
            //blockVoteAmount
            BigInteger voteAmount = new BigInteger("0");
            //blockCampaignAmount
            BigInteger campaignAmount = new BigInteger("0");
            for (Transaction transaction : transactionsList) {
                if (null != transactionReceiptMap.get(transaction.getHash())) {
                    TransactionReceipt transactionReceipt = (TransactionReceipt) transactionReceiptMap.get(transaction.getHash());
                    sum = sum.add(transactionReceipt.getGasUsed().multiply(transaction.getGasPrice()));
                    AnalysisResult analysisResult = TransactionAnalysis.analysis(transaction.getInput(), true);
                    String type = TransactionAnalysis.getTypeName(analysisResult.getType());
                    if ("voteTicket".equals(type)) {
                        voteAmount.add(BigInteger.ONE);
                        //get tickVoteContract vote event
                        List<TicketContract.VoteTicketEventEventResponse> eventEventResponses =
                                web3jClient.getTicketContract().getVoteTicketEventEvents(transactionReceipt);
                        String event = eventEventResponses.get(0).param1;
                        EventRes eventRes = JSON.parseObject(event, EventRes.class);
                        //event objcet is jsonString , transform jsonObject <EventRes>
                        //EventRes get Data
                        block.setBlockVoteNumber(Long.valueOf(eventRes.getData()));
                    } else if ("candidateDeposit".equals(type)) {
                        campaignAmount.add(BigInteger.ONE);
                    }
                    block.setBlockVoteAmount(voteAmount.longValue());
                    block.setBlockCampaignAmount(campaignAmount.longValue());
                    block.setActualTxCostSum(sum.toString());
                }
            }

        }
        return block;
    }
}