package com.platon.browser.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.platon.browser.client.Web3jClient;
import com.platon.browser.common.dto.AnalysisResult;
import com.platon.browser.common.util.TransactionAnalysis;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dto.EventRes;
import com.platon.browser.service.RedisCacheService;
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
import org.web3j.utils.AsciiUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Autowired
    private RedisCacheService redisCacheService;


    public Block BlockFilter ( EthBlock ethBlock, List <TransactionReceipt> transactionReceiptList, List <Transaction> transactionsList ) throws Exception {
        log.debug("[into BlockFilter !!!...]");
        log.debug("[blockChain chainId is ]: " + chainId);
        log.debug("[buildBlockStruct blockNumber is ]: " + ethBlock.getBlock().getNumber());
        Block block = build(ethBlock,transactionReceiptList,transactionsList);
        return block;
    }




    @Transactional
    public Block build ( EthBlock ethBlock, List <TransactionReceipt> transactionReceiptList, List <Transaction> transactionsList ) {
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
            }
            block.setHash(ethBlock.getBlock().getHash());
            block.setEnergonAverage(ethBlock.getBlock().getGasUsed().divide(new BigInteger(String.valueOf(ethBlock.getBlock().getTransactions().size()))).toString());
            block.setEnergonLimit(ethBlock.getBlock().getGasLimit().toString());
            block.setEnergonUsed(ethBlock.getBlock().getGasUsed().toString());
            block.setTransactionNumber(ethBlock.getBlock().getTransactions().size() > 0 ? ethBlock.getBlock().getTransactions().size() : new Integer(0));
            block.setCreateTime(new Date());
            block.setUpdateTime(new Date());
            block.setMiner(ethBlock.getBlock().getMiner());
            block.setExtraData(ethBlock.getBlock().getExtraData());
            block.setParentHash(ethBlock.getBlock().getParentHash());
            block.setNonce(ethBlock.getBlock().getNonce().toString());
            String rewardWei = getBlockReward(ethBlock.getBlock().getNumber().toString());
            block.setBlockReward(valueConversion(new BigInteger(rewardWei)));
            //actuakTxCostSum
            BigInteger sum = new BigInteger("0");
            //blockVoteAmount
            BigInteger voteAmount = new BigInteger("0");
            //blockCampaignAmount
            BigInteger campaignAmount = new BigInteger("0");
            if (transactionsList.size() < 0 && transactionReceiptList.size() < 0) {
                block.setActualTxCostSum("0");
                block.setBlockVoteAmount(0L);
                block.setBlockCampaignAmount(0L);
                block.setBlockVoteNumber(0L);
                log.debug("[Block info :]" + JSON.toJSONString(block));
                log.debug("[this block is An empty block , transaction null !!!...]");
                log.debug("[exit BlockFilter !!!...]");
                Set <Block> set = new HashSet <>();
                set.add(block);
                redisCacheService.updateBlockCache(chainId, set);
                return block;
            }
            for (Transaction transaction : transactionsList) {
                for (TransactionReceipt transactionReceipt : transactionReceiptList) {
                    if (transaction.getHash().equals(transactionReceipt.getTransactionHash())) {
                        sum = sum.add(transactionReceipt.getGasUsed().multiply(transaction.getGasPrice()));
                        AnalysisResult analysisResult = TransactionAnalysis.analysis(transaction.getInput(),true);
                        String type =  TransactionAnalysis.getTypeName(analysisResult.getType());
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
                    }
                    block.setBlockVoteAmount(voteAmount.longValue());
                    block.setBlockCampaignAmount(campaignAmount.longValue());
                    block.setActualTxCostSum(sum.toString());
                }
            }
            //insert struct<block> into database
            blockMapper.insert(block);
            Set <Block> set = new HashSet <>();
            set.add(block);
            //insert struct<block> into redis
            redisCacheService.updateBlockCache(chainId, set);
        }
        return block;
    }

    private static  String getBlockReward ( String number ) {
        //ATP trasnfrom ADP
        BigDecimal rate = BigDecimal.valueOf(10L).pow(18);
        BigDecimal height = new BigDecimal(number);
        BigDecimal blockSumOnYear = BigDecimal.valueOf(24).multiply(BigDecimal.valueOf(3600)).multiply(BigDecimal.valueOf(365));
        BigDecimal definiteValue = new BigDecimal("1.025");
        BigDecimal base = BigDecimal.valueOf(100000000L);
        BigDecimal wheel = height.divide(blockSumOnYear, 0, java.math.BigDecimal.ROUND_HALF_DOWN);
        if (wheel.intValue() == 0) {
            //one period block
            BigDecimal result = BigDecimal.valueOf(25000000L).multiply(rate).divide(blockSumOnYear,0, java.math.BigDecimal.ROUND_HALF_DOWN);
            return result.setScale(0).toString();
        }
        BigDecimal thisRound = base.multiply(rate).multiply(definiteValue.pow(wheel.intValue()));
        BigDecimal previousRound = base.multiply(rate).multiply(definiteValue.pow(wheel.subtract(BigDecimal.valueOf(1L)).intValue()));
        BigDecimal result = thisRound.subtract(previousRound).divide(blockSumOnYear);
        return result.setScale(0).toString();
    }



    public String valueConversion(BigInteger value){
        BigDecimal valueDiec = new BigDecimal(value.toString());
        BigDecimal conversionCoin = valueDiec.divide(new BigDecimal("1000000000000000000"));
        return  conversionCoin.toString();
    }

    public static  void main ( String[] args ) {
        BigInteger reward = BigInteger.ZERO;
        BigDecimal hight = new BigDecimal("1");
        String a = getBlockReward("0");
        BigDecimal y = BigDecimal.valueOf(24).multiply(BigDecimal.valueOf(3600)).multiply(BigDecimal.valueOf(356));
        BigDecimal wheel = hight.divide(y, 0, java.math.BigDecimal.ROUND_HALF_DOWN);


        BigDecimal definiteValue = new BigDecimal("1.025");
        //BigDecimal a = definiteValue.pow(wheel.intValue());
        //BigDecimal res = BigDecimal.valueOf(1000000000l).multiply(a);
        //System.out.println(res.setScale(0));
        BigDecimal ab = BigDecimal.valueOf(25000000L).divide(y, 5);
        BigDecimal abc = BigDecimal.valueOf(10L).pow(18);

        System.out.println(BigDecimal.valueOf(25000000L).multiply(abc).divide(y, 0));
        int count = AsciiUtil.toAscii("/n");
    }
}