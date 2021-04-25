package com.platon.browser.service.elasticsearch;

import com.platon.browser.AgentTestBase;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.anyMap;
import static org.mockito.Mockito.doThrow;

@RunWith(MockitoJUnitRunner.Silent.class)
public class EsNodeOptServiceTest extends AgentTestBase {
    @Mock
    private EsNodeOptRepository ESNodeOptRepository;
    @Spy
    private EsNodeOptService target;

    @Before
    public void setup(){
        ReflectionTestUtils.setField(target, "ESNodeOptRepository", ESNodeOptRepository);
    }

    /**
     * 根据区块号获取激励池余额
     */
    @Test(expected = Exception.class)
    public void save() throws IOException {
        target.save(Collections.emptySet());
        Set<NodeOpt> data = new HashSet<>();
        NodeOpt nodeOpt = new NodeOpt();
        nodeOpt.setId(888L);
        data.add(nodeOpt);
        target.save(data);

        data.clear();
        data.add(new NodeOpt());
        target.save(data);
        doThrow(new RuntimeException("")).when(ESNodeOptRepository).bulkAddOrUpdate(anyMap());
        target.save(data);
    }
}
