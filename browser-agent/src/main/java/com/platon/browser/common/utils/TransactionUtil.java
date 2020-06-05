package com.platon.browser.common.utils;

import com.platon.browser.client.PPosInvokeContractInput;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.util.decode.innercontract.InnerContractDecodeUtil;
import com.platon.browser.util.decode.innercontract.InnerContractDecodedResult;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 虚拟交易工具
 */
public class TransactionUtil {
    /**
     * 根据合约内部调用PPOS的输入信息生成虚拟PPOS交易列表
     * @param block 合约调用交易所在区块信息
     * @param parentTx 合约调用交易本身
     * @param invokeContractInput 合约内部调用PPOS操作的输入信息
     * @return
     */
    public static List<Transaction> getVirtualTxList(Block block, Transaction parentTx, PPosInvokeContractInput invokeContractInput){
        List<Transaction> transactionList = new ArrayList<>();
        if(invokeContractInput==null) return transactionList;
        List<String> inputs = invokeContractInput.getInput();
        for (int i=0;i<inputs.size();i++) {
            String input = inputs.get(i);
            InnerContractDecodedResult result = InnerContractDecodeUtil.decode(input, Collections.emptyList());
            Transaction tx = new Transaction();
            BeanUtils.copyProperties(parentTx, tx);
            tx.setStatus(parentTx.getStatus());
            tx.setFrom(invokeContractInput.getFrom());
            tx.setTo(invokeContractInput.getTo());
            tx.setToType(Transaction.ToTypeEnum.INNER_CONTRACT.getCode());
            tx.setHash(parentTx.getHash() + "-" + i);
            tx.setType(result.getTypeEnum().getCode());
            tx.setIndex(i);
            tx.setInput(input);
            tx.setInfo(result.getParam().toJSONString());
            tx.setSeq((long) i);
            transactionList.add(tx);
        }
        return transactionList;
    }
}
