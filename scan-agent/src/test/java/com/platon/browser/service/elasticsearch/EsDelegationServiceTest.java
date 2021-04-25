package com.platon.browser.service.elasticsearch;

import com.platon.browser.AgentTestBase;
import com.platon.browser.dao.entity.Delegation;
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
public class EsDelegationServiceTest extends AgentTestBase {
    @Mock
    private EsDelegationRepository ESDelegationRepository;
    @Spy
    private EsDelegationService target;

    @Before
    public void setup(){
        ReflectionTestUtils.setField(target, "ESDelegationRepository", ESDelegationRepository);
    }

    /**
     * 根据区块号获取激励池余额
     */
    @Test(expected = Exception.class)
    public void save() throws IOException {
        target.save(Collections.emptySet());
        Set<Delegation> data = new HashSet<>();
        data.add(new Delegation());
        target.save(data);
        doThrow(new RuntimeException("")).when(ESDelegationRepository).bulkAddOrUpdate(anyMap());
        target.save(data);
    }
}
