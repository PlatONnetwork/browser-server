package com.platon.browser.config;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class EsClusterConfigTest {

    @Value("${spring.elasticsearch.high-level-client.hosts}")
    private List<String> addresses; // 集群地址，多个用,隔开

    @Value("${spring.elasticsearch.high-level-client.port}")
    private int port; // 使用的端口号

    @Value("${spring.elasticsearch.high-level-client.schema}")
    private String schema; // 使用的协议

    @Spy
    private EsClusterConfig target;

    @Before
    public void setup() {
        List<String> addresses = new ArrayList<>();
        addresses.add("12.12.12.12");
        ReflectionTestUtils.setField(target,"hosts",addresses);
        ReflectionTestUtils.setField(target, "port", 3365);
        ReflectionTestUtils.setField(target, "schema", "ws");
        ReflectionTestUtils.setField(target, "username", "u");
        ReflectionTestUtils.setField(target, "password", "p");
    }

    @Test
    public void test() throws IOException {
        target.client();
        assertTrue(true);
    }

}
