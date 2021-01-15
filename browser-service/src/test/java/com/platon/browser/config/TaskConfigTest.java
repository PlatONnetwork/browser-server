package com.platon.browser.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.io.IOException;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.Silent.class)
public class TaskConfigTest {

    @Spy
    private TaskConfig target;

    @Test
    public void test() throws IOException {
        target.configureTasks(mock(ScheduledTaskRegistrar.class));
        assertTrue(true);
    }
}
