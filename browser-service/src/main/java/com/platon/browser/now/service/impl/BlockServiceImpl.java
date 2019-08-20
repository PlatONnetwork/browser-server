package com.platon.browser.now.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.platon.browser.dao.mapper.BlockMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.utils.Convert;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.BlockExample;
import com.platon.browser.dao.entity.BlockExample.Criteria;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.block.BlockDownload;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.NavigateEnum;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.now.service.BlockService;
import com.platon.browser.now.service.cache.StatisticCacheService;
import com.platon.browser.redis.dto.BlockRedis;
import com.platon.browser.req.PageReq;
import com.platon.browser.req.newblock.BlockDetailNavigateReq;
import com.platon.browser.req.newblock.BlockDetailsReq;
import com.platon.browser.req.newblock.BlockListByNodeIdReq;
import com.platon.browser.res.block.BlockDetailResp;
import com.platon.browser.res.block.BlockListResp;
import com.platon.browser.util.EnergonUtil;
import com.platon.browser.util.I18nUtil;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;

@Service
public class BlockServiceImpl implements BlockService {

	private final Logger logger = LoggerFactory.getLogger(BlockServiceImpl.class);
	
	@Autowired
	private StatisticCacheService statisticCacheService;

	@Autowired
	private BlockMapper blockMapper;
	
	@Autowired
	private I18nUtil i18n;

	@Override
	public RespPage<BlockListResp> blockList(PageReq req) {
		RespPage<BlockListResp> respPage = new RespPage<>();
		List<BlockListResp> lists = new LinkedList<>();
		// 小于50万条查询redis
		if (req.getPageNo().intValue() * req.getPageSize().intValue() < 500000) {
			List<BlockRedis> items = statisticCacheService.getBlockCache(req.getPageNo(), req.getPageSize());
			BlockListResp blockListNewResp = new BlockListResp();
			for (int i = 0; i < items.size(); i++) {
				blockListNewResp.setBlockReward(items.get(i).getBlockReward());
				blockListNewResp.setGasUsed(items.get(i).getGasUsed());
				blockListNewResp.setNodeId(items.get(i).getNodeId());
				blockListNewResp.setNodeName(items.get(i).getNodeName());
				blockListNewResp.setNumber(items.get(i).getNumber());
				blockListNewResp.setServerTime(new Date().getTime());
				blockListNewResp.setSize(items.get(i).getSize());
				blockListNewResp.setStatTxGasLimit(blockListNewResp.getStatTxGasLimit());
				blockListNewResp.setStatTxQty(items.get(i).getStatTxQty());
				blockListNewResp.setTimestamp(items.get(i).getTimestamp().getTime());
				lists.add(blockListNewResp);
			}
		} else {
			// TODO 是否查询超过五十万条数据
		}
		List<BlockRedis> blockEnd = statisticCacheService.getBlockCache(1, 1);
		Page<?> page = new Page<>(req.getPageNo(), req.getPageSize());
		if(blockEnd.isEmpty()) {
			page.setTotal(0);
		} else {
			page.setTotal(blockEnd.get(0).getNumber());
		}
		respPage.init(page, lists);
		return respPage;
	}

	@Override
	public RespPage<BlockListResp> blockListByNodeId(BlockListByNodeIdReq req) {
		BlockExample blockExample = new BlockExample();
		blockExample.setOrderByClause("number desc ");
		Criteria criteria = blockExample.createCriteria();
		criteria.andNodeIdEqualTo(req.getNodeId());
		PageHelper.startPage(req.getPageNo(), req.getPageSize());
		List<Block> blocks = blockMapper.selectByExample(blockExample);
		RespPage<BlockListResp> respPage = new RespPage<>();
		List<BlockListResp> lists = new LinkedList<>();
		for (Block block : blocks) {
			BlockListResp blockListResp = new BlockListResp();
			blockListResp.setBlockReward(block.getBlockReward());
			blockListResp.setGasUsed(block.getGasUsed());
			blockListResp.setNodeId(block.getNodeId());
			blockListResp.setNodeName(block.getNodeName());
			blockListResp.setNumber(block.getNumber());
			blockListResp.setServerTime(new Date().getTime());
			blockListResp.setSize(block.getSize());
			blockListResp.setStatTxGasLimit(block.getStatTxGasLimit());
			blockListResp.setStatTxQty(block.getStatTxQty());
			blockListResp.setTimestamp(block.getTimestamp().getTime());
			lists.add(blockListResp);
		}
		List<BlockRedis> blockEnd = statisticCacheService.getBlockCache(1, 1);
		Page<?> page = new Page<>(req.getPageNo(), req.getPageSize());
		if(blockEnd.isEmpty()) {
			page.setTotal(0);
		} else {
			page.setTotal(blockEnd.get(0).getNumber());
		}
		respPage.init(page, lists);
		return respPage;
	}

