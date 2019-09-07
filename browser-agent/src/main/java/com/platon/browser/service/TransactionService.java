package com.platon.browser.service;

import com.alibaba.fastjson.JSON;
import com.platon.browser.client.PlatonClient;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dto.CustomBlock;
import com.platon.browser.dto.CustomTransaction;
import com.platon.browser.enums.InnerContractAddrEnum;
import com.platon.browser.enums.ReceiveTypeEnum;
import com.platon.browser.exception.BeanCreateOrUpdateException;
import com.platon.browser.task.BlockSyncTask;
import com.platon.browser.util.TxParamResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.PlatonGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

/**
 * @Auther: Chendongming
 * @Date: 2019/9/6 13:43
 * @Description: 交易分析服务类
 */
@Service
public class TransactionService {
    private static Logger logger = LoggerFactory.getLogger(TransactionService.class);

    @Autowired
    private PlatonClient client;

    /**
     * 并行分析区块及交易
     */
    public void analyze(List<CustomBlock> blocks) {
        // 对需要复杂分析的区块或交易信息，开启并行处理
        blocks.forEach(b -> {
            List <CustomTransaction> txList = b.getTransactionList();
            CountDownLatch latch = new CountDownLatch(txList.size());
            txList.forEach(tx -> BlockSyncTask.THREAD_POOL.submit(() -> {
                try {
                    updateTransaction(tx);
                } catch (Exception e) {
                    logger.error("更新交易信息错误：{}", e.getMessage());
                } finally {
                    latch.countDown();
                }
            }));
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // 汇总区块相关的统计信息
        class Stat {
            private int transferQty=0,stakingQty=0,proposalQty=0,delegateQty=0,txGasLimit=0;
            private BigDecimal txFee = BigDecimal.ZERO;
        }
        blocks.forEach(block->{
            Stat stat = new Stat();
            block.getTransactionList().forEach(transaction->{
                switch (transaction.getTypeEnum()){
                    case TRANSFER: // 转账交易，from地址转账交易数加一
                        stat.transferQty++;
                        break;
                    case INCREASE_STAKING:// 增加自有质押
                    case CREATE_VALIDATOR:// 创建验证人
                    case EXIT_VALIDATOR:// 退出验证人
                    case REPORT_VALIDATOR:// 举报验证人
                    case EDIT_VALIDATOR:// 编辑验证人
                        stat.stakingQty++; // 质押交易数总和
                        break;
                    case DELEGATE:// 发起委托
                    case UN_DELEGATE:// 撤销委托
                        stat.delegateQty++; // 委托交易数总和
                        break;
                    case CANCEL_PROPOSAL:// 取消提案
                    case CREATE_PROPOSAL_TEXT:// 创建文本提案
                    case CREATE_PROPOSAL_UPGRADE:// 创建升级提案
                    case DECLARE_VERSION:// 版本声明
                    case VOTING_PROPOSAL:// 提案投票
                        stat.proposalQty++; // 提案交易数总和
                        break;
                }
                // 累加当前区块内所有交易的手续费
                stat.txFee = stat.txFee.add(new BigDecimal(transaction.getActualTxCost()));
                // 累加当前区块内所有交易的GasLimit
                stat.txGasLimit = stat.txGasLimit+Integer.parseInt(transaction.getGasLimit());
            });
            block.setStatDelegateQty(stat.delegateQty);
            block.setStatProposalQty(stat.proposalQty);
            block.setStatStakingQty(stat.stakingQty);
            block.setStatTransferQty(stat.transferQty);
            block.setStatTxGasLimit(String.valueOf(stat.txGasLimit));
            block.setStatTxFee(stat.txFee.toString());
        });
    }

    /**
     * 分析区块获取code&交易回执
     */
    private void updateTransaction(CustomTransaction tx) throws IOException, BeanCreateOrUpdateException {
        try {
            // 查询交易回执
            PlatonGetTransactionReceipt result = client.getWeb3j().platonGetTransactionReceipt(tx.getHash()).send();
            Optional<TransactionReceipt> receipt = result.getTransactionReceipt();
            // 如果交易回执存在，则更新交易中与回执相关的信息
            if(receipt.isPresent()) tx.updateWithTransactionReceipt(receipt.get(), BlockChainConfig.INNER_CONTRACT_ADDR);
        }catch (IOException e){
            logger.error("查询交易[hash={}]的回执出错:{}",tx.getHash(),e.getMessage());
            throw e;
        }catch (BeanCreateOrUpdateException e){
            logger.error("更新交易[hash={}]的回执相关信息出错:{}",tx.getHash(),e.getMessage());
            throw e;
        }

        // 解析交易参数，补充交易中与交易参数相关的信息
        try {
            TxParamResolver.Result txParams = TxParamResolver.analysis(tx.getInput());
            tx.setTxInfo(JSON.toJSONString(txParams.getParam()));
            tx.setTxType(String.valueOf(txParams.getTxTypeEnum().code));
            tx.setReceiveType(ReceiveTypeEnum.CONTRACT.name().toLowerCase());
            if(null != tx.getValue() && ! InnerContractAddrEnum.ADDRESSES.contains(tx.getTo())){
                tx.setTxType(String.valueOf(CustomTransaction.TxTypeEnum.TRANSFER.code));
                tx.setReceiveType(ReceiveTypeEnum.ACCOUNT.name().toLowerCase());
            }
        }catch (Exception e){
            logger.error("交易[hash={}]的参数解析出错:{}",tx.getHash(),e.getMessage());
            throw e;
        }
    }
}
