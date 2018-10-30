package com.platon.browser.agent.job;

import com.alibaba.fastjson.JSONObject;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.platon.browser.agent.dto.BlockDto;
import com.platon.browser.agent.dto.TransactionDto;
import com.platon.browser.common.base.AppException;
import com.platon.browser.common.client.Web3jClient;
import com.platon.browser.common.constant.ConfigConst;
import com.platon.browser.common.enums.ErrorCodeEnum;
import com.platon.browser.common.spring.MQSender;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.mapper.BlockMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * User: dongqile
 * Date: 2018/10/24
 * Time: 17:28
 */
public class BlockSynchronizeJob extends AbstractTaskJob {

    /**
     * 区块同步任务
     * <p>
     * <p>
     * 1.根据web3j配置文件获取节点信息
     * 2.构建web3jclient
     * 3.查询DB中区块信息的最高块
     * a.首次（数据库无数据）
     * 从最高块到第零块，循环获取区块信息，插入队列
     * b.获取最高块
     * 比较DB最高块和链上最高块的差，循环获取差额的区块
     * 4.调用web3j，获取区块，分解信息，获取到区块和交易信息，DTO转换，插入队列信息
     */

    private static Logger log = LoggerFactory.getLogger(BlockSynchronizeJob.class);

    @Autowired
    private BlockMapper blockMapper;

    @Autowired
    private MQSender mqSender;

