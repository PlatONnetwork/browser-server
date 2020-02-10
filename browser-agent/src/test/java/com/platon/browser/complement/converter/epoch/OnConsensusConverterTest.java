package com.platon.browser.complement.converter.epoch;

import com.platon.browser.AgentTestBase;
import com.platon.browser.common.collection.dto.EpochMessage;
import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.common.complement.cache.ReportMultiSignParamCache;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.dao.mapper.EpochBusinessMapper;
import com.platon.browser.complement.dao.mapper.SlashBusinessMapper;
import com.platon.browser.complement.dao.param.slash.Report;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dao.mapper.StakingMapper;
import com.platon.browser.elasticsearch.dto.Block;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class OnConsensusConverterTest extends AgentTestBase {
	
    @Mock
    private BlockChainConfig chainConfig;
    @Mock
    private EpochBusinessMapper epochBusinessMapper;
    @Mock
    private ReportMultiSignParamCache reportMultiSignParamCache;
    @Mock
    private SlashBusinessMapper slashBusinessMapper;
    @Mock
    private StakingMapper stakingMapper;
    @Mock
    private NetworkStatCache networkStatCache;
	
    @Spy
    private OnConsensusConverter target;

    @Before
    public void setup()throws Exception{
        ReflectionTestUtils.setField(target,"chainConfig",chainConfig);
        ReflectionTestUtils.setField(target,"epochBusinessMapper",epochBusinessMapper);
        ReflectionTestUtils.setField(target,"reportMultiSignParamCache",reportMultiSignParamCache);
        ReflectionTestUtils.setField(target,"slashBusinessMapper",slashBusinessMapper);
        ReflectionTestUtils.setField(target,"stakingMapper",stakingMapper);
        ReflectionTestUtils.setField(target,"networkStatCache",networkStatCache);
        when(chainConfig.getConsensusPeriodBlockCount()).thenReturn(blockChainConfig.getConsensusPeriodBlockCount());
        List<String> nodeIdList = new ArrayList<>();
        nodeList.forEach(n->nodeIdList.add(n.getNodeId()));
        when(reportMultiSignParamCache.getNodeIdList()).thenReturn(nodeIdList);
        List<Staking> stakings = new ArrayList();
        stakingList.forEach(s->stakings.add(s));
        when(slashBusinessMapper.getException(nodeIdList)).thenReturn(stakings);

        Staking data = stakings.get(0);
        List<Report> reportList = new ArrayList();
        Report report = Report.builder()
                .stakingBlockNum(BigInteger.TEN)
                .slashRate(BigDecimal.valueOf(3.3))
                .slash2ReportRate(BigDecimal.TEN)
                .settingEpoch(33)
                .txHash("3r33434343")
                .build();
        BeanUtils.copyProperties(data,report);
        reportList.add(report);
        when(reportMultiSignParamCache.getReportList(any())).thenReturn(reportList);
        when(stakingMapper.selectByPrimaryKey(any())).thenReturn(data);
        when(chainConfig.getDuplicateSignSlashRate()).thenReturn(BigDecimal.TEN);
    }

    @Test
    public void convert(){
        Block block = blockList.get(0);
        EpochMessage epochMessage = EpochMessage.newInstance();
        epochMessage.setCurValidatorList(validatorList);
        CollectionEvent collectionEvent = new CollectionEvent();
        collectionEvent.setBlock(block);
        collectionEvent.setEpochMessage(epochMessage);
        target.convert(collectionEvent,block);
    }
}
