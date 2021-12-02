package com.platon.browser.analyzer.epoch;

import com.platon.browser.AgentTestBase;
import com.platon.browser.bean.CollectionEvent;
import com.platon.browser.bean.EpochMessage;
import com.platon.browser.cache.NetworkStatCache;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.custommapper.EpochBusinessMapper;
import com.platon.browser.dao.custommapper.SlashBusinessMapper;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dao.mapper.StakingMapper;
import com.platon.browser.dao.param.ppos.Report;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.service.proposal.ProposalParameterService;
import com.platon.browser.service.statistic.StatisticService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class OnConsensusAnalyzerTest extends AgentTestBase {

    @Mock
    private BlockChainConfig chainConfig;

    @Mock
    private EpochBusinessMapper epochBusinessMapper;

    @Mock
    private SlashBusinessMapper slashBusinessMapper;

    @Mock
    private StakingMapper stakingMapper;

    @Mock
    private NetworkStatCache networkStatCache;

    @Mock
    private ProposalParameterService proposalParameterService;

    @Mock
    private StatisticService statisticService;

    @InjectMocks
    @Spy
    private OnConsensusAnalyzer target;

    @Before
    public void setup() throws Exception {
        when(chainConfig.getConsensusPeriodBlockCount()).thenReturn(blockChainConfig.getConsensusPeriodBlockCount());
        List<String> nodeIdList = new ArrayList<>();
        nodeList.forEach(n -> nodeIdList.add(n.getNodeId()));
        List<Staking> stakings = new ArrayList();
        stakingList.forEach(s -> stakings.add(s));
        when(slashBusinessMapper.getException(nodeIdList)).thenReturn(stakings);

        Staking data = stakings.get(0);
        List<Report> reportList = new ArrayList();
        Report report = Report.builder().stakingBlockNum(BigInteger.TEN).slashRate(BigDecimal.valueOf(3.3)).slash2ReportRate(BigDecimal.TEN).settingEpoch(33).txHash("3r33434343").build();
        BeanUtils.copyProperties(data, report);
        reportList.add(report);
        when(stakingMapper.selectByPrimaryKey(any())).thenReturn(data);
        when(chainConfig.getDuplicateSignSlashRate()).thenReturn(BigDecimal.TEN);
    }

    @Test
    public void convert() {
        Block block = blockList.get(0);
        EpochMessage epochMessage = EpochMessage.newInstance();
        epochMessage.setCurValidatorList(validatorList);
        CollectionEvent collectionEvent = new CollectionEvent();
        collectionEvent.setBlock(block);
        collectionEvent.setEpochMessage(epochMessage);
        target.analyze(collectionEvent, block);
    }

}
