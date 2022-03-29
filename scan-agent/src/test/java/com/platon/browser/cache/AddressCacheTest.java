package com.platon.browser.cache;

import com.platon.browser.AgentTestBase;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.enums.InnerContractAddrEnum;
import com.platon.browser.v0152.analyzer.ErcCache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AddressCacheTest extends AgentTestBase {

    @Spy
    private AddressCache addressCache;

    @Mock
    private ErcCache ercCache;

    @Test
    public void test() {
        this.addressCache.initEvmContractAddressCache(new ArrayList<>(this.addressList));
        this.addressCache.initWasmContractAddressCache(new ArrayList<>(this.addressList));
        this.addressCache.initOnFirstStart();
        this.addressCache.update(this.transactionList.get(0));
        this.addressCache.getAll();
        this.addressCache.cleanAll();
        this.addressCache.getWasmContractAddressCache();
        this.addressCache.isWasmContractAddress("");
        this.addressCache.getEvmContractAddressCache();
        //this.addressCache.getTypeData("");
        this.addressCache.getTypeData(InnerContractAddrEnum.RESTRICTING_PLAN_CONTRACT.getAddress());

        Transaction tx = this.transactionList.get(0);
        tx.setType(Transaction.TypeEnum.TRANSFER.getCode());
        this.addressCache.update(tx);

        tx.setType(Transaction.TypeEnum.DELEGATE_EXIT.getCode());
        this.addressCache.update(tx);

        tx.setType(Transaction.TypeEnum.VERSION_DECLARE.getCode());
        this.addressCache.update(tx);

        tx.setFrom("0x1");
        tx.setTo("0x1");
        this.addressCache.update(tx);

        tx.setFrom("lat1zqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqzsjx8h7");
        tx.setTo("lat1zqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqzsjx8h7");
        this.addressCache.update(tx);

        tx.setContractAddress("lat1zqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqzsjx8h8");
        tx.setType(Transaction.TypeEnum.EVM_CONTRACT_CREATE.getCode());
        this.addressCache.update(tx);

        tx.setType(Transaction.TypeEnum.WASM_CONTRACT_CREATE.getCode());
        this.addressCache.update(tx);

        tx.setType(Transaction.TypeEnum.ERC20_CONTRACT_CREATE.getCode());
        this.addressCache.update(tx);

        assertTrue(true);
    }

}
