package com.platon.browser.cache;

import com.platon.browser.AgentTestBase;
import com.platon.browser.bean.ComplementInfo;
import com.platon.browser.dao.param.ppos.DelegateExit;
import com.platon.browser.dao.param.ppos.DelegateRewardClaim;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.enums.ContractTypeEnum;
import com.platon.browser.enums.InnerContractAddrEnum;
import com.platon.browser.param.claim.Reward;
import com.platon.browser.v0152.analyzer.ErcCache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

        ComplementInfo ci = new ComplementInfo();
        ci.setContractType(ContractTypeEnum.EVM.getCode());
        this.addressCache.updateFirst("123", ci);
        this.addressCache.getTypeData("123");
        ci.setContractType(ContractTypeEnum.WASM.getCode());
        this.addressCache.updateFirst("456", ci);
        this.addressCache.getTypeData("456");
        ci.setContractType(ContractTypeEnum.ERC20_EVM.getCode());
        this.addressCache.updateFirst("789", ci);
        //this.addressCache.getTypeData("789");


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

        List<Reward> claims = new ArrayList<>();
        Reward reward = Reward.builder().reward(BigDecimal.TEN).build();
        claims.add(reward);
        DelegateRewardClaim drc = DelegateRewardClaim.builder().address("").rewardList(claims).build();
        this.addressCache.update(drc);

        DelegateExit delegateExit = DelegateExit.builder().txFrom("789").delegateReward(BigDecimal.TEN).build();
        this.addressCache.update(delegateExit);

        delegateExit = DelegateExit.builder().txFrom("74829301").delegateReward(BigDecimal.TEN).build();
        this.addressCache.update(delegateExit);

        assertTrue(true);
    }

}
