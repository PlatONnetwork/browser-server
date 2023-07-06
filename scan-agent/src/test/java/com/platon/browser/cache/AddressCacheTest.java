package com.platon.browser.cache;

import com.platon.browser.AgentTestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AddressCacheTest extends AgentTestBase {

    @Spy
    private NewAddressCache newAddressCache;


    @Test
    public void test() {
        /*this.newAddressCache.initEvmContractAddressCache(new ArrayList<>(this.addressList));
        this.newAddressCache.initWasmContractAddressCache(new ArrayList<>(this.addressList));
        this.newAddressCache.initOnFirstStart();
        this.newAddressCache.update(this.transactionList.get(0));
        this.newAddressCache.getAll();
        this.newAddressCache.cleanAll();
        this.newAddressCache.getWasmContractAddressCache();
        this.newAddressCache.isWasmContractAddress("");
        this.newAddressCache.getEvmContractAddressCache();
        //this.addressCache.getTypeData("");
        this.newAddressCache.getTypeData(InnerContractAddrEnum.RESTRICTING_PLAN_CONTRACT.getAddress());

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

        assertTrue(true);*/
    }

}
