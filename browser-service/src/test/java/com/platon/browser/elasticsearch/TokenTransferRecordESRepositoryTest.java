package com.platon.browser.elasticsearch;

import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @Author: AgentRJ
 * @Date: 2020/9/24
 * @Version 1.0
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class TokenTransferRecordESRepositoryTest {
    @Mock
    private RestHighLevelClient client;

    @Spy
    private TokenTransferRecordESRepository target;

    @Before
    public void setup()throws Exception{
        ReflectionTestUtils.setField(target,"client",client);
        ReflectionTestUtils.setField(target,"indexName","client");
        IndicesClient indicesClient = mock(IndicesClient.class);
        when(client.indices()).thenReturn(indicesClient);
        CreateIndexResponse createIndexResponse = mock(CreateIndexResponse.class);
        when(indicesClient.create(any(CreateIndexRequest.class),any(RequestOptions.class))).thenReturn(createIndexResponse);
    }

    @Test
    public void testTemplateExists() {
        try {
            boolean exists = target.existsTemplate(target.getDefaultIndexTemplateName());
            target.putIndexTemplate(target.getDefaultIndexTemplateName(), target.defaultIndexTemplate());
            boolean after = target.existsTemplate(target.getDefaultIndexTemplateName());
            System.out.println("..");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}