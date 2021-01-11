package com.platon.browser.service;

import com.github.pagehelper.Page;
import com.platon.browser.ApiTestMockBase;
import com.platon.browser.cache.TokenTransferRecordCacheDto;
import com.platon.browser.config.DownFileCommon;
import com.platon.browser.dao.entity.Erc20TokenTransferRecord;
import com.platon.browser.dao.mapper.CustomErc20TokenAddressRelMapper;
import com.platon.browser.dao.mapper.Erc20TokenAddressRelMapper;
import com.platon.browser.dao.mapper.Erc20TokenTransferRecordMapper;
import com.platon.browser.dao.mapper.NetworkStatMapper;
import com.platon.browser.elasticsearch.TokenTransferRecordESRepository;
import com.platon.browser.elasticsearch.bean.ESResult;
import com.platon.browser.elasticsearch.dto.ESTokenTransferRecord;
import com.platon.browser.request.token.QueryHolderTokenListReq;
import com.platon.browser.request.token.QueryTokenHolderListReq;
import com.platon.browser.request.token.QueryTokenTransferRecordListReq;
import com.platon.browser.response.RespPage;
import com.platon.browser.response.token.QueryHolderTokenListResp;
import com.platon.browser.response.token.QueryTokenHolderListResp;
import com.platon.browser.response.token.QueryTokenTransferRecordListResp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class Erc20TokenTransferRecordServiceApiTest extends ApiTestMockBase {
	@Mock
	private Erc20TokenTransferRecordMapper erc20TokenTransferRecordMapper;
	@Mock
	private TokenTransferRecordESRepository esTokenTransferRecordRepository;
	@Mock
	private Erc20TokenAddressRelMapper erc20TokenAddressRelMapper;
	@Mock
	private CustomErc20TokenAddressRelMapper customErc20TokenAddressRelMapper;
	@Mock
	private DownFileCommon downFileCommon;
	@Mock
	private NetworkStatMapper networkStatMapper;
	@InjectMocks
	@Spy
	private Erc20TokenTransferRecordService target;

	@Before
	public void setup() {
		when(i18n.i(any(),any(),any())).thenReturn("test");
		when(i18n.i(any(),any())).thenReturn("test");
	}

	@Test
	public void test_queryTokenRecordList() throws IOException {
		QueryTokenTransferRecordListReq req = new QueryTokenTransferRecordListReq();
		req.setPageNo(1);
		req.setPageSize(10);

		ESResult<ESTokenTransferRecord> queryResultFromES = new ESResult<>();
		List<Object> o = new ArrayList<>();
		this.esTokenTransferRecords.forEach(esTokenTransferRecord -> {
			o.add(esTokenTransferRecord);
		});
		queryResultFromES.setRsData(this.esTokenTransferRecords);
		queryResultFromES.setTotal(3l);
		doReturn(queryResultFromES).when(this.esTokenTransferRecordRepository).search(any(), any(), anyInt(), anyInt());
		TokenTransferRecordCacheDto trrcd = new TokenTransferRecordCacheDto();
		trrcd.setTransferRecordList(new ArrayList<>());
		RespPage page = new RespPage();
		page.setTotalCount(500);
		page.setTotalPages(40);
		trrcd.setPage(page);

		when(statisticCacheService.getTokenTransferRecordCache(any(),any())).thenReturn(trrcd);
		RespPage<QueryTokenTransferRecordListResp> queryTokenListResp = this.target.queryTokenRecordList(req);
		req.setAddress("lax196278ns22j23awdfj9f2d4vz0pedld8au6xelj");
		queryTokenListResp = this.target.queryTokenRecordList(req);
		req.setContract("lax196278ns22j23awdfj9f2d4vz0pedld8au6xelj");
		queryTokenListResp = this.target.queryTokenRecordList(req);
		req.setTxHash("lax196278ns22j23awdfj9f2d4vz0pedld8au6xelj");
		queryTokenListResp = this.target.queryTokenRecordList(req);
		assertTrue(true);
	}

	@Test
	public void test_tokenHolderList() {
		QueryTokenHolderListReq req = new QueryTokenHolderListReq();
		req.setContract("");
		req.setPageNo(1);
		req.setPageSize(10);

		Page page = new Page();
		page.addAll(this.erc20TokenAddressRels);
		when(this.erc20TokenAddressRelMapper.selectByExample(any())).thenReturn((page));

		RespPage<QueryTokenHolderListResp> queryTokenHolderListRespRespPage = this.target.tokenHolderList(req);
		assertTrue(true);
	}

	@Test
	public void test_holderTokenList() {
		QueryHolderTokenListReq req = new QueryHolderTokenListReq();
		req.setAddress("");
		req.setPageNo(1);
		req.setPageSize(10);

		Page page = new Page();
		page.addAll(this.erc20TokenAddressRels);
		when(this.erc20TokenAddressRelMapper.selectByExample(any())).thenReturn((page));

		RespPage<QueryHolderTokenListResp> queryHolderTokenListRespRespPage = this.target.holderTokenList(req);
		assertTrue(true);
	}

	@Test
	public void test_exportHolderTokenList() {
		Page page = new Page();
		page.addAll(this.erc20TokenAddressRels);
		when(this.erc20TokenAddressRelMapper.selectByExample(any())).thenReturn((page));

		HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
		this.target.exportHolderTokenList("", "en", "+8", "", httpServletResponse);
		assertTrue(true);
	}

	@Test
	public void test_exportTokenHolderList() {
		Page page = new Page();
		page.addAll(this.erc20TokenAddressRels);
		when(this.erc20TokenAddressRelMapper.selectByExample(any())).thenReturn((page));

		HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
		this.target.exportTokenHolderList("", "en", "+8", "", httpServletResponse);
		assertTrue(true);
	}

	@Test
	public void test_exportTokenTransferList() throws IOException {
		ESResult<Object> queryResultFromES = new ESResult<>();
		List<Object> o = new ArrayList<>();
		this.esTokenTransferRecords.forEach(esTokenTransferRecord -> {
			o.add(esTokenTransferRecord);
		});
		queryResultFromES.setTotal(3l);
		queryResultFromES.setRsData(o);
		when(this.esTokenTransferRecordRepository.search(any(), any(), anyInt(), anyInt())).thenReturn(queryResultFromES);
		HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
		this.target.exportTokenTransferList("", "", new Date().getTime(), "en", "+8", "", httpServletResponse);

		when(this.esTokenTransferRecordRepository.search(any(), any(), anyInt(), anyInt())).thenReturn(queryResultFromES);
		this.target.exportTokenTransferList("lax196278ns22j23awdfj9f2d4vz0pedld8au6xelj", "", new Date().getTime(), "en", "+8", "", httpServletResponse);
		this.target.exportTokenTransferList("", "lax196278ns22j23awdfj9f2d4vz0pedld8au6xelj", new Date().getTime(), "en", "+8", "", httpServletResponse);
		assertTrue(true);
	}

	@Test
	public void test_save() {
		this.target.save(new Erc20TokenTransferRecord());
		this.target.batchSave(new ArrayList<>());
		assertTrue(true);
	}


}
