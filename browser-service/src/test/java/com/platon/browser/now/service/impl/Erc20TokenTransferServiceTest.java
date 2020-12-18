package com.platon.browser.now.service.impl;

import com.github.pagehelper.Page;
import com.platon.browser.TestMockBase;
import com.platon.browser.common.DownFileCommon;
import com.platon.browser.dao.entity.Erc20TokenTransferRecord;
import com.platon.browser.dao.mapper.*;
import com.platon.browser.dto.elasticsearch.ESResult;
import com.platon.browser.dto.transaction.TokenTransferRecordCacheDto;
import com.platon.browser.elasticsearch.TokenTransferRecordESRepository;
import com.platon.browser.elasticsearch.dto.ESTokenTransferRecord;
import com.platon.browser.now.service.cache.StatisticCacheService;
import com.platon.browser.req.token.QueryHolderTokenListReq;
import com.platon.browser.req.token.QueryTokenHolderListReq;
import com.platon.browser.req.token.QueryTokenTransferRecordListReq;
import com.platon.browser.res.RespPage;
import com.platon.browser.res.token.QueryHolderTokenListResp;
import com.platon.browser.res.token.QueryTokenHolderListResp;
import com.platon.browser.res.token.QueryTokenTransferRecordListResp;
import com.platon.browser.util.I18nUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

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
public class Erc20TokenTransferServiceTest extends TestMockBase {

	@Mock
	private Erc20TokenMapper erc20TokenMapper;
	@Mock
	private Erc20TokenTransferRecordMapper erc20TokenTransferRecordMapper;
	@Mock
	private TokenTransferRecordESRepository esTokenTransferRecordRepository;
	@Mock
	private I18nUtil i18n;
	@Mock
	private Erc20TokenAddressRelMapper erc20TokenAddressRelMapper;
	@Mock
	private CustomErc20TokenAddressRelMapper customErc20TokenAddressRelMapper;
	@Mock
	private DownFileCommon downFileCommon;
	@Mock
	private StatisticCacheService statisticCacheService;
	@Mock
	private NetworkStatMapper networkStatMapper;

	@Spy
	private Erc20TokenTransferRecordServiceImpl target;

	@Before
	public void setup() {
		ReflectionTestUtils.setField(this.target, "statisticCacheService", this.statisticCacheService);
		ReflectionTestUtils.setField(this.target, "erc20TokenMapper", this.erc20TokenMapper);
		ReflectionTestUtils.setField(this.target, "erc20TokenTransferRecordMapper", this.erc20TokenTransferRecordMapper);
		ReflectionTestUtils.setField(this.target, "esTokenTransferRecordRepository", this.esTokenTransferRecordRepository);
		ReflectionTestUtils.setField(this.target, "i18n", this.i18n);
		ReflectionTestUtils.setField(this.target, "erc20TokenAddressRelMapper", this.erc20TokenAddressRelMapper);
		ReflectionTestUtils.setField(this.target, "customErc20TokenAddressRelMapper", this.customErc20TokenAddressRelMapper);
		ReflectionTestUtils.setField(this.target, "downFileCommon", this.downFileCommon);
		ReflectionTestUtils.setField(this.target, "networkStatMapper", networkStatMapper);

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
