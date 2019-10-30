package com.platon.browser.service;

import com.alibaba.fastjson.JSON;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.result.Result;
import com.platon.browser.client.result.RpcTxReceiptResult;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dto.CustomBlock;
import com.platon.browser.dto.CustomTransaction;
import com.platon.browser.enums.InnerContractAddrEnum;
import com.platon.browser.enums.ReceiveTypeEnum;
import com.platon.browser.exception.BeanCreateOrUpdateException;
import com.platon.browser.util.TxParamResolver;
import com.platon.browser.utils.HexTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * @Auther: Chendongming
 * @Date: 2019/9/6 13:43
 * @Description: 交易分析服务类
 */
@Service
public class TransactionService {
    private static Logger logger = LoggerFactory.getLogger(TransactionService.class);

    @Autowired
    private PlatOnClient client;
    @Autowired
    private BlockChainConfig chainConfig;

    private ExecutorService executor;

    public void init(ExecutorService executor) {
        this.executor = executor;
    }

    /**
     * 并行分析区块及交易
     */
    public List<CustomBlock> analyze(List<CustomBlock> blocks) throws InterruptedException, BeanCreateOrUpdateException {
        // 对需要复杂分析的区块或交易信息，开启并行处理
        for (CustomBlock block:blocks){
            List <CustomTransaction> txList = block.getTransactionList();
            if(!txList.isEmpty()){
                RpcTxReceiptResult receiptResult = client.getReceiptByBlockNumber(block.getNumber());
                // 解码log
                receiptResult.resolve();
                Map<String, Result> map = receiptResult.getMap();
                for (CustomTransaction tx : txList) {
                    Result result = map.get(HexTool.prefix(tx.getHash()));
                    if (result != null) {
                        tx.setGasUsed(result.getGasUsed().toString());
                        tx.setActualTxCost(result.getGasUsed().multiply(new BigInteger(tx.getGasPrice())).toString());
                        tx.setFailReason(result.getFailReason());
                        if (chainConfig.getInnerContractAddr().contains(tx.getTo())) {
                            // 内置合约
                            tx.setTxReceiptStatus(result.getLogStatus());
                        } else {
                            tx.setTxReceiptStatus(result.getStatus());
                        }
                    }

                    try {
                        TxParamResolver.Result txParams = TxParamResolver.analysis(tx.getInput());
                        tx.setTxInfo(JSON.toJSONString(txParams.getParam()));
                        tx.setTxType(String.valueOf(txParams.getTxTypeEnum().getCode()));
                        tx.setReceiveType(ReceiveTypeEnum.CONTRACT.name().toLowerCase());
                        if (null != tx.getValue() && !InnerContractAddrEnum.getAddresses().contains(tx.getTo())) {
                            tx.setTxType(String.valueOf(CustomTransaction.TxTypeEnum.TRANSFER.getCode()));
                            tx.setReceiveType(ReceiveTypeEnum.ACCOUNT.name().toLowerCase());
                        }
                    } catch (Exception e) {
                        throw new BeanCreateOrUpdateException("交易[hash=" + tx.getHash() + "]的参数解析出错:" + e.getMessage());
                    }
                }

            }
        }

        // 汇总区块相关的统计信息
        class Stat {
            private int transferQty=0;
            private int stakingQty=0;
            private int proposalQty=0;
            private int delegateQty=0;
            private int txGasLimit=0;
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
                    default:
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
        return blocks;
    }

}
