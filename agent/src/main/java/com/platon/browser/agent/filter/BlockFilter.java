package com.platon.browser.agent.filter;

import com.alibaba.fastjson.JSON;
import com.platon.browser.agent.client.Web3jClient;
import com.platon.browser.common.util.TransactionType;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dto.EventRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.web3j.platon.contracts.TicketContract;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.AsciiUtil;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * User: dongqile
 * Date: 2019/1/7
 * Time: 14:27
 */
public class BlockFilter {

    private static Logger log = LoggerFactory.getLogger(BlockFilter.class);

    @Autowired
    private BlockMapper blockMapper;

    @Autowired
    private Web3jClient web3jClient;

    @Value("${chain.id}")
    private String chainId;

    public boolean buildStruct ( EthBlock ethBlock, List <TransactionReceipt> transactionReceiptList, List <Transaction> transactionsList ) throws Exception {
        Block block = new Block();
        log.debug("[into BlockFilter !!!...]");
        log.debug("[blockChain chainId is ]: " + chainId);
        log.debug("[buildBlockStruct blockNumber is ]: " + ethBlock.getBlock().getNumber());
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
            //block.setBlockReward();
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
                log.debug("[this block is An empty block , transaction null !!!...]");
                log.debug("[exit BlockFilter !!!...]");
                return true;
            }
            for (Transaction transaction : transactionsList) {
                for (TransactionReceipt transactionReceipt : transactionReceiptList) {
                    if (transaction.getHash().equals(transactionReceipt.getTransactionHash())) {
                        sum = sum.add(transactionReceipt.getGasUsed().multiply(transaction.getGasPrice()));
                        String type = TransactionType.geTransactionTyep(transaction.getInput());
                        if ("voteTicket".equals(type)) {
                            voteAmount.add(BigInteger.ONE);
                            //get tickVoteContract vote event
                            List <TicketContract.VoteTicketEventEventResponse> eventEventResponses =
                                    web3jClient.getTicketContract().getVoteTicketEventEvents(transactionReceipt);
                            String event = eventEventResponses.get(0).param1;
                            EventRes eventRes = JSON.parseObject(event, EventRes.class);
                            //event objcet is jsonString , transform jsonObject <EventRes>
                            //EventRes get Data ---> is ascii transform
                            int count = AsciiUtil.toAscii(eventRes.getData());
                            block.setBlockVoteNumber(Long.valueOf(count));
                        } else if ("candidateDeposit".equals(type)) {
                            campaignAmount.add(BigInteger.ONE);
                        }
                    }
                    block.setBlockVoteAmount(voteAmount.longValue());
                    block.setBlockCampaignAmount(campaignAmount.longValue());
                    block.setActualTxCostSum(sum.toString());
                }
            }
            log.debug("[BlockFilter succ !!!...]");
            log.debug("[exit BlockFilter !!!...]");
            return true;
        }
        log.debug("[BlockFilter build blockStruct fail !!!...]");
        return false;
    }


    private String getBlockReward ( String height ) {
        BigInteger reward = BigInteger.ZERO;
        BigInteger definiteValue = new BigInteger("1.025");
        BigInteger fx = new BigInteger("1000000000").multiply(definiteValue);

        /*BigInteger reward = getConstReward(height);
        BigInteger txfee = BigInteger.ZERO;
        if (transactionList != null && transactionList.size() > 0) {
            txfee = getGasInBlock(transactionList);
        }
        BigInteger blockReward = reward.add(txfee);*/
        return String.valueOf(fx);
    }

    public static void main ( String[] args ) {
        BigInteger reward = BigInteger.ZERO;
        BigInteger definiteValue = new BigInteger("1.025");
        BigInteger x = new BigInteger("112333333120088");
        BigInteger period = BigInteger.valueOf(24).multiply(BigInteger.valueOf(3600).multiply(BigInteger.valueOf(365)));
        BigInteger hight = new BigInteger("");
        BigInteger wheel = hight.mod(period);
        BigInteger fx = new BigInteger("1000000000").multiply(definiteValue);
        //fx.pow(x.divide(y).intValue());
        /*BigInteger reward = getConstReward(height);
        BigInteger txfee = BigInteger.ZERO;
        if (transactionList != null && transactionList.size() > 0) {
            txfee = getGasInBlock(transactionList);
        }
        BigInteger blockReward = reward.add(txfee);*/
        //System.out.println(y);
        System.out.println(x);
        int count = AsciiUtil.toAscii("/n");
        System.out.println(count);
    }
}