    @Override
    protected void doJob ( ShardingContext shardingContext ) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            EthBlockNumber ethBlockNumber = null;
            Web3j web3j = Web3jClient.getWeb3jClient();
            try {
                ethBlockNumber = web3j.ethBlockNumber().send();
            } catch (Exception e) {
                log.error("获取区块高度异常", e);
                throw new AppException(ErrorCodeEnum.BLOCKCHAIN_ERROR);
            }
            String blockNumber = ethBlockNumber.getBlockNumber().toString();
            Block block = blockMapper.selectNewestBlockInfo();
            if (null == block || block.equals("")) {
                //判断是否是首次
                for (int i = 0; i < Long.valueOf(blockNumber); i++) {
                    try {
                        BlockDto newBlock = buildStruct(i, web3j);
                        // 数据插入队列
                        String str = JSONObject.toJSONString(newBlock);
                        //chainId获取
                        mqSender.send(ConfigConst.getChainId(), "blockInfo", str);
                    } catch (Exception e) {
                        log.error("同步区块信息异常", e);
                        throw new AppException(ErrorCodeEnum.BLOCKCHAIN_ERROR);
                    }
                }
            } else {
                if (Long.valueOf(blockNumber) > block.getNumber()) {
                    //链上块增长
                    for (int i = block.getNumber().intValue(); i < Integer.parseInt(blockNumber); i++) {
                        try {
                            BlockDto newBlock = buildStruct(i, web3j);
                            // 数据插入队列
                            String str = JSONObject.toJSONString(newBlock);
                            //chainId获取
                            mqSender.send(ConfigConst.getChainId(), "blockInfo", str);
                        } catch (Exception e) {
                            log.error("同步区块信息异常", e);
                            throw new AppException(ErrorCodeEnum.BLOCKCHAIN_ERROR);
                        }
                    }
                } else if (Long.valueOf(blockNumber) < block.getNumber() || Long.valueOf(blockNumber) == block.getNumber()) {
                    //链上块无增长
                    log.info("无新区块");
                }
            }
        }catch (Exception e){
                log.error(e.getMessage());
        } finally {
            stopWatch.stop();
            log.info("BlockSynchronizeJob-->{}", stopWatch.shortSummary());
        }

    }

    //不同高度基础奖励不同
    private BigInteger getConstReward ( String height ) {
        if (Long.valueOf(height) >= 4370000) {
            return new BigInteger("3");
        }
        return new BigInteger("5");
    }

    // 计算区块中的交易费
    private BigInteger getGasInBlock ( List <TransactionDto> transactionList ) {
        BigInteger txfee = null;
        if (transactionList.size() > 0) {
            for (TransactionDto transactionDto : transactionList) {
                BigInteger price = transactionDto.getEnergonPrice();
                BigInteger used = transactionDto.getEnergonUsed();
                Integer num = transactionList.size();
                txfee = price.multiply(used).multiply(new BigInteger(num.toString()));
            }
            return txfee;
        }
        throw new AppException(ErrorCodeEnum.TX_ERROR);
    }

    //区块奖励
    private BigInteger getBlockReward ( String height, List <TransactionDto> transactionList ) {
        BigInteger reward = getConstReward(height);
        BigInteger txfee = getGasInBlock(transactionList);
        BigInteger blockReward = reward.add(txfee);
        return blockReward;
    }

    private BlockDto buildStruct ( int i, Web3j web3j ) throws Exception {
        DefaultBlockParameter defaultBlockParameter = new DefaultBlockParameterNumber(new BigInteger(String.valueOf(i)));
        EthBlock ethBlock = web3j.ethGetBlockByNumber(defaultBlockParameter, true).send();
        //交易相关
        List <TransactionDto> transactionDtolist = new ArrayList <>();
        List <EthBlock.TransactionResult> list = ethBlock.getBlock().getTransactions();
        for (EthBlock.TransactionResult transactionResult : list) {
            EthTransaction ethTransaction = web3j.ethGetTransactionByHash(transactionResult.toString()).send();
            Optional <Transaction> value = ethTransaction.getTransaction();
            if (!value.isPresent()) {
                throw new AppException(ErrorCodeEnum.BLOCKCHAIN_ERROR);
            }
            //todo:tx的gaslimit是gas
            Transaction transaction = value.get();
            TransactionDto transactionDto = new TransactionDto();
            EthGetTransactionReceipt ethGetTransactionReceipt = web3j.ethGetTransactionReceipt(transaction.getHash()).send();
            Optional <TransactionReceipt> transactionReceipt = ethGetTransactionReceipt.getTransactionReceipt();
            if (!transactionReceipt.isPresent()) {
                throw new AppException(ErrorCodeEnum.TX_ERROR);
            }
            TransactionReceipt receipt = transactionReceipt.get();
            transactionDto.setHash(transaction.getHash());
            transactionDto.setBlockHash(transaction.getBlockHash());
            transactionDto.setBlockNumber(transaction.getBlockNumber());
            transactionDto.setEnergonPrice(transaction.getGasPrice());
            transactionDto.setTransactionIndex(receipt.getTransactionIndex());
            transactionDto.setActualTxCoast(receipt.getGasUsed().multiply(transaction.getGasPrice()));
            transactionDto.setEnergonLimit(transaction.getGas());
            transactionDto.setFrom(transaction.getFrom());
            transactionDto.setTo(transaction.getTo());
            transactionDto.setTimestamp(ethBlock.getBlock().getTimestamp().longValue());
            transactionDto.setInput(transaction.getInput());
            transactionDto.setTransactionIndex(transaction.getTransactionIndex());
            transactionDto.setNonce(transaction.getNonce().toString());
            transactionDto.setValue(transaction.getValue().toString());
            transactionDto.setTxType("");
            transactionDtolist.add(transactionDto);
        }
            BlockDto newBlock = new BlockDto();
            newBlock.setHash(ethBlock.getBlock().getHash());
            newBlock.setNumber(ethBlock.getBlock().getNumber().intValue());
            newBlock.setParentHash(ethBlock.getBlock().getParentHash());
            newBlock.setNonce(ethBlock.getBlock().getNonce().toString());
            newBlock.setMiner(ethBlock.getBlock().getMiner());
            newBlock.setExtraData(ethBlock.getBlock().getExtraData());
            newBlock.setSize(ethBlock.getBlock().getSize().intValue());
            newBlock.setTimestamp(ethBlock.getBlock().getTimestamp().longValue());
            newBlock.setEnergonUsed(ethBlock.getBlock().getGasUsed());
            newBlock.setEnergonLimit(ethBlock.getBlock().getGasLimit());
            newBlock.setEnergonAverage(ethBlock.getBlock().getGasUsed().divide(new BigInteger(String.valueOf(list.size()))));
            newBlock.setBlockReward(getBlockReward(String.valueOf(newBlock.getNumber()), newBlock.getTransaction()).toString());
            return newBlock;
    }

/*    public static void main(String args[]){
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setEnergonPrice(new BigInteger("12321321321321"));
        transactionDto.setEnergonUsed(new BigInteger("12321321321"));
        List<TransactionDto> list = new ArrayList <>();
        list.add(transactionDto);
        BlockSynchronizeJob blockSynchronizeJob = new BlockSynchronizeJob();
        BigInteger a = blockSynchronizeJob.getGasInBlock(list);
        System.out.println(a);
    }*/
}