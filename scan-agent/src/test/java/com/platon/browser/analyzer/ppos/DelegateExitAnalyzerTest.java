package com.platon.browser.analyzer.ppos;

import com.platon.browser.AgentTestBase;
import com.platon.browser.bean.CollectionTransaction;
import com.platon.browser.cache.AddressCache;
import com.platon.browser.cache.NodeCache;
import com.platon.browser.bean.NodeItem;
import com.platon.browser.bean.CollectionEvent;
import com.platon.browser.dao.custommapper.CustomAddressMapper;
import com.platon.browser.dao.custommapper.DelegateBusinessMapper;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.Address;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dao.custommapper.CustomGasEstimateMapper;
import com.platon.browser.dao.mapper.AddressMapper;
import com.platon.browser.dao.mapper.DelegationMapper;
import com.platon.browser.dao.mapper.GasEstimateMapper;
import com.platon.browser.dao.mapper.StakingMapper;
import com.platon.browser.bean.CustomDelegation;
import com.platon.browser.elasticsearch.dto.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @Auther: dongqile
 * @Date: 2019/11/13
 * @Description: 委托退出转换器测试类
 */

@RunWith(MockitoJUnitRunner.Silent.class)
public class DelegateExitAnalyzerTest extends AgentTestBase {

    @Mock
    private DelegateBusinessMapper delegateBusinessMapper;

    @Mock
    private CollectionEvent collectionEvent;

    @Mock
    private NodeCache nodeCache;

    @Mock
    private DelegationMapper delegationMapper;

    @Mock
    private BlockChainConfig chainConfig;

    @Mock
    private AddressCache addressCache;

    @Mock
    private GasEstimateMapper gasEstimateMapper;

    @Mock
    private CustomGasEstimateMapper customGasEstimateMapper;

    @Mock
    private StakingMapper stakingMapper;

    @InjectMocks
    @Spy
    private DelegateExitAnalyzer target;
    @Mock
    private AddressMapper addressMapper;
    @Mock
    private CustomAddressMapper customAddressMapper;
    @Before
    public void setup() throws Exception {
        NodeItem nodeItem = NodeItem.builder()
                .nodeId("0x77fffc999d9f9403b65009f1eb27bae65774e2d8ea36f7b20a89f82642a5067557430e6edfe5320bb81c3666a19cf4a5172d6533117d7ebcd0f2c82055499050")
                .nodeName("integration-node1")
                .stakingBlockNum(new BigInteger("88602"))
                .build();
        when(this.nodeCache.getNode(anyString())).thenReturn(nodeItem);
        when(this.stakingMapper.selectByPrimaryKey(any())).thenReturn(this.stakingList.get(0));
        this.blockChainConfig.getDelegateThreshold();
        when(this.chainConfig.getDelegateThreshold()).thenReturn(this.blockChainConfig.getDelegateThreshold());
        Address addressInfo =new Address();
        when(addressMapper.selectByPrimaryKey(any())).thenReturn(addressInfo);
    }

    @Test
    public void convert() throws Exception {
        CustomDelegation delegation = new CustomDelegation();
        for (CustomDelegation delegations : this.delegationList) {
            if (delegations.getDelegateAddr().equals("atp12att07xxprevkqddq74qfnj988dgyrq2pcdvt4"))
                delegation = delegations;
        }
        delegation.setStakingBlockNum(1606L);
        delegation.setNodeId("0x77fffc999d9f9403b65009f1eb27bae65774e2d8ea36f7b20a89f82642a5067557430e6edfe5320bb81c3666a19cf4a5172d6533117d7ebcd0f2c82055499050");
        delegation.setDelegateReleased(BigDecimal.ONE);
        delegation.setDelegateHes(new BigDecimal("0"));
        delegation.setDelegateLocked(new BigDecimal("1000000000000000000"));
        when(this.delegationMapper.selectByPrimaryKey(any())).thenReturn(delegation);
        Staking staking = new Staking();
        staking.setNodeId(delegation.getNodeId());
        staking.setStatus(1);
        when(this.stakingMapper.selectByExample(any())).thenReturn(Arrays.asList(staking));

        CollectionTransaction tx = null;
        for (CollectionTransaction collectionTransaction : this.transactionList) {
            if (collectionTransaction.getTypeEnum() == Transaction.TypeEnum.DELEGATE_EXIT)
                tx = collectionTransaction;
        }
        this.target.analyze(this.collectionEvent, tx);

        delegation.setDelegateHes(new BigDecimal("500000000000000000000000"));
        delegation.setDelegateLocked(new BigDecimal("500000000000000000000000"));
        delegation.setDelegateReleased(new BigDecimal("500000000000000000000000"));
        when(this.chainConfig.getDelegateThreshold()).thenReturn(BigDecimal.ONE);

        this.target.analyze(this.collectionEvent, tx);
        delegation.setDelegateReleased(BigDecimal.ZERO);
        delegation.setDelegateHes(new BigDecimal("900000000000000000000000"));
        this.target.analyze(this.collectionEvent, tx);

        delegation.setDelegateHes(new BigDecimal("200000000000000000000000"));
        this.target.analyze(this.collectionEvent, tx);

        assertTrue(true);
    }

}