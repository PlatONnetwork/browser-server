package com.platon.browser.task;

import com.platon.browser.AgentTestData;
import com.platon.browser.bean.ProposalParticipantStat;
import com.platon.browser.bean.CollectionNetworkStat;
import com.platon.browser.cache.NetworkStatCache;
import com.platon.browser.enums.AppStatus;
import com.platon.browser.service.proposal.ProposalService;
import com.platon.browser.utils.AppStatusUtil;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.dao.custommapper.CustomProposalMapper;
import com.platon.browser.dao.mapper.ProposalMapper;
import com.platon.browser.exception.BlankResponseException;
import com.platon.browser.exception.ContractInvokeException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

/**
 * @description:
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-13 17:13:04
 **/
@RunWith(MockitoJUnitRunner.Silent.class)
public class ProposalInfoTaskTest extends AgentTestData {
    @Mock
    private NetworkStatCache networkStatCache;
    @Mock
    private ProposalMapper proposalMapper;
    @Mock
    private CustomProposalMapper customProposalMapper;
    @Mock
    private ProposalService proposalService;


    @Before
    public void setup() throws IOException {
//        ReflectionTestUtils.setField(target, "platOnClient", platOnClient);
//        Web3jWrapper ww = mock(Web3jWrapper.class);
//        when(platOnClient.getWeb3jWrapper()).thenReturn(ww);
//        Web3j web3j = mock(Web3j.class);
//        when(ww.getWeb3j()).thenReturn(web3j);
//        Request rq = mock(Request.class);
//        when(web3j.platonBlockNumber()).thenReturn(rq);
//        PlatonBlockNumber rs = mock(PlatonBlockNumber.class);
//        when(rq.send()).thenReturn(rs);
//        when(rs.getBlockNumber()).thenReturn(BigInteger.TEN);
    }

    @Test
    public void test() throws ContractInvokeException, BlankResponseException {
        AppStatusUtil.setStatus(AppStatus.RUNNING);
        when(proposalMapper.selectByExample(any())).thenReturn(new ArrayList<>(proposalList));
        NetworkStat net = CollectionNetworkStat.newInstance();
        net.setCurNumber(100000L);
        when(networkStatCache.getNetworkStat()).thenReturn(net);
    }
}
