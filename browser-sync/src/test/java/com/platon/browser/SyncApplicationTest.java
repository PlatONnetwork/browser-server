package com.platon.browser;

import com.platon.browser.service.SyncService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.ApplicationArguments;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Set;

@RunWith(MockitoJUnitRunner.Silent.class)
public class SyncApplicationTest {
    @Mock
    private SyncService syncService;
    @Spy
    private SyncApplication target;
    @Test
    public void test(){
        ReflectionTestUtils.setField(target, "syncService", syncService);
        SyncService.setBlockSyncDone(true);
        SyncService.setTransactionSyncDone(true);
        target.run(new ApplicationArguments() {
            @Override
            public String[] getSourceArgs() {
                return new String[0];
            }

            @Override
            public Set<String> getOptionNames() {
                return null;
            }

            @Override
            public boolean containsOption(String name) {
                return false;
            }

            @Override
            public List<String> getOptionValues(String name) {
                return null;
            }

            @Override
            public List<String> getNonOptionArgs() {
                return null;
            }
        });
    }
}
