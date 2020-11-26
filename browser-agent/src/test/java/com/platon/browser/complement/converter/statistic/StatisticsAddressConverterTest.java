package com.platon.browser.complement.converter.statistic;

import com.platon.browser.AgentTestBase;
import com.platon.browser.common.collection.dto.CollectionTransaction;
import com.platon.browser.common.collection.dto.EpochMessage;
import com.platon.browser.common.complement.cache.AddressCache;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.dao.mapper.StatisticBusinessMapper;
import com.platon.browser.dao.entity.Erc20Token;
import com.platon.browser.dao.entity.Erc20TokenAddressRel;
import com.platon.browser.dao.mapper.*;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.enums.ContractTypeEnum;
import com.platon.browser.service.redis.RedisErc20TokenService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.Silent.class)
public class StatisticsAddressConverterTest extends AgentTestBase {


    @Mock
    private AddressCache addressCache;
    @Mock
    private StatisticBusinessMapper statisticBusinessMapper;
    @Mock
    private AddressMapper addressMapper;
    @Mock
    private Erc20TokenMapper erc20TokenMapper;
    @Mock
    private CustomErc20TokenMapper customErc20TokenMapper;
    @Mock
    private RedisTemplate redisTemplate;
    @Mock
    private CustomErc20TokenAddressRelMapper customErc20TokenAddressRelMapper;
    @Mock
    private Erc20TokenAddressRelMapper erc20TokenAddressRelMapper;
    @Mock
    private RedisErc20TokenService dbHelperCache;

    @Spy
    private StatisticsAddressConverter target;


    @Before
    public void setup() throws Exception {
        ReflectionTestUtils.setField(this.target, "dbHelperCache", this.dbHelperCache);
        ReflectionTestUtils.setField(this.target, "addressCache", this.addressCache);
        ReflectionTestUtils.setField(this.target, "statisticBusinessMapper", this.statisticBusinessMapper);
        ReflectionTestUtils.setField(this.target, "addressMapper", this.addressMapper);
        ReflectionTestUtils.setField(this.target, "erc20TokenMapper", this.erc20TokenMapper);
        ReflectionTestUtils.setField(this.target, "customErc20TokenMapper", this.customErc20TokenMapper);
        ReflectionTestUtils.setField(this.target, "redisTemplate", this.redisTemplate);
        ReflectionTestUtils.setField(this.target, "customErc20TokenAddressRelMapper", this.customErc20TokenAddressRelMapper);
        ReflectionTestUtils.setField(this.target, "erc20TokenAddressRelMapper", this.erc20TokenAddressRelMapper);
        when(this.addressCache.getAll()).thenReturn(new ArrayList<>(this.addressList));
        when(this.addressMapper.selectByExampleWithBLOBs(any())).thenReturn(new ArrayList<>(this.addressList));

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
        collectionEvent.setTransactions(new ArrayList<>(this.transactionList));
        CollectionTransaction.setGeneralContractAddressCache(this.addressList.get(0).getAddress(), ContractTypeEnum.EVM);
        CollectionTransaction.setGeneralContractAddressCache(this.addressList.get(1).getAddress(), ContractTypeEnum.WASM);
        CollectionTransaction.setGeneralContractAddressCache(this.addressList.get(2).getAddress(), ContractTypeEnum.ERC20_EVM);
        this.transactionList.get(0).setBin("0x");
        this.target.convert(collectionEvent, block, epochMessage);
    }