	@Override
	public BlockDownload blockListByNodeIdDownload(String nodeId, String date) {
		SimpleDateFormat ymdhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logger.info("导出数据起始日期：{},结束日期：{}",date,ymdhms.format(new Date()));
        // 限制最多导出3万条记录
        PageHelper.startPage(1,30000);
        BlockExample blockExample = new BlockExample();
        Criteria criteria = blockExample.createCriteria();
        criteria.andNodeIdEqualTo(nodeId);
        List<Block> blockList = blockMapper.selectByExample(blockExample);
        List<Object[]> rows = new ArrayList<>();
        blockList.forEach(block->{
            Object[] row = {
                    block.getNumber(),
                    ymdhms.format(block.getTimestamp()),
                    block.getStatTxQty(),
                    EnergonUtil.format(Convert.fromVon(block.getBlockReward(), Convert.Unit.LAT).setScale(18,RoundingMode.DOWN)),
                    EnergonUtil.format(Convert.fromVon(block.getGasUsed(), Convert.Unit.LAT).setScale(18,RoundingMode.DOWN))
            };
            rows.add(row);
        });

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Writer outputWriter = new OutputStreamWriter(baos, Charset.defaultCharset());
        try {
			outputWriter.write(new String(new byte[] { (byte) 0xEF, (byte) 0xBB,(byte) 0xBF }));
		} catch (IOException e) {
			e.printStackTrace();
		}
        CsvWriter writer = new CsvWriter(outputWriter, new CsvWriterSettings());
        writer.writeHeaders(
                i18n.i(I18nEnum.DOWNLOAD_BLOCK_CSV_NUMBER),
                i18n.i(I18nEnum.DOWNLOAD_BLOCK_CSV_TIMESTAMP),
                i18n.i(I18nEnum.DOWNLOAD_BLOCK_CSV_TRANSACTION_COUNT),
                i18n.i(I18nEnum.DOWNLOAD_BLOCK_CSV_REWARD),
                i18n.i(I18nEnum.DOWNLOAD_BLOCK_CSV_TXN_FEE)
        );
        writer.writeRowsAndClose(rows);
        BlockDownload blockDownload = new BlockDownload();
        blockDownload.setData(baos.toByteArray());
        blockDownload.setFilename("block-"+nodeId+"-"+date+".csv");
        blockDownload.setLength(baos.size());
        return blockDownload;
	}

	@Override
	public BlockDetailResp blockDetails(BlockDetailsReq req) {
		return this.queryBlockByNumber(req.getNumber().longValue());
	}

	@Override
	public BlockDetailResp blockDetailNavigate(BlockDetailNavigateReq req) {
		long blockNumber = req.getNumber().longValue();
		switch (NavigateEnum.valueOf(req.getDirection().toUpperCase())) {
			case PREV:
				blockNumber = -1;
				break;
			case NEXT:
				blockNumber = +1;
				break;
		}
		return this.queryBlockByNumber(blockNumber);
	}

	private BlockDetailResp queryBlockByNumber(long blockNumber) {
		Block block = blockMapper.selectByPrimaryKey(blockNumber);
		BlockDetailResp blockDetailResp = new BlockDetailResp();
		if (block != null) {
			blockDetailResp.setBlockReward(block.getBlockReward());
			blockDetailResp.setExtraData(block.getExtraData());
			blockDetailResp.setGasLimit(block.getGasLimit());
			blockDetailResp.setGasUsed(block.getGasUsed());
			blockDetailResp.setHash(block.getHash());
			blockDetailResp.setNodeId(block.getNodeId());
			blockDetailResp.setNodeName(block.getNodeName());
			blockDetailResp.setNumber(block.getNumber());
			blockDetailResp.setParentHash(block.getParentHash());
			blockDetailResp.setStatDelegateQty(block.getStatDelegateQty());
			blockDetailResp.setStatProposalQty(block.getStatProposalQty());
			blockDetailResp.setStatTransferQty(block.getStatTransferQty());
			blockDetailResp.setStatTxGasLimit(block.getStatTxGasLimit());
			blockDetailResp.setStatTxQty(block.getStatTxQty());
			blockDetailResp.setTimestamp(block.getTimestamp().getTime());
			
			// 取上一个区块
			BlockExample blockExample = new BlockExample();
	        blockExample.createCriteria().andNumberEqualTo(blockNumber - 1);
	        List<Block> blocks = blockMapper.selectByExample(blockExample);
	        if (blocks.size()>1){
	            logger.error("duplicate block: block number {}",blockNumber);
	            throw new BusinessException(RetEnum.RET_FAIL.getCode(), i18n.i(I18nEnum.BLOCK_ERROR_DUPLICATE));
	        }
	        if(blocks.size()==0){
	        	blockDetailResp.setTimeDiff(0l);
	            // 当前块没有上一个块证明这是第一个块, 设置first标识
	            blockDetailResp.setFirst(true);
	        }else{
	            Block prevBlock = blocks.get(0);
	            blockDetailResp.setTimeDiff(blockDetailResp.getTimestamp()-prevBlock.getTimestamp().getTime());
	        }
	
	        /** 设置last标识 **/
	        blockExample = new BlockExample();
	        blockExample.createCriteria().andNumberEqualTo(blockNumber+1);
	        blocks = blockMapper.selectByExample(blockExample);
	        if(blocks.size()==0){
	            // 当前区块没有下一个块，则表示这是最后一个块，设置last标识
	        	blockDetailResp.setLast(true);
	        }
			blockDetailResp.setTimestamp(block.getTimestamp().getTime());
		}
		return blockDetailResp;
	}

}
