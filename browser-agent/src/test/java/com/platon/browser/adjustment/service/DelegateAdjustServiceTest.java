package com.platon.browser.adjustment.service;

import com.platon.browser.AgentTestBase;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.dao.mapper.ConfigMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

/**
 *
 **/
@Slf4j
@RunWith(MockitoJUnitRunner.Silent.class)
public class DelegateAdjustServiceTest extends AgentTestBase {
    @Mock
    private ConfigMapper configMapper;
    @Mock
    private PlatOnClient platOnClient;

    @Spy
    @InjectMocks
    private DelegateAdjustService target;

    @Test
    public void test() throws Exception {

        target.adjustDelegateData(delegateAdjustParamList);

    }
}
