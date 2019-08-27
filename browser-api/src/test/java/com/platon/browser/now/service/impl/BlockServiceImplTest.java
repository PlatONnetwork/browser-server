package com.platon.browser.now.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.platon.browser.BrowserApiApplication;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.block.BlockDownload;
import com.platon.browser.now.service.BlockService;
import com.platon.browser.req.PageReq;
import com.platon.browser.req.newblock.BlockDetailNavigateReq;
import com.platon.browser.req.newblock.BlockDetailsReq;
import com.platon.browser.req.newblock.BlockListByNodeIdReq;
import com.platon.browser.res.block.BlockDetailResp;
import com.platon.browser.res.block.BlockListResp;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BrowserApiApplication.class)
public class BlockServiceImplTest {

	@Autowired
    private BlockService blockService;
	
	@Test
	public void blockList() {
		PageReq pageReq = new PageReq();
        RespPage<BlockListResp> pages = blockService.blockList(pageReq);
        assertTrue(pages.getData().size()>=0);
	}
	
	@Test
	public void blockListByNodeId() {
		BlockListByNodeIdReq req = new BlockListByNodeIdReq();
		req.setNodeId("0xef97cb9caf757c70e9aca9062a9f6607ce89c3e7cac90ffee56d3fcffffa55aebd20b48c0db3924438911fd1c88c297d6532b434c56dbb5d9758f0794c6841dc");
		RespPage<BlockListResp> pages = blockService.blockListByNodeId(req);
		assertTrue(pages.getData().size()>=0);
	}
	
	@Test
	public void blockDetails() {
		BlockDetailsReq req = new BlockDetailsReq();
		req.setNumber(3);
		BlockDetailResp blockDetailResp = blockService.blockDetails(req);
		assertNotNull(blockDetailResp);
	}
	
	@Test
	public void blockDetailNavigate() {
		BlockDetailNavigateReq req = new BlockDetailNavigateReq();
		req.setNumber(2l);
		req.setDirection("next");
		BlockDetailResp blockDetailResp = blockService.blockDetailNavigate(req);
		assertNotNull(blockDetailResp);
	}
	
	@Test
	public void blockListByNodeIdDownload() {
		String nodeId = "0xef97cb9caf757c70e9aca9062a9f6607ce89c3e7cac90ffee56d3fcffffa55aebd20b48c0db3924438911fd1c88c297d6532b434c56dbb5d9758f0794c6841dc";
		String date = "2019-08-22";
		BlockDownload download = blockService.blockListByNodeIdDownload(nodeId, date);
		assertNotNull(download.getData());
	}
	
}
