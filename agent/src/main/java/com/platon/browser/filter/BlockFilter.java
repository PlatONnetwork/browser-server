package com.platon.browser.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.platon.browser.client.Web3jClient;
import com.platon.browser.common.dto.AnalysisResult;
import com.platon.browser.common.dto.agent.CandidateDto;
import com.platon.browser.common.util.TransactionAnalysis;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dto.EventRes;
import com.platon.browser.dto.NodeRankingDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.web3j.platon.contracts.TicketContract;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

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

    @Autowired
    private BlockMapper blockMapper;

    @Transactional
    public Block blockAnalysis ( EthBlock ethBlock, List <TransactionReceipt> transactionReceiptList, List <Transaction> transactionsList,
                                 String nodeInfoList ,BigInteger publicKey,Map<String,Object> transactionReceiptMap) throws Exception {
        Date beginTime = new Date();
        log.debug("[into BlockFilter !!!...]");
        log.debug("[blockChain chainId is ]: " + chainId);
        log.debug("[buildBlockStruct blockNumber is ]: " + ethBlock.getBlock().getNumber());
        Block block = build(ethBlock, transactionReceiptList, transactionsList,nodeInfoList,publicKey,transactionReceiptMap);
        Date endTime = new Date();
        String time = String.valueOf(endTime.getTime()-beginTime.getTime());
        log.debug("--------------------------------------blockAnalysis :" + time);
        return block;
    }



    public Block build ( EthBlock ethBlock, List <TransactionReceipt> transactionReceiptList, List <Transaction> transactionsList ,
                         String nodeInfoList,BigInteger publicKey,Map<String,Object> transactionReceiptMap) throws Exception {
        Block block = new Block();
        log.debug("[EthBlock info :]" + JSON.toJSONString(ethBlock));
        log.debug("[List <TransactionReceipt> info :]" + JSONArray.toJSONString(transactionReceiptList));
        log.debug("[ List <Transaction> info :]" + JSONArray.toJSONString(transactionsList));
        if (!StringUtils.isEmpty(ethBlock)) {
            block.setNumber(ethBlock.getBlock().getNumber().longValue());
            if (String.valueOf(ethBlock.getBlock().getTimestamp().longValue()).length() == 10) {
                block.setTimestamp(new Date(ethBlock.getBlock().getTimestamp().longValue() * 1000L));
            } else {
                block.setTimestamp(new Date(ethBlock.getBlock().getTimestamp().longValue()));
            }
            block.setSize(ethBlock.getBlock().getSize().intValue());
            block.setChainId(chainId);
            if (ethBlock.getBlock().getTransactions().size() <= 0) {
                block.setEnergonAverage(BigInteger.ZERO.toString());
            }else {
                block.setEnergonAverage(ethBlock.getBlock().getGasUsed().divide(new BigInteger(String.valueOf(ethBlock.getBlock().getTransactions().size()))).toString());
            }
            block.setHash(ethBlock.getBlock().getHash());
            block.setEnergonLimit(ethBlock.getBlock().getGasLimit().toString());
            block.setEnergonUsed(ethBlock.getBlock().getGasUsed().toString());
            block.setTransactionNumber(ethBlock.getBlock().getTransactions().size() > 0 ? ethBlock.getBlock().getTransactions().size() : new Integer(0));
            block.setCreateTime(new Date());
            block.setUpdateTime(new Date());
            block.setMiner(ethBlock.getBlock().getMiner());
            block.setExtraData(ethBlock.getBlock().getExtraData());
            block.setParentHash(ethBlock.getBlock().getParentHash());
            block.setNonce(ethBlock.getBlock().getNonce().toString());
            String nodeId = publicKey.toString(16);
            block.setNodeId(nodeId);
            List <CandidateDto> list = JSON.parseArray(nodeInfoList, CandidateDto.class);
            if(null != list && list.size() > 0 ){
                list.forEach(candidateDto -> {
                    if(publicKey.equals(new BigInteger(candidateDto.getCandidateId().replace("0x", ""), 16))){
                        NodeRankingDto nodeRankingDto = new NodeRankingDto();
                        nodeRankingDto.init(candidateDto);
                        block.setNodeName(nodeRankingDto.getName() == null ? "" : nodeRankingDto.getName());
                    }
                });
            }
            String rewardWei = getBlockReward(ethBlock.getBlock().getNumber().toString());
            block.setBlockReward(rewardWei);
            //actuakTxCostSum
            BigInteger sum = new BigInteger("0");
            //blockVoteAmount
            BigInteger voteAmount = new BigInteger("0");
            //blockCampaignAmount
            BigInteger campaignAmount = new BigInteger("0");
            if (transactionsList.size() <= 0 && transactionReceiptList.size() <= 0) {
                block.setActualTxCostSum("0");
                block.setBlockVoteAmount(0L);
                block.setBlockCampaignAmount(0L);
                block.setBlockVoteNumber(0L);
                log.debug("[Block info :]" + JSON.toJSONString(block));
                log.debug("[this block is An empty block , transaction null !!!...]");
                log.debug("[exit BlockFilter !!!...]");
                blockMapper.insert(block);
                return block;
            }
            for (Transaction transaction : transactionsList) {
                if(null != transactionReceiptMap.get(transaction.getHash())){
                    TransactionReceipt transactionReceipt = (TransactionReceipt) transactionReceiptMap.get(transaction.getHash());
                        sum = sum.add(transactionReceipt.getGasUsed().multiply(transaction.getGasPrice()));
                        AnalysisResult analysisResult = TransactionAnalysis.analysis(transaction.getInput(), true);
                        String type = TransactionAnalysis.getTypeName(analysisResult.getType());
                        if ("voteTicket".equals(type)) {
                            voteAmount.add(BigInteger.ONE);
                            //get tickVoteContract vote event
                            List <TicketContract.VoteTicketEventEventResponse> eventEventResponses =
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
            //insert struct<block> into database
            CacheTool.blocks.add(block);
            CacheTool.currentBlockNumber=block.getNumber();
            if(CacheTool.blocks.size()==100){
                blockMapper.batchInsert(CacheTool.blocks);
                CacheTool.blocks.clear();
            }
        }
        return block;
    }

    private static String getBlockReward ( String number ) {
        //ATP trasnfrom ADP
        BigDecimal rate = BigDecimal.valueOf(10L).pow(18);
        BigDecimal height = new BigDecimal(number);
        BigDecimal blockSumOnYear = BigDecimal.valueOf(24).multiply(BigDecimal.valueOf(3600)).multiply(BigDecimal.valueOf(365));
        BigDecimal definiteValue = new BigDecimal("1.025");
        BigDecimal base = BigDecimal.valueOf(100000000L);
        BigDecimal wheel = height.divide(blockSumOnYear, 0, java.math.BigDecimal.ROUND_HALF_DOWN);
        if (wheel.intValue() == 0) {
            //one period block
            BigDecimal result = BigDecimal.valueOf(25000000L).multiply(rate).divide(blockSumOnYear, 0, java.math.BigDecimal.ROUND_HALF_DOWN);
            return result.setScale(0).toString();
        }
        BigDecimal thisRound = base.multiply(rate).multiply(definiteValue.pow(wheel.intValue()));
        BigDecimal previousRound = base.multiply(rate).multiply(definiteValue.pow(wheel.subtract(BigDecimal.valueOf(1L)).intValue()));
        BigDecimal result = thisRound.subtract(previousRound).divide(blockSumOnYear);
        return result.setScale(0).toString();
    }


    public String valueConversion ( BigInteger value ) {
        BigDecimal valueDiec = new BigDecimal(value.toString());
        BigDecimal conversionCoin = valueDiec.divide(new BigDecimal("1000000000000000000"));
        return conversionCoin.toString();
    }


}