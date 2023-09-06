package com.platon.browser.client;

import com.alibaba.fastjson.JSON;
import com.platon.browser.bean.ReceiptResult;
import com.platon.protocol.Web3j;
import com.platon.protocol.core.Request;
import com.platon.protocol.core.Response;
import com.platon.protocol.core.methods.response.bean.EconomicConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.concurrent.ExecutorService;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class PlatOnClientTest {

	@Mock
	private ExecutorService logDecodeExecutor;
	@Mock
	private RetryableClient retryableClient;
	@Mock
	private SpecialApi specialApi;

	@Spy
	private PlatOnClient target;

	@Test
	public void testInit() throws Exception {
		ReflectionTestUtils.setField(target,"logDecodeExecutor",logDecodeExecutor);
		ReflectionTestUtils.setField(target,"retryableClient",retryableClient);
		ReflectionTestUtils.setField(target,"specialApi",specialApi);
		ReflectionTestUtils.setField(target,"logDecodeThreadNum",6);

		ReflectionTestUtils.invokeMethod(target,"init");

		target.updateCurrentWeb3jWrapper();
		target.getWeb3jWrapper();

		ReceiptResult rr = getReceiptResult();
		when(specialApi.getReceiptResult(any(),any())).thenReturn(rr);
		target.getReceiptResult(33L);

		Web3jWrapper web3jWrapper = mock(Web3jWrapper.class);
		when(retryableClient.getWeb3jWrapper()).thenReturn(web3jWrapper);
		Web3j web3j = mock(Web3j.class);
		when(web3jWrapper.getWeb3j()).thenReturn(web3j);
		Request request = mock(Request.class);
		when(web3j.getEconomicConfig()).thenReturn(request);
		Response response = mock(Response.class);
		when(request.send()).thenReturn(response);
		EconomicConfig ec = new EconomicConfig();


		assertTrue(true);
	}


	private ReceiptResult getReceiptResult(){
		String json ="{\n" +
				"    \"jsonrpc\": \"2.0\",\n" +
				"    \"id\": 9430,\n" +
				"    \"result\": [\n" +
				"      {\n" +
				"        \"blockNumber\": 1388,\n" +
				"        \"gasUsed\": \"0x14bc8\",\n" +
				"        \"logs\": [\n" +
				"          {\n" +
				"            \"removed\": false,\n" +
				"            \"logIndex\": \"0\",\n" +
				"            \"transactionIndex\": null,\n" +
				"            \"transactionHash\": null,\n" +
				"            \"blockHash\": null,\n" +
				"            \"blockNumber\": null,\n" +
				"            \"address\": null,\n" +
				"            \"data\": \"0xe3a27b22436f6465223a302c2244617461223a22222c224572724d7367223a226f6b227d\",\n" +
				"            \"type\": null,\n" +
				"            \"topics\": null\n" +
				"          }\n" +
				"        ],\n" +
				"        \"transactionHash\": \"0x4a0ee1bb27b624e193ea3bc722eb47923acfaf01a5570294eeaea02d6580c494\",\n" +
				"        \"transactionIndex\": \"0x0\",\n" +
				"        \"status\": null,\n" +
				"        \"logStatus\": 1,\n" +
				"        \"failReason\": null\n" +
				"      }\n" +
				"    ]\n" +
				"  }";
		ReceiptResult rr = JSON.parseObject(json,ReceiptResult.class);
		return rr;
	}


}
