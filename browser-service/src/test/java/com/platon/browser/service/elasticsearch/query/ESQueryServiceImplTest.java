package com.platon.browser.service.elasticsearch.query;

import com.platon.browser.config.EsIndexConfig;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.service.elasticsearch.*;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ESQueryServiceImplTest {

    @Spy
    private EsBlockRepository ESBlockRepository;

    @Spy
    private EsTransactionRepository ESTransactionRepository;

    @Spy
    private EsDelegationRepository ESDelegationRepository;

    @Spy
    private EsNodeOptRepository ESNodeOptRepository;

    @Spy
    private EsDelegationRewardRepository ESDelegationRewardRepository;

    @Mock
    private RestHighLevelClient client;


    @Before
    public void setup() {
        EsIndexConfig config = new EsIndexConfig();
        config.setBlockIndexName("browser_block_1");
        config.setTransactionIndexName("browser_transaction_1");
        config.setDelegationIndexName("browser_delegation_1");
        config.setNodeOptIndexName("browser_nodeopt_1");
        config.setDelegationRewardIndexName("browser_delegationreward_1");

        ReflectionTestUtils.setField(ESBlockRepository, "config", config);
        ReflectionTestUtils.setField(ESBlockRepository, "client", client);
        ReflectionTestUtils.setField(ESTransactionRepository, "config", config);
        ReflectionTestUtils.setField(ESTransactionRepository, "client", client);
        ReflectionTestUtils.setField(ESDelegationRepository, "config", config);
        ReflectionTestUtils.setField(ESDelegationRepository, "client", client);
        ReflectionTestUtils.setField(ESNodeOptRepository, "config", config);
        ReflectionTestUtils.setField(ESNodeOptRepository, "client", client);
        ReflectionTestUtils.setField(ESDelegationRewardRepository, "config", config);
        ReflectionTestUtils.setField(ESDelegationRewardRepository, "client", client);
    }

    @Test
    public void testBlockES() throws IOException {

        IndicesClient indicesClient = mock(IndicesClient.class);
        GetIndexRequest request = new GetIndexRequest("browser_block_1");
        when(client.indices()).thenReturn(indicesClient);
        when(indicesClient.exists(request, RequestOptions.DEFAULT)).thenReturn(false);

        if (!ESBlockRepository.existsIndex()) {
            ESBlockRepository.createIndex(null);
        }
        ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
        constructor.setDesc("num");
        constructor.setFrom(1);
        constructor.setSize(10);
        constructor.mustNot(new ESQueryBuilders().term("num", "1"));
        constructor.should(new ESQueryBuilders().term("nodeId", "0x"));
        constructor.setResult(new String[]{"num"});

        SearchResponse response = mock(SearchResponse.class);
        when(client.search(anyObject(), anyObject())).thenReturn(response);
        SearchHits searchHits = mock(SearchHits.class);
        when(response.getHits()).thenReturn(searchHits);
        TotalHits totalHits = mock(TotalHits.class);
        when(searchHits.getTotalHits()).thenReturn(totalHits);
        SearchHit[] s = new SearchHit[]{};
        when(searchHits.getHits()).thenReturn(s);
        try {
            ESBlockRepository.search(constructor, Block.class, -1, 100);
        } catch (IOException e) {

        }
        ESBlockRepository.deleteIndex();
    }

    @Test
    public void testTransactionES() throws IOException {
        IndicesClient indicesClient = mock(IndicesClient.class);
        GetIndexRequest request = new GetIndexRequest("browser_transaction_1");
        when(client.indices()).thenReturn(indicesClient);
        when(indicesClient.exists(request, RequestOptions.DEFAULT)).thenReturn(false);
        if (!ESTransactionRepository.existsIndex()) {
            ESTransactionRepository.createIndex(null);
        }
        ESTransactionRepository.deleteIndex();
    }

    @Test
    public void testDelegationES() throws IOException {
        IndicesClient indicesClient = mock(IndicesClient.class);
        GetIndexRequest request = new GetIndexRequest("browser_delegation_1");
        when(client.indices()).thenReturn(indicesClient);
        when(indicesClient.exists(request, RequestOptions.DEFAULT)).thenReturn(false);
        if (!ESDelegationRepository.existsIndex()) {
            ESDelegationRepository.createIndex(null);
        }
        ESDelegationRepository.deleteIndex();
    }

    @Test
    public void testNodeOptES() throws IOException {
        IndicesClient indicesClient = mock(IndicesClient.class);
        GetIndexRequest request = new GetIndexRequest("browser_nodeOpt_1");
        when(client.indices()).thenReturn(indicesClient);
        when(indicesClient.exists(request, RequestOptions.DEFAULT)).thenReturn(false);
        if (!ESNodeOptRepository.existsIndex()) {
            ESNodeOptRepository.createIndex(null);
        }
        ESNodeOptRepository.deleteIndex();
    }

    @Test
    public void testDelegationRewardES() throws IOException {
        IndicesClient indicesClient = mock(IndicesClient.class);
        GetIndexRequest request = new GetIndexRequest("browser_delegationReward_1");
        when(client.indices()).thenReturn(indicesClient);
        when(indicesClient.exists(request, RequestOptions.DEFAULT)).thenReturn(false);
        if (!ESDelegationRewardRepository.existsIndex()) {
            ESDelegationRewardRepository.createIndex(null);
        }
        ESDelegationRewardRepository.deleteIndex();
    }

}
