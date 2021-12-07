package com.platon.browser.task;

import com.github.pagehelper.Page;
import com.platon.browser.AgentTestBase;
import com.platon.browser.AgentTestData;
import com.platon.browser.enums.AppStatus;
import com.platon.browser.utils.AppStatusUtil;
import com.platon.browser.dao.custommapper.StakeBusinessMapper;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.mapper.NodeMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @description:
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-13 17:13:04
 **/
@RunWith(MockitoJUnitRunner.Silent.class)
public class NodeUpdateTaskTest extends AgentTestData {

    @Mock
    private BlockChainConfig chainConfig;

    @Mock
    private NodeMapper nodeMapper;

    @Mock
    private StakeBusinessMapper stakeBusinessMapper;

    @Before
    public void setup() {
        when(chainConfig.getKeyBase()).thenReturn("https://keybase.io/");
        when(chainConfig.getKeyBaseApi()).thenReturn("_/api/1.0/user/autocomplete.json?q=");
        Page<Node> page = new Page<>();
        page.addAll(nodeList);
        nodeList.forEach(n -> n.setExternalId("5FD68B690010632B"));
        when(nodeMapper.selectByExample(any())).thenReturn(page);
    }

}
