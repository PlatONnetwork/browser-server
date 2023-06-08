package com.platon.browser.cache;

import com.platon.browser.bean.PPOSTx;
import com.platon.browser.utils.HexUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PPosInvokeContractInputCache {
    private PPosInvokeContractInputCache() {}
    private static final Map<Long, ConcurrentHashMap<String, List<PPOSTx>>> BLOCK_NUMBER_TO_PPOS_INPUT = new HashMap<>();
    private static final Map<String,List<PPOSTx>> CONTRACT_INVOKE_TX_HASH_TO_PPOS_INPUT = new HashMap<>();
    public static void update(Long blockNumber, ConcurrentHashMap<String, List<PPOSTx>> result){
        BLOCK_NUMBER_TO_PPOS_INPUT.clear();
        CONTRACT_INVOKE_TX_HASH_TO_PPOS_INPUT.clear();
        BLOCK_NUMBER_TO_PPOS_INPUT.put(blockNumber,result);
        result.forEach((key,value) ->{
            CONTRACT_INVOKE_TX_HASH_TO_PPOS_INPUT.put(HexUtil.prefix(key),value);
        });
    }

    public static boolean hasCache(Long blockNumber){
        return BLOCK_NUMBER_TO_PPOS_INPUT.get(blockNumber) !=null;
    }

    public static List<PPOSTx> getPPosInvokeContractInput(String contractInvokeTxHash){
        return CONTRACT_INVOKE_TX_HASH_TO_PPOS_INPUT.get(contractInvokeTxHash);
    }
}
