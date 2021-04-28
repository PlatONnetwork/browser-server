package com.platon.browser.analyzer;

import com.platon.browser.bean.Receipt;
import com.platon.browser.bean.ReceiptResult;
import com.platon.protocol.core.methods.response.PlatonBlock;
import com.platon.protocol.core.methods.response.Transaction;
import com.platon.browser.bean.CollectionBlock;
import com.platon.browser.bean.CollectionTransaction;
import com.platon.browser.exception.BeanCreateOrUpdateException;
import com.platon.browser.exception.BlankResponseException;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.exception.ContractInvokeException;
import com.platon.browser.utils.HexUtil;
import com.platon.browser.utils.NodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 区块分析器
 */
@Slf4j
@Component
public class BlockAnalyzer {

    @Transactional(rollbackFor = Exception.class)
    public CollectionBlock analyze(PlatonBlock.Block rawBlock, ReceiptResult receipt) throws ContractInvokeException, BlankResponseException, BeanCreateOrUpdateException {
        String nodeId;
        if (rawBlock.getNumber().longValue() == 0) {
            nodeId = "000000000000000000000000000000000";
        } else {
            nodeId = NodeUtil.getPublicKey(rawBlock);
        }
        nodeId = HexUtil.prefix(nodeId);
        CollectionBlock result = CollectionBlock.newInstance();
        result.setNum(rawBlock.getNumber().longValue())
                .setHash(rawBlock.getHash())
                .setPHash(rawBlock.getParentHash())
                .setSize(rawBlock.getSize().intValue())
                .setTime(new Date(rawBlock.getTimestamp().longValue()))
                .setExtra(rawBlock.getExtraData())
                .setMiner(rawBlock.getMiner())
                .setNodeId(nodeId)
                .setTxFee(rawBlock.getGasUsed().toString())
                .setGasLimit(rawBlock.getGasLimit().toString())
                .setGasUsed(rawBlock.getGasUsed().toString());
        result.setSeq(new AtomicLong(result.getNum() * 100000));
        if (rawBlock.getTransactions().isEmpty())
            return result;

        if (receipt.getResult().isEmpty())
            throw new BusinessException("区块[" + result.getNum() + "]有[" + rawBlock.getTransactions().size() + "]笔交易,但查询不到回执!");

        result.setReceiptMap(receipt.getMap());

        // 分析交易
        List<PlatonBlock.TransactionResult> transactionResults = rawBlock.getTransactions();

        List<Transaction> originTransactions = new ArrayList<>();
        if (receipt.getResult() != null && !receipt.getResult().isEmpty()) {
            Map<String, Receipt> receiptMap = receipt.getMap();
            for (PlatonBlock.TransactionResult tr : transactionResults) {
                PlatonBlock.TransactionObject to = (PlatonBlock.TransactionObject) tr.get();
                Transaction rawTransaction = to.get();
                originTransactions.add(rawTransaction);
            }
        }
        result.setOriginTransactions(originTransactions);

// 把分析交易的操作延后到CollectionEventHandler中处理
//        if (receipt.getResult() != null && !receipt.getResult().isEmpty()) {
//            Map<String, Receipt> receiptMap = receipt.getMap();
//            for (PlatonBlock.TransactionResult tr : transactionResults) {
//                PlatonBlock.TransactionObject to = (PlatonBlock.TransactionObject) tr.get();
//                Transaction rawTransaction = to.get();
//                CollectionTransaction transaction = transactionAnalyzer.analyze(result, rawTransaction, receiptMap.get(rawTransaction.getHash()));
//                // 把解析好的交易添加到当前区块的交易列表
//                result.getTransactions().add(transaction);
//                // 设置当前块的erc20交易数和erc721u交易数，以便更新network_stat表
//                result.setErc20TxQty(result.getErc20TxQty() + transaction.getErc20TxList().size());
//                result.setErc721TxQty(result.getErc721TxQty() + transaction.getErc721TxList().size());
//            }
//        }
        return result;
    }

}
