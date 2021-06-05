package com.platon.browser.service.misc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.Proposal;
import com.platon.browser.dao.custommapper.CustomProposalMapper;
import com.platon.browser.service.govern.ParameterService;
import com.platon.browser.service.ppos.StakeEpochService;

@RunWith(MockitoJUnitRunner.Silent.class)
public class StakeEpochServiceTest {
    @Mock
    private ParameterService parameterService;
    @Mock
    private BlockChainConfig chainConfig;
    @Mock
    private CustomProposalMapper customProposalMapper;

    @Spy
    private StakeEpochService target;

    @Before
    public void setup() throws Exception {
        ReflectionTestUtils.setField(target, "customProposalMapper", customProposalMapper);
        ReflectionTestUtils.setField(target, "parameterService", parameterService);
        ReflectionTestUtils.setField(target, "chainConfig", chainConfig);
    }

    @Test
    public void test() throws Exception {
    	try {
    		target.getUnStakeFreeDuration();
		} catch (Exception e) {
		}
    	when(parameterService.getValueInBlockChainConfig(any())).thenReturn("1");
    	target.getUnStakeFreeDuration();
    	when(chainConfig.getSettlePeriodBlockCount()).thenReturn(BigInteger.ONE);
    	List<Proposal> proposals = new ArrayList<Proposal>();
    	Proposal proposal = new Proposal();
    	proposal.setEndVotingBlock(1000000000l);
    	proposals.add(proposal);
    	when(customProposalMapper.selectVotingProposal(any())).thenReturn(proposals);
    	target.getUnStakeEndBlock("0x1", BigInteger.TEN, true);
    	
    }
}
