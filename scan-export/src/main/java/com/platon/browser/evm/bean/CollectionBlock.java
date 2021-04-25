package com.platon.browser.evm.bean;

import com.platon.protocol.core.methods.response.PlatonBlock;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.exception.BeanCreateOrUpdateException;
import com.platon.browser.utils.HexUtil;
import com.platon.browser.utils.NodeUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

public class CollectionBlock extends Block {
    private CollectionBlock(){}
    public static CollectionBlock newInstance(){
        CollectionBlock block = new CollectionBlock();
        Date date = new Date();
        block.setCreTime(date)
            .setUpdTime(date)
            .setDQty(0)
            .setPQty(0)
            .setSQty(0)
            .setTranQty(0)
            .setTxQty(0)
            .setReward(BigDecimal.ZERO.toString())
            .setGasLimit(BigDecimal.ZERO.toString())
            .setGasUsed(BigDecimal.ZERO.toString())
            .setTxFee(BigDecimal.ZERO.toString())
            .setSize(0)
            .setTxGasLimit(BigDecimal.ZERO.toString());
        return block;
    }

    @SuppressWarnings("rawtypes")
	public CollectionBlock updateWithRawBlockAndReceiptResult(PlatonBlock.Block block) throws BeanCreateOrUpdateException, IOException {
        String nodeId;
        if(block.getNumber().longValue()==0){
            nodeId="000000000000000000000000000000000";
        }else{
            nodeId = NodeUtil.getPublicKey(block);
        }
        nodeId = HexUtil.prefix(nodeId);
        this.setNum(block.getNumber().longValue())
            .setHash(block.getHash())
            .setPHash(block.getParentHash())
            .setSize(block.getSize().intValue())
            .setTime(new Date(block.getTimestamp().longValue()))
            .setExtra(block.getExtraData())
            .setMiner(block.getMiner())
            .setNodeId(nodeId)
            .setGasLimit(block.getGasLimit().toString())
            .setGasUsed(block.getGasUsed().toString())
            .setTxQty(block.getTransactions().size());

//        if(block.getTransactions().isEmpty()) return this;

//        if(receiptResult.getResult().isEmpty()) throw new BusinessException("区块["+this.getNum()+"]有["+block.getTransactions().size()+"]笔交易,但查询不到回执!");

        // 解析交易
//        List<PlatonBlock.TransactionResult> transactionResults = block.getTransactions();
//        if(receiptResult.getResult()!=null&&!receiptResult.getResult().isEmpty()){
//            Map<String,Receipt> receiptMap=receiptResult.getMap();
//            for (PlatonBlock.TransactionResult tr : transactionResults) {
//                PlatonBlock.TransactionObject to = (PlatonBlock.TransactionObject) tr.get();
//                Transaction rawTransaction = to.get();
//                CollectionTransaction transaction = CollectionTransaction.newInstance()
//                        .updateWithBlock(this)
//                        .updateWithRawTransaction(rawTransaction)
//                        .updateWithBlockAndReceipt(this,receiptMap.get(rawTransaction.getHash()));
//                transactions.add(transaction);
//            }
//        }
        return this;
    }
}
