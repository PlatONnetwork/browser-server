package com.platon.browser;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.platon.browser.dao.custommapper.CustomNodeMapper;
import com.platon.browser.service.CommonService;
import com.platon.browser.utils.NetworkParams;
import org.junit.Before;
import org.mockito.Mock;

import com.platon.browser.dao.entity.Address;
import com.platon.browser.dao.mapper.AddressMapper;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.service.elasticsearch.bean.ESResult;
import com.platon.browser.service.elasticsearch.EsBlockRepository;
import com.platon.browser.service.elasticsearch.EsTransactionRepository;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.service.StatisticCacheService;
import com.platon.browser.utils.I18nUtil;

public class ApiTestMockBase extends ApiTestData {

    @Mock
    protected EsBlockRepository ESBlockRepository;

    @Mock
    protected EsTransactionRepository ESTransactionRepository;

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

    @Mock
    protected CommonService commonService;

    @Mock
    protected NetworkParams networkParams;

    @Before
    public void setUp() throws Exception {
        initNetwork();

        initBlock();

        initNode();

        initTransaction();

        initAddress();

        when(customNodeMapper.selectCountByActive()).thenReturn(10);
        when(i18n.i(any(), any(), any())).thenReturn("test");
        when(commonService.getNodeName(any(), any())).thenReturn("test-name");
        when(networkParams.getUnit()).thenReturn("ATP");
        when(networkParams.getHrp()).thenReturn("atp");

    }

    private void initAddress() {
        Address address = new Address();
        address.setType(1);
        when(addressMapper.selectByPrimaryKey(any())).thenReturn(address);
    }

    private void initTransaction() throws IOException {
        ESResult<Object> transEs = new ESResult<>();
        List<Object> transactionListTemp = new ArrayList<>();
        for (Transaction t : transactionList) {
            transactionListTemp.add(t);
        }
        transEs.setRsData(transactionListTemp);
        transEs.setTotal(2l);
        when(ESTransactionRepository.search(any(), any(), anyInt(), anyInt())).thenReturn(transEs);

        when(ESTransactionRepository.get(any(), any())).thenReturn(transactionList.get(0));
    }

    private void initNode() {
        when(nodeMapper.selectByExample(any())).thenReturn(nodeList);
        when(nodeMapper.selectByPrimaryKey(any())).thenReturn(nodeList.get(0));
    }

    private void initBlock() throws IOException {
        Block block = new Block();
        block.setReward("10");
        block.setTime(new Date());
        block.setNum(10l);
        when(ESBlockRepository.get(any(), any())).thenReturn(block);

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
        when(ESBlockRepository.search(any(), any(), anyInt(), anyInt())).thenReturn(blockEs);

        List<Block> bl = new ArrayList<>();
        bl.add(block);
        bl.add(block1);
        when(statisticCacheService.getBlockCache(anyInt(), any())).thenReturn(bl);
        when(statisticCacheService.getBlockCacheByStartEnd(any(), any())).thenReturn(bl);


    }

    private void initNetwork() {
        when(statisticCacheService.getNetworkStatCache()).thenReturn(networkStatList.get(0));
        when(commonService.getIssueValue()).thenReturn(BigDecimal.valueOf(1));
        when(commonService.getCirculationValue()).thenReturn(BigDecimal.valueOf(1));
    }

}
