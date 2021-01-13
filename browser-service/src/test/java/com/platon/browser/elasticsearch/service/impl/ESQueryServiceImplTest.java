package com.platon.browser.elasticsearch.service.impl;

import java.io.IOException;

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

import com.platon.browser.elasticsearch.BlockEsRepository;
import com.platon.browser.elasticsearch.DelegationEsRepository;
import com.platon.browser.elasticsearch.DelegationRewardEsRepository;
import com.platon.browser.elasticsearch.NodeOptEsRepository;
import com.platon.browser.elasticsearch.TransactionEsRepository;
import com.platon.browser.elasticsearch.dto.Block;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ESQueryServiceImplTest {

	@Spy
	private BlockEsRepository blockESRepository;
	
	@Spy
	private TransactionEsRepository transactionESRepository;
	
	@Spy
	private DelegationEsRepository delegationESRepository;
	
	@Spy
	private NodeOptEsRepository nodeOptESRepository;
	
	@Spy
	private DelegationRewardEsRepository delegationRewardESRepository;
	
	@Mock
	private RestHighLevelClient client;
	
	
	@Before
	public void setup() {
		ReflectionTestUtils.setField(blockESRepository,"client",client);
		ReflectionTestUtils.setField(transactionESRepository,"client",client);
		ReflectionTestUtils.setField(delegationESRepository,"client",client);
		ReflectionTestUtils.setField(nodeOptESRepository,"client",client);
		ReflectionTestUtils.setField(delegationRewardESRepository,"client",client);
	}
	
	@Test
	public void testBlockES() throws IOException {
		blockESRepository.setIndexName("browser_block_1");
		IndicesClient indicesClient = mock(IndicesClient.class);
		GetIndexRequest request = new GetIndexRequest("browser_block_1");
		when(client.indices()).thenReturn(indicesClient);
		when(indicesClient.exists(request, RequestOptions.DEFAULT)).thenReturn(false);
		if(!blockESRepository.existsIndex()) {
			blockESRepository.createIndex(null);
		}
		ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
		constructor.setDesc("num");
		constructor.setFrom(1);
		constructor.setSize(10);
		constructor.mustNot(new ESQueryBuilders().term("num", "1"));
		constructor.should(new ESQueryBuilders().term("nodeId", "0x"));
		constructor.setResult(new String[] {"num"});
		
		SearchResponse response = mock(SearchResponse.class);
		when(client.search(anyObject(), anyObject())).thenReturn(response);
		SearchHits searchHits = mock(SearchHits.class);
		when(response.getHits()).thenReturn(searchHits);
		TotalHits totalHits = mock(TotalHits.class);
		when(searchHits.getTotalHits()).thenReturn(totalHits);
		SearchHit[] s = new SearchHit[] {};
		when(searchHits.getHits()).thenReturn(s);
		try {
			blockESRepository.search(constructor, Block.class, -1, 100);
		} catch (IOException e) {
			
		}
		blockESRepository.deleteIndex();
	}
	
	@Test
	public void testTransactionES() throws IOException {
		transactionESRepository.setIndexName("browser_transaction_1");
		IndicesClient indicesClient = mock(IndicesClient.class);
		GetIndexRequest request = new GetIndexRequest("browser_transaction_1");
		when(client.indices()).thenReturn(indicesClient);
		when(indicesClient.exists(request, RequestOptions.DEFAULT)).thenReturn(false);
		if(!transactionESRepository.existsIndex()) {
			transactionESRepository.createIndex(null);
		}
		transactionESRepository.deleteIndex();
	}
	
	@Test
	public void testDelegationES() throws IOException {
		delegationESRepository.setIndexName("browser_delegation_1");
		IndicesClient indicesClient = mock(IndicesClient.class);
		GetIndexRequest request = new GetIndexRequest("browser_delegation_1");
		when(client.indices()).thenReturn(indicesClient);
		when(indicesClient.exists(request, RequestOptions.DEFAULT)).thenReturn(false);
		if(!delegationESRepository.existsIndex()) {
			delegationESRepository.createIndex(null);
		}
		delegationESRepository.deleteIndex();
	}
	
	@Test
	public void testNodeOptES() throws IOException {
		nodeOptESRepository.setIndexName("browser_nodeOpt_1");
		IndicesClient indicesClient = mock(IndicesClient.class);
		GetIndexRequest request = new GetIndexRequest("browser_nodeOpt_1");
		when(client.indices()).thenReturn(indicesClient);
		when(indicesClient.exists(request, RequestOptions.DEFAULT)).thenReturn(false);
		if(!nodeOptESRepository.existsIndex()) {
			nodeOptESRepository.createIndex(null);
		}
		nodeOptESRepository.deleteIndex();
	}
	
	@Test
	public void testDelegationRewardES() throws IOException {
		delegationRewardESRepository.setIndexName("browser_delegationReward_1");
		IndicesClient indicesClient = mock(IndicesClient.class);
		GetIndexRequest request = new GetIndexRequest("browser_delegationReward_1");
		when(client.indices()).thenReturn(indicesClient);
		when(indicesClient.exists(request, RequestOptions.DEFAULT)).thenReturn(false);
		if(!delegationRewardESRepository.existsIndex()) {
			delegationRewardESRepository.createIndex(null);
		}
		delegationRewardESRepository.deleteIndex();
	}
	
}
