package com.platon.browser.common.complement.cache;

import com.platon.browser.AgentTestBase;
import com.platon.browser.elasticsearch.dto.Transaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AddressCacheTest extends AgentTestBase {

    @Spy
    private AddressCache addressCache;
    @Test
    public void test(){
        addressCache.initEvmContractAddressCache(new ArrayList<>(addressList));
        addressCache.initWasmContractAddressCache(new ArrayList<>(addressList));
        addressCache.initOnFirstStart();
        addressCache.update(transactionList.get(0));
        addressCache.getAll();
        addressCache.cleanAll();

        Transaction tx = transactionList.get(0);
        tx.setType(Transaction.TypeEnum.TRANSFER.getCode());
        addressCache.update(tx);

        tx.setType(Transaction.TypeEnum.DELEGATE_EXIT.getCode());
        addressCache.update(tx);

        tx.setType(Transaction.TypeEnum.VERSION_DECLARE.getCode());
        addressCache.update(tx);

        tx.setFrom("0xddd");
        tx.setTo("0xddd");
        addressCache.update(tx);

        assertTrue(true);
    }
}
