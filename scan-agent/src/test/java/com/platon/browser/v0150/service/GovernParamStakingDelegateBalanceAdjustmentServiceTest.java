package com.platon.browser.v0150.service;

import com.platon.browser.AgentTestBase;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.dao.mapper.ConfigMapper;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.enums.ModifiableGovernParamEnum;
import com.platon.browser.v0150.V0150Config;
import com.platon.contracts.ppos.ProposalContract;
import com.platon.contracts.ppos.dto.CallResponse;
import com.platon.contracts.ppos.dto.resp.GovernParam;
import com.platon.contracts.ppos.dto.resp.ParamItem;
import com.platon.contracts.ppos.dto.resp.ParamValue;
import com.platon.protocol.core.RemoteCall;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigInteger;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @description: MySQL/ES/Redis启动一致性自检服务测试
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-13 11:41:00
 **/
@RunWith(MockitoJUnitRunner.Silent.class)
public class GovernParamStakingDelegateBalanceAdjustmentServiceTest extends AgentTestBase {
    @Mock
    private ConfigMapper configMapper;
    @Mock
    private PlatOnClient platOnClient;
    @Mock
    private V0150Config v0150Config;

    @Spy
    @InjectMocks
    private RestrictingMinimumReleaseParamService target;

    @Test
    public void test() throws Exception {
        Block block = blockList.get(0);

        ProposalContract proposalContract = mock(ProposalContract.class);
        when(platOnClient.getProposalContract()).thenReturn(proposalContract);

        RemoteCall remoteCall0 = mock(RemoteCall.class);
        CallResponse callResponse0 = mock(CallResponse.class);
        when(proposalContract.getActiveVersion()).thenReturn(remoteCall0);
        when(remoteCall0.send()).thenReturn(callResponse0);
        when(callResponse0.getData()).thenReturn(BigInteger.TEN);
        when(v0150Config.getRestrictingMinimumReleaseActiveVersion()).thenReturn(BigInteger.TEN);

        RemoteCall remoteCall1 = mock(RemoteCall.class);
        CallResponse callResponse1 = mock(CallResponse.class);
        when(proposalContract.getParamList(any())).thenReturn(remoteCall1);
        when(remoteCall1.send()).thenReturn(callResponse1);
        GovernParam gp = new GovernParam();
        ParamItem pi = new ParamItem();
        pi.setName(ModifiableGovernParamEnum.RESTRICTING_MINIMUM_RELEASE.getName());
        pi.setModule(ModifiableGovernParamEnum.RESTRICTING_MINIMUM_RELEASE.getModule());
        pi.setDesc("");
        gp.setParamItem(pi);
        ParamValue pv = new ParamValue();
        pv.setActiveBlock("98900");
        pv.setStaleValue("100000");
        pv.setValue("100000");
        gp.setParamValue(pv);
        when(callResponse1.getData()).thenReturn(Arrays.asList(gp));

        target.checkRestrictingMinimumReleaseParam(block);


    }
}
