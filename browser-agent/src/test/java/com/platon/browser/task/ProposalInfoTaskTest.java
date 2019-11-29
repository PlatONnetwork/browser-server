package com.platon.browser.task;

import com.platon.browser.AgentTestBase;
import com.platon.browser.common.collection.dto.CollectionNetworkStat;
import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.common.enums.AppStatus;
import com.platon.browser.common.service.proposal.ProposalService;
import com.platon.browser.common.utils.AppStatusUtil;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.dao.mapper.CustomProposalMapper;
import com.platon.browser.dao.mapper.ProposalMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

/**
 * @description:
 * @author: chendongming@juzix.net
 * @create: 2019-11-13 17:13:04
 **/
@RunWith(MockitoJUnitRunner.Silent.class)
public class ProposalInfoTaskTest extends AgentTestBase {
    @Mock
    private NetworkStatCache networkStatCache;
    @Mock
    private ProposalMapper proposalMapper;
    @Mock
    private CustomProposalMapper customProposalMapper;
    @Mock
    private ProposalService proposalService;
    @Spy
    private ProposalInfoTask target;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(target, "networkStatCache", networkStatCache);
        ReflectionTestUtils.setField(target, "proposalMapper", proposalMapper);
        ReflectionTestUtils.setField(target, "customProposalMapper", customProposalMapper);
        ReflectionTestUtils.setField(target, "proposalService", proposalService);
    }

    @Test
    public void test() {
        AppStatusUtil.setStatus(AppStatus.RUNNING);
        when(proposalMapper.selectByExample(any())).thenReturn(new ArrayList<>(proposalList));
        NetworkStat networkStat = CollectionNetworkStat.newInstance();
        networkStat.setCurNumber(100000L);
        when(networkStatCache.getNetworkStat()).thenReturn(networkStat);
        target.cron();
        verify(target, times(1)).cron();

        doThrow(new RuntimeException("")).when(networkStatCache).getNetworkStat();
        target.cron();
    }
}
