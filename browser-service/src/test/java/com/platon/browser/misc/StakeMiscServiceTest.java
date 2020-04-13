package com.platon.browser.misc;

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
import com.platon.browser.dao.mapper.ProposalMapper;
import com.platon.browser.service.govern.ParameterService;
import com.platon.browser.service.misc.StakeMiscService;

@RunWith(MockitoJUnitRunner.Silent.class)
public class StakeMiscServiceTest {
    @Mock
    private ParameterService parameterService;
    @Mock
    private BlockChainConfig chainConfig;
    @Mock
    private ProposalMapper proposalMapper;

    @Spy
    private StakeMiscService target;

    @Before
    public void setup() throws Exception {
        ReflectionTestUtils.setField(target, "proposalMapper", proposalMapper);
        ReflectionTestUtils.setField(target, "parameterService", parameterService);
        ReflectionTestUtils.setField(target, "chainConfig", chainConfig);
    }

    @Test
    public void test() throws Exception {
    	
    	
//    	assertEquals(target.getUnStakeFreeDuration(), new BusinessException("参数表参数缺失："+ModifiableGovernParamEnum.UN_STAKE_FREEZE_DURATION.getName()));
    	when(parameterService.getValueInBlockChainConfig(any())).thenReturn("1");
    	target.getUnStakeFreeDuration();
    	
    	when(chainConfig.getSettlePeriodBlockCount()).thenReturn(BigInteger.ONE);
    	List<Proposal> proposals = new ArrayList<Proposal>();
    	Proposal proposal = new Proposal();
    	proposal.setEndVotingBlock(1000000000l);
    	when(proposalMapper.selectByExample(any())).thenReturn(proposals);
    	
    	target.getUnStakeEndBlock("0x1", BigInteger.TEN, true);
    	
    }
}
