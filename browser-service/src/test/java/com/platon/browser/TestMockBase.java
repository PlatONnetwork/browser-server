package com.platon.browser;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.mockito.Mock;

import com.platon.browser.dao.entity.Address;
import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.mapper.AddressMapper;
import com.platon.browser.dao.mapper.CustomNodeMapper;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.dto.elasticsearch.ESResult;
import com.platon.browser.elasticsearch.BlockESRepository;
import com.platon.browser.elasticsearch.TransactionESRepository;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.now.service.cache.StatisticCacheService;
import com.platon.browser.util.I18nUtil;

public class TestMockBase extends TestData{

	@Mock
	protected BlockESRepository blockESRepository;
	
	@Mock
	protected TransactionESRepository transactionESRepository;
	
	@Mock
	protected NodeMapper nodeMapper;
	
	@Mock
	protected AddressMapper addressMapper;
	
	@Mock
	protected StatisticCacheService statisticCacheService;
	
	@Mock
	protected CustomNodeMapper customNodeMapper;
	
	@Mock
	protected I18nUtil i18n;
	
	@Before
	public void setUp() throws Exception {
		when(statisticCacheService.getNetworkStatCache()).thenReturn(networkStatList.get(0));
		Block block = new Block();
		block.setReward("10");
		block.setTime(new Date());
		block.setNum(10l);
		when(blockESRepository.get(any(),any())).thenReturn(block);
		when(nodeMapper.selectByExample(any())).thenReturn(nodeList);
		when(nodeMapper.selectByPrimaryKey(any())).thenReturn(nodeList.get(0));
		
		Address address = new Address();
		address.setType(1);
		when(addressMapper.selectByPrimaryKey(any())).thenReturn(address);
		
		Transaction transaction = new Transaction();
		when(transactionESRepository.get(any(),any())).thenReturn(transaction);
		
		ESResult<Object> blockEs = new ESResult<>();
        List<Object> blockList = new ArrayList<>();
        Block block1 = new Block();
		block1.setNum(110l);
		block1.setReward("10");
		block1.setTime(new Date());
        blockList.add(block);
        blockList.add(block1);
        blockEs.setRsData(blockList);
        blockEs.setTotal(2l);
        when(blockESRepository.search(any(), any(), anyInt(),anyInt())).thenReturn(blockEs);
        
        List<Block> bl = new ArrayList<>();
        bl.add(block);
        bl.add(block1);
        when(statisticCacheService.getBlockCache(anyInt(),any())).thenReturn(bl);
        
        when(statisticCacheService.getBlockCacheByStartEnd(any(),any())).thenReturn(bl);
        
        when(customNodeMapper.selectCountByActive()).thenReturn(10);
        
        ESResult<Object> transEs = new ESResult<>();
        List<Object> transactionListTemp = new ArrayList<>();
        for (Transaction t : transactionList) {
        	transactionListTemp.add(t);
		}
        transEs.setRsData(transactionListTemp);
        transEs.setTotal(2l);
        when(transactionESRepository.search(any(), any(), anyInt(),anyInt())).thenReturn(transEs);
        
        when(i18n.i(any(), any(), any())).thenReturn("test");
	}
}
