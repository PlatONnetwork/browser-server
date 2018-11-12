package com.platon.browser.agent.job;

import com.alibaba.fastjson.JSONObject;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.github.pagehelper.PageHelper;
import com.platon.browser.common.base.AppException;
import com.platon.browser.common.client.Web3jClient;
import com.platon.browser.common.constant.ConfigConst;
import com.platon.browser.common.dto.agent.BlockDto;
import com.platon.browser.common.dto.agent.TransactionDto;
import com.platon.browser.common.enums.ErrorCodeEnum;
import com.platon.browser.common.spring.MQSender;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.BlockExample;
import com.platon.browser.dao.mapper.BlockMapper;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.rlp.RlpDecoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Numeric;

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

    private static final String WEB3_PROPER = "classpath:web3j.properties.xml";

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
            BlockExample condition = new BlockExample();
            condition.setOrderByClause("timestamp desc");
            PageHelper.startPage(1, 1);
            List <Block> blocks = blockMapper.selectByExample(condition);
            if (blocks.size() > 0) {
                if (Long.valueOf(blockNumber) > blocks.get(0).getNumber()) {
                    //链上块增长
                    for (int i = blocks.get(0).getNumber().intValue()+1 ; i < Integer.parseInt(blockNumber); i++) {
                        try {
                            BlockDto newBlock = buildStruct(i, web3j);
                            //chainId获取
                            mqSender.send(ConfigConst.getChainId(), "block", newBlock);
                        } catch (Exception e) {
                            log.error("同步区块信息异常", e);
                            throw new AppException(ErrorCodeEnum.BLOCKCHAIN_ERROR);
                        }
                    }
                } else if (Long.valueOf(blockNumber) < blocks.get(0).getNumber() || Long.valueOf(blockNumber) == blocks.get(0).getNumber()) {
                    //链上块无增长
                    log.info("无新区块");
                }
            } else {
                //判断是否是首次
                for (int i = 1; i < Long.valueOf(blockNumber); i++) {
                    try {
                        BlockDto newBlock = buildStruct(i, web3j);
                        //chainId获取
                        mqSender.send(ConfigConst.getChainId(), "block", newBlock);
                    } catch (Exception e) {
                        log.error("同步区块信息异常", e);
                        throw new AppException(ErrorCodeEnum.BLOCKCHAIN_ERROR);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            stopWatch.stop();
            log.debug("BlockSynchronizeJob-->{}", stopWatch.shortSummary());
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
        if (transactionList != null  && transactionList.size() > 0) {
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
    private String getBlockReward ( String height, List <TransactionDto> transactionList ) {
        BigInteger reward = getConstReward(height);
        BigInteger txfee = BigInteger.ZERO;
        if (transactionList != null  && transactionList.size() > 0) {
            txfee = getGasInBlock(transactionList);
        }
        BigInteger blockReward = reward.add(txfee);
        return String.valueOf(blockReward);
    }

    private BlockDto buildStruct ( int i, Web3j web3j ) throws Exception {
        DefaultBlockParameter defaultBlockParameter = new DefaultBlockParameterNumber(new BigInteger(String.valueOf(i)));
        EthBlock ethBlock = web3j.ethGetBlockByNumber(defaultBlockParameter, true).send();
        //交易相关
        List <TransactionDto> transactionDtolist = new ArrayList <>();
        if(!ethBlock.getBlock().getTransactions().equals(null) && ethBlock.getBlock().getTransactions().size() > 0){
            List <EthBlock.TransactionResult> list = ethBlock.getBlock().getTransactions();
            for (EthBlock.TransactionResult transactionResult : list) {
                Transaction txList = (Transaction)transactionResult.get();
                EthTransaction ethTransaction = web3j.ethGetTransactionByHash(txList.getHash()).send();
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
                BeanUtils.copyProperties(receipt,transactionDto);
                transactionDto.setHash(transaction.getHash());
                transactionDto.setTransactionIndex(receipt.getTransactionIndex());
                transactionDto.setEnergonPrice(transaction.getGasPrice());
                transactionDto.setEnergonLimit(transaction.getGas());
                transactionDto.setEnergonUsed(receipt.getGasUsed());
                transactionDto.setNonce(transaction.getNonce().toString());
                transactionDto.setValue(transaction.getValue().toString());
                transactionDto.setTxReceiptStatus(receipt.getStatus());
                transactionDto.setActualTxCoast(receipt.getGasUsed().multiply(transaction.getGasPrice()));
                String input = transactionDto.getInput();
                if(null != transaction.getTo()){
                    EthGetCode ethGetCode  = web3j.ethGetCode(transaction.getTo(), DefaultBlockParameterName.LATEST).send();
                    if(!ethGetCode.getCode().equals("0x")){
                        transactionDto.setReceiveType("contract");
                    }else {
                        transactionDto.setReceiveType("account");
                    }
                }
                transactionDto.setReceiveType("contract");
                String type = geTransactionTyep(input);
                transactionDto.setTxType(type);
                transactionDtolist.add(transactionDto);
            }
        }
        BlockDto newBlock = new BlockDto();
        BeanUtils.copyProperties(ethBlock.getBlock(),newBlock);
        newBlock.setEnergonUsed(ethBlock.getBlock().getGasUsed());
        newBlock.setEnergonLimit(ethBlock.getBlock().getGasLimit());
        newBlock.setNonce(String.valueOf(ethBlock.getBlock().getNonce()));
        newBlock.setNumber(ethBlock.getBlock().getNumber().intValue());
        newBlock.setTimestamp(ethBlock.getBlock().getTimestamp().longValue());
        if (ethBlock.getBlock().getTransactions().size() > 0) {
            newBlock.setEnergonAverage(ethBlock.getBlock().getGasUsed().divide(new BigInteger(String.valueOf(ethBlock.getBlock().getTransactions().size()))));
        } else
        newBlock.setEnergonAverage(BigInteger.ZERO);
        newBlock.setBlockReward(getBlockReward(String.valueOf(newBlock.getNumber()),newBlock.getTransaction()));
        newBlock.setTransaction(transactionDtolist);
        newBlock.setTransactionNumber(transactionDtolist.size() > 0 ? transactionDtolist.size() : new Integer(0));
        return newBlock;

    }


    private String geTransactionTyep ( String input ) throws Exception {
        String type = null;
        if(StringUtils.isNotEmpty(input)){
            RlpList rlpList = RlpDecoder.decode(Hex.decode(input));
            List <RlpType> rlpTypes = rlpList.getValues();
            RlpList rlpList1 = (RlpList) rlpTypes.get(0);
            RlpString rlpString = (RlpString) rlpList1.getValues().get(0);
            String typecode = Hex.toHexString(rlpString.getBytes());
            byte[] hexByte = Numeric.hexStringToByteArray(typecode);
            //todo:置换web3j jar包platon版本
            switch (type) {
                case "0":
                    //主币交易转账
                    type = "transfer";
                    break;
                case "1":
                    //合约发布
                    type = "contractCreate";
                    break;
                case "2":
                    //合约调用
                    type = "transactionExecute";
                    break;
                case "3":
                    //投票
                    type = "vote";
                    break;
                case "4":
                    //权限
                    type = "authorization";
                    break;
                case "5":
                    //MPC交易
                    type = "MPCtransaction";
                    break;
            }
            return type;
        }
        return type = "transfer";

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