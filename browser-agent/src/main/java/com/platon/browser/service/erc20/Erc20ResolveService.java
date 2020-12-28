package com.platon.browser.service.erc20;

import com.alaya.protocol.core.methods.response.TransactionReceipt;
import com.platon.browser.cache.AddressCache;
import com.platon.browser.elasticsearch.dto.Transaction;

import java.util.List;

public interface Erc20ResolveService extends Erc20Service {
    /**
     * 回填对应的erc20合约参数信息
     * 
     * @param transactions
     * @param addressCache
     */
    void initContractAddressCache(List<Transaction> transactions, AddressCache addressCache);

    /**
     *  从配置中特定的Event判定回执中是否存在合约地址
     *
     * @param transactionReceipt
     * @return
     */
    List<String> getContractAddressFromEvents(TransactionReceipt transactionReceipt);

}
