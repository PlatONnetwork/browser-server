package com.platon.browser.cache;

import com.platon.browser.bean.PPosInvokeContractInput;
import com.platon.browser.utils.HexUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PPosInvokeContractInputCache {
    private PPosInvokeContractInputCache() {}
    private static final Map<Long, List<PPosInvokeContractInput>> BLOCK_NUMBER_TO_PPOS_INPUT = new HashMap<>();
    private static final Map<String,PPosInvokeContractInput> CONTRACT_INVOKE_TX_HASH_TO_PPOS_INPUT = new HashMap<>();
    public static void update(Long blockNumber, List<PPosInvokeContractInput> pPosInvokeContractInput4Blocks){
        BLOCK_NUMBER_TO_PPOS_INPUT.clear();
        CONTRACT_INVOKE_TX_HASH_TO_PPOS_INPUT.clear();
        BLOCK_NUMBER_TO_PPOS_INPUT.put(blockNumber,pPosInvokeContractInput4Blocks);
        pPosInvokeContractInput4Blocks.forEach(e-> CONTRACT_INVOKE_TX_HASH_TO_PPOS_INPUT.put(HexUtil.prefix(e.getTxHash()),e));
    }

    public static boolean hasCache(Long blockNumber){
        return BLOCK_NUMBER_TO_PPOS_INPUT.get(blockNumber) !=null;
    }

    public static PPosInvokeContractInput getPPosInvokeContractInput(String contractInvokeTxHash){
        return CONTRACT_INVOKE_TX_HASH_TO_PPOS_INPUT.get(contractInvokeTxHash);
    }
}
