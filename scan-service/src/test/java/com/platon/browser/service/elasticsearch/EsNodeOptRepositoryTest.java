package com.platon.browser.service.elasticsearch;

import com.platon.browser.config.EsIndexConfig;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @Auther: dongqile
 * @Date: 2019/12/4
 * @Description:
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class EsNodeOptRepositoryTest {


    @Mock
    private RestHighLevelClient client;

    @Spy
    private EsNodeOptRepository target;

    @Before
    public void setup()throws Exception{
        ReflectionTestUtils.setField(target,"client",client);
        target.config = new EsIndexConfig();
        target.config.setNodeOptIndexName("alaya_browser_hrpatp201018v10000_nodeopt");
        IndicesClient indicesClient = mock(IndicesClient.class);
        when(client.indices()).thenReturn(indicesClient);
        CreateIndexResponse createIndexResponse = mock(CreateIndexResponse.class);
        when(indicesClient.create(any(CreateIndexRequest.class),any(RequestOptions.class))).thenReturn(createIndexResponse);
    }

    @Test
    public void createIndexTest()throws Exception{
        Map <String,String> map = new HashMap <>();
        map.put("","");
        target.createIndex(map);
    }

    @Test
    public void existsIndexTest()throws Exception{
        target.existsIndex();
    }

    @Test
    public void deleteIndexTest()throws Exception{
        target.deleteIndex();
    }

    @Test
    public void addTest()throws Exception{
        target.add("Test","test");
    }

    @Test
    public void existsTest()throws Exception{
        target.exists("1");
    }
/*
    @Test
    public void getTest()throws Exception{
        target.get("Test",String.class);
    }*/

    @Test
    public void updateTest()throws Exception{
        target.update("Test","test1");
    }

    @Test
    public void deleteTest()throws Exception{
        target.delete("Test");
    }

    @Test
    public void bulkDeleteTest()throws Exception{
        List <String> list = new ArrayList <>();
        list.add("Test");
        target.bulkDelete(list);
    }

    @Test
    public void bulkAddOrUpdateTest()throws Exception{
        Map<String,String> map = new HashMap <>();
        map.put("Test","aaaa");
        target.bulkAddOrUpdate(map);
    }

}
