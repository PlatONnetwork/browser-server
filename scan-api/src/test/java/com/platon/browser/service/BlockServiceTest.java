package com.platon.browser.service;

import com.platon.browser.ApiTestMockBase;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.request.PageReq;
import com.platon.browser.request.newblock.BlockDetailNavigateReq;
import com.platon.browser.request.newblock.BlockDetailsReq;
import com.platon.browser.request.newblock.BlockDownload;
import com.platon.browser.request.newblock.BlockListByNodeIdReq;
import com.platon.browser.response.RespPage;
import com.platon.browser.response.block.BlockDetailResp;
import com.platon.browser.response.block.BlockListResp;
import com.platon.browser.service.elasticsearch.bean.ESResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class BlockServiceTest extends ApiTestMockBase {

	@Spy
    private BlockService target;

	@Before
	public void setup() throws IOException {
		ReflectionTestUtils.setField(target,"i18n",i18n);
		ReflectionTestUtils.setField(target,"statisticCacheService",statisticCacheService);
		ReflectionTestUtils.setField(target,"commonService",commonService);
		ReflectionTestUtils.setField(target,"esBlockRepository", ESBlockRepository);
		ReflectionTestUtils.setField(target,"customNodeMapper", customNodeMapper);
	}

	@Test
	public void blockDetails() throws IOException {
		BlockDetailsReq req = new BlockDetailsReq();
		req.setNumber(440);
		BlockDetailResp blockDetailResp = target.blockDetails(req);

		req.setNumber(0);
		blockDetailResp = target.blockDetails(req);
		assertNotNull(blockDetailResp);
	}

	@Test
	public void blockList() throws IOException {
		List<Block> blocks = new ArrayList<>();
		Block block = new Block();
		block.setNum(10l);
		block.setReward("10");
		block.setTime(new Date());
		blocks.add(block);

		Block block1 = new Block();
		block1.setNum(110l);
		block1.setReward("10");
		block1.setTime(new Date());
		blocks.add(block1);
		when(statisticCacheService.getBlockCache(any(),any())).thenReturn(blocks);

		PageReq pageReq = new PageReq();
        RespPage<BlockListResp> pages = target.blockList(pageReq);

        pageReq.setPageNo(10);
        target.blockList(pageReq);

        pageReq.setPageNo(100000);
        ESResult<Object> blockEs = new ESResult<>();
        List<Object> blockList = new ArrayList<>();
        blockList.add(block);
        blockList.add(block1);
        blockEs.setRsData(blockList);
        blockEs.setTotal(2l);
        when(ESBlockRepository.search(any(), any(), anyInt(),anyInt())).thenReturn(blockEs);
        target.blockList(pageReq);
        assertTrue(pages.getData().size()>=0);
	}

	@Test
	public void blockDetailNavigate() {
		BlockDetailNavigateReq req = new BlockDetailNavigateReq();
		req.setNumber(0l);
		req.setDirection("next");
		BlockDetailResp blockDetailResp = target.blockDetailNavigate(req);

		req.setNumber(0l);
		req.setDirection("prev");
		target.blockDetailNavigate(req);
		assertNotNull(blockDetailResp);
	}

	@Test
	public void blockListByNodeId() throws IOException {
		BlockListByNodeIdReq req = new BlockListByNodeIdReq();
		req.setNodeId("0x");
		ESResult<Object> blockEs = new ESResult<>();
		List<Block> blocks = new ArrayList<>();
		Block block = new Block();
		block.setNum(10l);
		block.setReward("10");
		block.setTime(new Date());
		blocks.add(block);

		Block block1 = new Block();
		block1.setNum(110l);
		block1.setReward("10");
		block1.setTime(new Date());
		blocks.add(block1);
        List<Object> blockList = new ArrayList<>();
        blockList.add(block);
        blockList.add(block1);
        blockEs.setRsData(blockList);
        blockEs.setTotal(2l);
        when(ESBlockRepository.search(any(), any(), anyInt(),anyInt())).thenReturn(blockEs);
		RespPage<BlockListResp> pages = target.blockListByNodeId(req);

		assertNotNull(pages);
	}

	@Test
	public void blockListByNodeIdDownload() throws IOException {
		ESResult<Object> blockEs = new ESResult<>();
		List<Block> blocks = new ArrayList<>();
		Block block = new Block();
		block.setNum(10l);
		block.setReward("10");
		block.setTime(new Date());
		block.setTxFee("10");
		blocks.add(block);

		Block block1 = new Block();
		block1.setNum(110l);
		block1.setReward("10");
		block1.setTime(new Date());
		block1.setTxFee("10");
		blocks.add(block1);
        List<Object> blockList = new ArrayList<>();
        blockList.add(block);
        blockList.add(block1);
        blockEs.setRsData(blockList);
        blockEs.setTotal(2l);
        when(ESBlockRepository.search(any(), any(), anyInt(),anyInt())).thenReturn(blockEs);

        when(i18n.i(any(), any(), any())).thenReturn("test");
        BlockDownload blpBlockDownload = target.blockListByNodeIdDownload("0x", new Date().getTime(), "en_US", "+8");

		assertNotNull(blpBlockDownload);
	}
}
