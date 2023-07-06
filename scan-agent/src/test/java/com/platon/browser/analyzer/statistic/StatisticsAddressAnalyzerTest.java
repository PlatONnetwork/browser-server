package com.platon.browser.analyzer.statistic;

import com.platon.browser.AgentTestBase;
import com.platon.browser.analyzer.TransactionAnalyzer;
import com.platon.browser.bean.CollectionEvent;
import com.platon.browser.bean.EpochMessage;
import com.platon.browser.cache.NewAddressCache;
import com.platon.browser.dao.custommapper.CustomAddressMapper;
import com.platon.browser.dao.custommapper.StatisticBusinessMapper;
import com.platon.browser.dao.mapper.AddressMapper;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.enums.ContractTypeEnum;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigInteger;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.Silent.class)
public class StatisticsAddressAnalyzerTest extends AgentTestBase {

    @Mock
    private NewAddressCache newAddressCache;
    @Mock
    private StatisticBusinessMapper statisticBusinessMapper;
    @Mock
    private AddressMapper addressMapper;

    @InjectMocks
    @Spy
    private StatisticsAddressAnalyzer target;
    @Mock
    private CustomAddressMapper customAddressMapper;
    @Before
    public void setup() throws Exception {
        when(this.newAddressCache.listNewAddressInBlockCtx()).thenReturn(new ArrayList<>(this.addressList));
        when(this.newAddressCache.listRewardClaimAddressInBlockCtx()).thenReturn(new ArrayList<>(this.addressList));
        when(this.newAddressCache.listSuicidedAddressInBlockCtx()).thenReturn(new ArrayList<>(this.addressList));
        when(this.addressMapper.selectByExampleWithBLOBs(any())).thenReturn(new ArrayList<>(this.addressList));
        when(customAddressMapper.batchUpdateAddressInfo(any())).thenReturn(1);
    }

    @Test
    public void convert() {
        Block block = this.blockList.get(0);
        EpochMessage epochMessage = EpochMessage.newInstance();
        epochMessage.setIssueEpochRound(BigInteger.TEN);
        epochMessage.setSettleEpochRound(BigInteger.TEN);
        epochMessage.setCurrentBlockNumber(BigInteger.TEN);
        epochMessage.setConsensusEpochRound(BigInteger.TEN);
        CollectionEvent collectionEvent = new CollectionEvent();
        collectionEvent.setBlock(block);
        collectionEvent.setEpochMessage(epochMessage);
        collectionEvent.getBlock().setDtoTransactions(new ArrayList<>(this.transactionList));
        TransactionAnalyzer.setGeneralContractAddressCache(this.addressList.get(0).getAddress(), ContractTypeEnum.EVM);
        TransactionAnalyzer.setGeneralContractAddressCache(this.addressList.get(1).getAddress(), ContractTypeEnum.WASM);
        TransactionAnalyzer.setGeneralContractAddressCache(this.addressList.get(2).getAddress(), ContractTypeEnum.ERC20_EVM);
       // this.transactionList.get(0).setBin("0x");
        this.target.analyze(collectionEvent, block, epochMessage);
    }

}