    @Test
    public void erc20TokenConvert() {
        Block block = this.blockList.get(0);
        EpochMessage epochMessage = EpochMessage.newInstance();
        epochMessage.setIssueEpochRound(BigInteger.TEN);
        epochMessage.setSettleEpochRound(BigInteger.TEN);
        epochMessage.setCurrentBlockNumber(BigInteger.TEN);
        epochMessage.setConsensusEpochRound(BigInteger.TEN);
        CollectionEvent collectionEvent = new CollectionEvent();
        collectionEvent.setBlock(block);
        collectionEvent.setEpochMessage(epochMessage);
        collectionEvent.setTransactions(new ArrayList<>(this.transactionList));
        this.target.erc20TokenConvert(collectionEvent, block, epochMessage);
        List<Erc20Token> erc20Tokens = new ArrayList<>();
        Erc20Token erc20Token = new Erc20Token();
        erc20Token.setAddress("123");
        erc20Token.setCreateTime(new Date());
        erc20Token.setSymbol("123");
        erc20Token.setTotalSupply(BigDecimal.TEN);
        erc20Token.setName("123");
        erc20Token.setDecimal(2);
        erc20Token.setCreator("");
        erc20Token.setTxHash("");
        erc20Token.setBlockTimestamp(new Date());
        erc20Token.setType("");
        erc20Token.setTxCount(1);
        erc20Token.setHolder(0);
        erc20Token.setStatus(0);
        erc20Tokens.add(erc20Token);
        when(this.erc20TokenMapper.selectByExample(any())).thenReturn(erc20Tokens);
        Erc20Token erc20Token2 = new Erc20Token();
        erc20Token2.setAddress("456");
        erc20Token2.setCreateTime(new Date());
        erc20Token2.setSymbol("123");
        erc20Token2.setTotalSupply(BigDecimal.TEN);
        erc20Token2.setName("123");
        erc20Token2.setDecimal(2);
        erc20Token2.setCreator("");
        erc20Token2.setTxHash("");
        erc20Token2.setBlockTimestamp(new Date());
        erc20Token2.setType("");
        erc20Token2.setTxCount(0);
        erc20Token2.setHolder(0);
        erc20Token2.setStatus(0);
        List<Erc20Token> erc20Tokens2 = new ArrayList<>();
        erc20Tokens2.add(erc20Token);
        erc20Tokens2.add(erc20Token2);
        when(this.addressCache.getAllErc20Token()).thenReturn(erc20Tokens2);
        this.target.erc20TokenConvert(collectionEvent, block, epochMessage);
    }

    @Test
    public void erc20AddressConvert() {
        Block block = this.blockList.get(0);
        EpochMessage epochMessage = EpochMessage.newInstance();
        epochMessage.setIssueEpochRound(BigInteger.TEN);
        epochMessage.setSettleEpochRound(BigInteger.TEN);
        epochMessage.setCurrentBlockNumber(BigInteger.TEN);
        epochMessage.setConsensusEpochRound(BigInteger.TEN);
        CollectionEvent collectionEvent = new CollectionEvent();
        collectionEvent.setBlock(block);
        collectionEvent.setEpochMessage(epochMessage);
        collectionEvent.setTransactions(new ArrayList<>(this.transactionList));
        this.target.erc20AddressConvert(collectionEvent, block, epochMessage);
        List<Erc20TokenAddressRel> erc20TokenAddressRels = new ArrayList<>();
        Erc20TokenAddressRel erc20TokenAddressRel = new Erc20TokenAddressRel();
        erc20TokenAddressRel.setTxCount(1);
        erc20TokenAddressRel.setContract("123");
        erc20TokenAddressRel.setAddress("123");
        erc20TokenAddressRels.add(erc20TokenAddressRel);
        when(this.customErc20TokenAddressRelMapper.selectExistData(any())).thenReturn(erc20TokenAddressRels);
        Erc20TokenAddressRel erc20TokenAddressRel2 = new Erc20TokenAddressRel();
        erc20TokenAddressRel2.setTxCount(1);
        erc20TokenAddressRel.setContract("456");
        erc20TokenAddressRel.setAddress("123");
        Map<String, Erc20TokenAddressRel> erc20TokenAddressRelMap = new HashMap<>();
        erc20TokenAddressRelMap.put("1", erc20TokenAddressRel);
        erc20TokenAddressRelMap.put("2", erc20TokenAddressRel2);
        when(this.addressCache.getErc20TokenAddressRelMap()).thenReturn(erc20TokenAddressRelMap);
        SetOperations setOperations = mock(SetOperations.class);
        when(this.redisTemplate.opsForSet()).thenReturn(setOperations);
        this.target.erc20AddressConvert(collectionEvent, block, epochMessage);
    }
}
