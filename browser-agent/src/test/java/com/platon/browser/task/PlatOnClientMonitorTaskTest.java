package com.platon.browser.task;

import com.github.pagehelper.Page;
import com.platon.browser.AgentTestBase;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.common.enums.AppStatus;
import com.platon.browser.common.utils.AppStatusUtil;
import com.platon.browser.complement.dao.mapper.StakeBusinessMapper;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.mapper.NodeMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @description:
 * @author: chendongming@juzix.net
 * @create: 2019-11-13 17:13:04
 **/
@RunWith(MockitoJUnitRunner.Silent.class)
public class PlatOnClientMonitorTaskTest extends AgentTestBase {
    @Mock
    private PlatOnClient platOnClient;
    @Spy
    private PlatOnClientMonitorTask target;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(target, "platOnClient", platOnClient);
    }

    @Test
    public void test() {
        AppStatusUtil.setStatus(AppStatus.RUNNING);
        target.cron();
        verify(target, times(1)).cron();

        doThrow(new RuntimeException("")).when(platOnClient).updateCurrentWeb3jWrapper();
        target.cron();
    }
}
