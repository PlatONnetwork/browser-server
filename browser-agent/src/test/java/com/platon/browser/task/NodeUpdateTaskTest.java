package com.platon.browser.task;

import com.github.pagehelper.Page;
import com.platon.browser.AgentTestBase;
import com.platon.browser.common.enums.AppStatus;
import com.platon.browser.common.utils.AppStatusUtil;
import com.platon.browser.complement.dao.mapper.StakeBusinessMapper;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.exception.BlockNumberException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.BeanUtils;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;

/**
 * @description:
 * @author: chendongming@juzix.net
 * @create: 2019-11-13 17:13:04
 **/
@RunWith(MockitoJUnitRunner.Silent.class)
public class NodeUpdateTaskTest extends AgentTestBase {
    @Mock
    private BlockChainConfig chainConfig;
    @Mock
    private NodeMapper nodeMapper;
    @Mock
    private StakeBusinessMapper stakeBusinessMapper;
    @Spy
    private NodeUpdateTask target;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(target, "chainConfig", chainConfig);
        ReflectionTestUtils.setField(target, "nodeMapper", nodeMapper);
        ReflectionTestUtils.setField(target, "stakeBusinessMapper", stakeBusinessMapper);

        when(chainConfig.getKeyBase()).thenReturn("https://keybase.io/");
        when(chainConfig.getKeyBaseApi()).thenReturn("_/api/1.0/user/autocomplete.json?q=");
        Page<Node> page = new Page<>();
        page.addAll(nodeList);
        nodeList.forEach(n->n.setExternalId("5FD68B690010632B"));
        when(nodeMapper.selectByExample(any())).thenReturn(page);
    }

    @Test
    public void test() {
        AppStatusUtil.setStatus(AppStatus.RUNNING);
        target.cron();
        verify(target, times(1)).cron();

        doThrow(new RuntimeException("")).when(chainConfig).getKeyBase();
        target.cron();
    }
}
