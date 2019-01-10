package com.platon.browser.agent.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.platon.browser.agent.client.Web3jClient;
import com.platon.browser.common.base.AppException;
import com.platon.browser.common.dto.agent.PendingTransactionDto;
import com.platon.browser.common.enums.ErrorCodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.util.StopWatch;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetCode;
import org.web3j.protocol.core.methods.response.EthPendingTransactions;
import org.web3j.protocol.core.methods.response.Transaction;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: dongqile
 * Date: 2018/10/25
 * Time: 18:07
 */
@DependsOn("dBStorageService")
public class PendingTxSynchronizeJob extends AbstractTaskJob{

    /**
     * 挂起交易同步任务
     * 1.根据web3j配置文件获取节点信息
     * 2.构建web3jclient
     * 3.同步链上挂起交易列表
     * 4.数据整合推送至rabbitMQ队列
     */

    private static Logger log = LoggerFactory.getLogger(PendingTxSynchronizeJob.class);

    @Value("${chain.id}")
    private String chainId;
/*

    @Autowired
    private MQSender mqSender;
*/

    @Override
    protected void doJob ( ShardingContext shardingContext ) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            Web3j web3j = Web3jClient.getWeb3jClient();
            EthPendingTransactions ethPendingTransactions = web3j.ethPendingTx().send();
            List<Transaction> list = ethPendingTransactions.getTransactions();
            List<PendingTransactionDto> pendingTransactionDtoList = new ArrayList <>();
            if(!list.equals(null) && list.size() > 0){
                for(Transaction transaction : list){
                    PendingTransactionDto pendingTransactionDto = new PendingTransactionDto();
                    pendingTransactionDto.setHash(transaction.getHash());
                    pendingTransactionDto.setFrom(transaction.getFrom());
                    pendingTransactionDto.setTo(transaction.getTo());
                    if (null != transaction.getTo()) {
                        EthGetCode ethGetCode = web3j.ethGetCode(transaction.getTo(), DefaultBlockParameterName.LATEST).send();
                        if ("0x".equals(ethGetCode.getCode())) {
                            pendingTransactionDto.setReceiveType("account");
                        } else {
                            pendingTransactionDto.setReceiveType("contract");
                        }
                    }else {
                        pendingTransactionDto.setTo("0x");
                    }
                    pendingTransactionDto.setEnergonLimit(transaction.getGas());
                    pendingTransactionDto.setEnergonPrice(transaction.getGasPrice());
                    pendingTransactionDto.setNonce(transaction.getNonce().toString());
                    pendingTransactionDto.setTimestamp(new Date().getTime());
                    pendingTransactionDto.setValue(valueConversion(transaction.getValue()));
                    pendingTransactionDto.setInput(transaction.getInput());
         /*           String type = TransactionType.geTransactionTyep(!transaction.getInput().equals(null) ? transaction.getInput() : "0x");
                    pendingTransactionDto.setTxType(type);*/
                    pendingTransactionDtoList.add(pendingTransactionDto);
                }
                //mqSender.send(chainId, MqMessageTypeEnum.PENDING.name(), pendingTransactionDtoList);
            }
            log.info("newest pendingtx is null");
        }catch (Exception e){
            log.error(e.getMessage());
            throw new AppException(ErrorCodeEnum.PENDINGTX_ERROR);
        } finally {
            stopWatch.stop();
            log.info("PendingTxSynchrinizeJob-->{}", stopWatch.shortSummary());
        }
    }

    public String valueConversion(BigInteger value){
        BigDecimal valueDiec = new BigDecimal(value.toString());
        BigDecimal conversionCoin = valueDiec.divide(new BigDecimal("1000000000000000000"));
        return  conversionCoin.toString();
    }

}