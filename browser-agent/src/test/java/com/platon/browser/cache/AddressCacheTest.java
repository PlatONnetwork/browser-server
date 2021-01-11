package com.platon.browser.cache;

import com.platon.browser.AgentTestBase;
import com.platon.browser.bean.ComplementInfo;
import com.platon.browser.dao.param.ppos.DelegateExit;
import com.platon.browser.dao.param.ppos.DelegateRewardClaim;
import com.platon.browser.dao.entity.Erc20TokenAddressRel;
import com.platon.browser.service.erc20.ERCData;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.enums.ContractTypeEnum;
import com.platon.browser.enums.InnerContractAddrEnum;
import com.platon.browser.param.claim.Reward;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AddressCacheTest extends AgentTestBase {

    @Spy
    private AddressCache addressCache;

    @Test
    public void test() {
        this.addressCache.initEvmContractAddressCache(new ArrayList<>(this.addressList));
        this.addressCache.initWasmContractAddressCache(new ArrayList<>(this.addressList));
        this.addressCache.initEvmErc20ContractAddressCache(new ArrayList<>(this.addressList));
        this.addressCache.initOnFirstStart();
        this.addressCache.update(this.transactionList.get(0));
        this.addressCache.getAll();
        this.addressCache.cleanAll();
        this.addressCache.getEvmErc20ContractAddressCache();
        this.addressCache.getWasmContractAddressCache();
        this.addressCache.isWasmContractAddress("");
        this.addressCache.getEvmContractAddressCache();
        this.addressCache.isEvmErc20ContractAddress("");
        this.addressCache.getTypeData("");
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
        this.addressCache.getTypeData("789");


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

        this.addressCache.getAllErc20Token();
        this.addressCache.getErc20Token("");
        this.addressCache.cleanErc20TokenCache();
        this.addressCache.cleanErc20TokenAddressRelMap();
        this.addressCache.getErc20TokenAddressRelMap();
        this.addressCache.createDefaultErc20("123");
        ERCData ercData = new ERCData();
        ercData.setTotalSupply(BigInteger.TEN);
        this.addressCache.createFirstErc20("", "", "", new Date(), "", ercData);
        this.addressCache.updateErcTx("123");
        this.addressCache.updateErcHolder("123");
        this.addressCache.putErc20TokenAddressRelMap("", new Erc20TokenAddressRel());
        this.addressCache.updateTokenAddress("");

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
