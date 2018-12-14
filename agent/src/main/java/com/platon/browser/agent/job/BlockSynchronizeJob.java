package com.platon.browser.agent.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.github.pagehelper.PageHelper;
import com.platon.browser.agent.client.Web3jClient;
import com.platon.browser.common.base.AppException;
import com.platon.browser.common.dto.agent.BlockDto;
import com.platon.browser.common.dto.agent.TransactionDto;
import com.platon.browser.common.enums.ErrorCodeEnum;
import com.platon.browser.common.spring.MQSender;
import com.platon.browser.common.util.TransactionType;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.BlockExample;
import com.platon.browser.dao.mapper.BlockMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StopWatch;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.*;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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

    private static Long maxNubmer = 0L;

    @Value("${chain.id}")
    private String chainId;


    private static boolean isFirstRun = true;


    @PostConstruct
    public void init(){
        BlockExample condition = new BlockExample();
        condition.createCriteria().andChainIdEqualTo(chainId);
        condition.setOrderByClause("timestamp desc");
        PageHelper.startPage(1, 1);
        List <Block> blocks = blockMapper.selectByExample(condition);
        // 1、首先从数据库查询当前链的最高块号，作为采集起始块号
        // 2、如果查询不到则从0开始
        if(blocks.size()==0){
            maxNubmer = 0L;
        }else {
            maxNubmer = blocks.get(0).getNumber();
        }
    }

    @Override
    protected void doJob ( ShardingContext shardingContext ) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            if(isFirstRun){
                TimeUnit.SECONDS.sleep(30l);
                isFirstRun = false;
            }
            EthBlockNumber ethBlockNumber = null;
            Web3j web3j = Web3jClient.getWeb3jClient();
            try {
                ethBlockNumber = web3j.ethBlockNumber().send();
            } catch (Exception e) {
                log.error("Get blockNumber exception...", e);
                throw new AppException(ErrorCodeEnum.BLOCKCHAIN_ERROR);
            }
            String blockNumber = ethBlockNumber.getBlockNumber().toString();

            for(int i = maxNubmer.intValue() + 1 ; i <= Integer.parseInt(blockNumber); i ++){
                try {
                    BlockDto newBlock = buildStruct(i, web3j);
                    //chainId获取
                    if(newBlock.getNumber() > maxNubmer.intValue()){
                        mqSender.send(chainId, "block", newBlock);
                        log.info("BlockSynchronizeJob :{ DB blockNumber = " + newBlock.getNumber() + ", chainId =" + chainId +"}");
                    }
                } catch (Exception e) {
                    log.error("Synchronize block exception", e);
                    throw new AppException(ErrorCodeEnum.BLOCKCHAIN_ERROR);
                }
                TimeUnit.MILLISECONDS.sleep(100);
            }
            maxNubmer = Long.valueOf(blockNumber);
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
        if (transactionList != null && transactionList.size() > 0) {
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
        if (transactionList != null && transactionList.size() > 0) {
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
        if (!ethBlock.getBlock().getTransactions().equals(null) && ethBlock.getBlock().getTransactions().size() > 0) {
            List <EthBlock.TransactionResult> list = ethBlock.getBlock().getTransactions();
            for (EthBlock.TransactionResult transactionResult : list) {
                Transaction txList = (Transaction) transactionResult.get();
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
                BeanUtils.copyProperties(receipt, transactionDto);
                transactionDto.setHash(transaction.getHash());
                transactionDto.setTransactionIndex(receipt.getTransactionIndex());
                transactionDto.setEnergonPrice(transaction.getGasPrice());
                transactionDto.setEnergonLimit(transaction.getGas());
                transactionDto.setEnergonUsed(receipt.getGasUsed());
                transactionDto.setNonce(transaction.getNonce().toString());
                transactionDto.setValue(valueConversion(transaction.getValue()));
                if(String.valueOf(ethBlock.getBlock().getTimestamp().longValue()).length() == 10){
                    transactionDto.setTimestamp(ethBlock.getBlock().getTimestamp().longValue() * 1000L);
                }else {
                    transactionDto.setTimestamp(ethBlock.getBlock().getTimestamp().longValue());
                }
                //transactionDto.setInput(transaction.getInput());
                transactionDto.setTxReceiptStatus(null != receipt.getStatus() ? receipt.getStatus() : "0x0");
                transactionDto.setActualTxCoast(receipt.getGasUsed().multiply(transaction.getGasPrice()));
                String input = transactionDto.getInput();
                if (null != transaction.getTo()) {
                    EthGetCode ethGetCode = web3j.ethGetCode(transaction.getTo(), DefaultBlockParameterName.LATEST).send();
                    if ("0x".equals(ethGetCode.getCode())) {
                        transactionDto.setReceiveType("account");
                    } else {
                        transactionDto.setReceiveType("contract");
                    }
                } else {
                    transactionDto.setTo("0x");
                    transactionDto.setReceiveType("contract");
                }
                String type = TransactionType.geTransactionTyep(input);
                transactionDto.setTxType(type);
                transactionDtolist.add(transactionDto);
            }
        }
        BlockDto newBlock = new BlockDto();
        BeanUtils.copyProperties(ethBlock.getBlock(), newBlock);
        newBlock.setEnergonUsed(ethBlock.getBlock().getGasUsed());
        newBlock.setEnergonLimit(ethBlock.getBlock().getGasLimit());
        newBlock.setNonce(String.valueOf(ethBlock.getBlock().getNonce()));
        newBlock.setNumber(ethBlock.getBlock().getNumber().intValue());
        if(String.valueOf(ethBlock.getBlock().getTimestamp().longValue()).length() == 10){
            newBlock.setTimestamp(ethBlock.getBlock().getTimestamp().longValue() * 1000L);
        }else {
            newBlock.setTimestamp(ethBlock.getBlock().getTimestamp().longValue());
        }
        if (ethBlock.getBlock().getTransactions().size() > 0) {
            newBlock.setEnergonAverage(ethBlock.getBlock().getGasUsed().divide(new BigInteger(String.valueOf(ethBlock.getBlock().getTransactions().size()))));
        } else
            newBlock.setEnergonAverage(BigInteger.ZERO);
        newBlock.setBlockReward(getBlockReward(String.valueOf(newBlock.getNumber()), newBlock.getTransaction()));
        newBlock.setTransaction(transactionDtolist);
        newBlock.setTransactionNumber(transactionDtolist.size() > 0 ? transactionDtolist.size() : new Integer(0));
        return newBlock;

    }

    public String valueConversion ( BigInteger value ) {
        BigDecimal valueDiec = new BigDecimal(value.toString());
        BigDecimal conversionCoin = valueDiec.divide(new BigDecimal("1000000000000000000"));
        return conversionCoin.toString();
    }



}