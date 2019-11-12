package com.platon.browser.now.service.impl;

import com.github.pagehelper.Page;
import com.platon.browser.common.BrowserConst;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.elasticsearch.BlockESRepository;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.ESResult;
import com.platon.browser.elasticsearch.service.impl.ESQueryBuilderConstructor;
import com.platon.browser.elasticsearch.service.impl.ESQueryBuilders;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.NavigateEnum;
import com.platon.browser.now.service.BlockService;
import com.platon.browser.now.service.CommonService;
import com.platon.browser.now.service.cache.StatisticCacheService;
import com.platon.browser.req.PageReq;
import com.platon.browser.req.newblock.BlockDetailNavigateReq;
import com.platon.browser.req.newblock.BlockDetailsReq;
import com.platon.browser.req.newblock.BlockDownload;
import com.platon.browser.req.newblock.BlockListByNodeIdReq;
import com.platon.browser.res.RespPage;
import com.platon.browser.res.block.BlockDetailResp;
import com.platon.browser.res.block.BlockListResp;
import com.platon.browser.util.DateUtil;
import com.platon.browser.util.EnergonUtil;
import com.platon.browser.util.I18nUtil;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.utils.Convert;

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

/**
 *  区块方法逻辑具体实现
 *  @file BlockServiceImpl.java
 *  @description
 *	@author zhangrj
 *  @data 2019年8月31日
 */
@Service
public class BlockServiceImpl implements BlockService {

	private final Logger logger = LoggerFactory.getLogger(BlockServiceImpl.class);

	@Autowired
	private StatisticCacheService statisticCacheService;

	@Autowired
	private BlockESRepository blockESRepository;

	@Autowired
	private I18nUtil i18n;
	
	@Autowired
	private CommonService commonService;

	@Override
	public RespPage<BlockListResp> blockList(PageReq req) {
		long startTime = System.currentTimeMillis();
		RespPage<BlockListResp> respPage = new RespPage<>();
		List<BlockListResp> lists = new LinkedList<>();
		/** 查询现阶段最大区块数 */
		NetworkStat networkStatRedis = statisticCacheService.getNetworkStatCache();
		Long bNumber = networkStatRedis.getCurNumber();
		logger.debug("perform-blockList,time1:{}", System.currentTimeMillis() - startTime);
		/** 小于50万条查询redis */
		if (req.getPageNo().intValue() * req.getPageSize().intValue() < BrowserConst.MAX_NUM) {
			/**
			 * 当页号等于1，重新获取数据，与首页保持一致
			 */
			List<Block> items;
			if(req.getPageNo().intValue() == 1) {
				/** 查询缓存最新的八条区块信息 */
				items = statisticCacheService.getBlockCache(0,1);
				logger.debug("perform-blockList,time2:{}", System.currentTimeMillis() - startTime);
				if(!items.isEmpty()) {
					/**
					 * 如果统计区块小于区块交易则重新查询新的区块
					 */
					Long dValue = items.get(0).getNum()-bNumber;
					if(dValue > 0) {
						items = statisticCacheService.getBlockCache(dValue.intValue()/req.getPageSize()+1,req.getPageSize());
					} else {
						items = statisticCacheService.getBlockCache(req.getPageNo(), req.getPageSize());
					}
				}
				logger.debug("perform-blockList,time3:{}", System.currentTimeMillis() - startTime);
			} else {
				items = statisticCacheService.getBlockCache(req.getPageNo(), req.getPageSize());
			}
			logger.debug("perform-blockList,time4:{}", System.currentTimeMillis() - startTime);
			for (Block blockRedis:items) {
				BlockListResp blockListNewResp = this.transferBlockListResp(blockRedis);
				lists.add(blockListNewResp);
			}
		} else {
			/** 查询超过五十万条数据，根据区块号倒序 */
			ESResult<Block> blocks = new ESResult<>();
			ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
			constructor.setDesc("num");
			try {
				blocks = blockESRepository.search(constructor, Block.class, req.getPageNo(), req.getPageSize());
			} catch (IOException e) {
				logger.error("获取区块错误。", e);
			}
			for(Block block:blocks.getRsData()) {
				BlockListResp blockListNewResp = this.transferBlockListResp(block);
				lists.add(blockListNewResp);
			}
		}
		bNumber = lists.get(0).getNumber();
		logger.debug("perform-blockList,time5:{}", System.currentTimeMillis() - startTime);
		Page<?> page = new Page<>(req.getPageNo(), req.getPageSize());
		page.setTotal(bNumber);
		respPage.init(page, lists);
		if(System.currentTimeMillis() - startTime > 100) {
			logger.debug("perform-blockList,time6:{}", System.currentTimeMillis() - startTime);
		}
		return respPage;
	}
	
	private BlockListResp transferBlockListResp(Block block) {
		BlockListResp blockListResp = new BlockListResp();
		BeanUtils.copyProperties(block, blockListResp);
		blockListResp.setBlockReward(block.getReward().toString());
		blockListResp.setNumber(block.getNum());
		blockListResp.setStatTxGasLimit(block.getTxGasLimit().toString());
		blockListResp.setStatTxQty(block.getTxQty());
		blockListResp.setServerTime(new Date().getTime());
		blockListResp.setTimestamp(block.getTime().getTime());
		blockListResp.setGasUsed(block.getGasUsed().toString());
//		blockListResp.setNodeName(commonService.getNodeName(block.getNodeId()));
		return blockListResp;
	}

	@Override
	public RespPage<BlockListResp> blockListByNodeId(BlockListByNodeIdReq req) {
		/** 根据nodeId 查询区块列表，以区块号倒序  */
		
		ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
		constructor.must(new ESQueryBuilders().term("nodeId", req.getNodeId()));
		constructor.setDesc("num");
		ESResult<Block> blocks = new ESResult<>();
		try {
			blocks = blockESRepository.search(constructor, Block.class, req.getPageNo(), req.getPageSize());
		} catch (IOException e) {
			logger.error("获取区块错误。", e);
		}
		/** 初始化返回对象 */
		RespPage<BlockListResp> respPage = new RespPage<>();
		List<BlockListResp> lists = new LinkedList<>();
		for (Block block : blocks.getRsData()) {
			BlockListResp blockListResp = this.transferBlockListResp(block);
			lists.add(blockListResp);
		}
		/** 设置返回的分页数据 */
		Page<?> page = new Page<>(req.getPageNo(), req.getPageSize());
		if(blocks.getTotal() > 5000) {
			page.setTotal(5000);
		} else {
			page.setTotal(blocks.getTotal());
		}
		respPage.init(page, lists);
		return respPage;
	}

	@Override
	public BlockDownload blockListByNodeIdDownload(String nodeId, Long date, String local, String timeZone) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date now = new Date();
		String msg = dateFormat.format(now);
        logger.info("导出数据起始日期：{},结束时间：{}",dateFormat.format(new Date(date)),msg);
        /** 限制最多导出3万条记录 */
        /** 设置根据时间和nodeId查询数据 */
		
		ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
		constructor.must(new ESQueryBuilders().term("nodeId", nodeId));
		constructor.must(new ESQueryBuilders().range("time", new Date(date).getTime(), now.getTime()));
		constructor.setDesc("num");
		ESResult<Block> blockList = new ESResult<>();
		try {
			blockList = blockESRepository.search(constructor, Block.class, 1, 30000);
		} catch (IOException e) {
			logger.error("获取区块错误。", e);
		}
        /** 将查询数据转成对应list */
        List<Object[]> rows = new ArrayList<>();
        blockList.getRsData().forEach(block->{
            Object[] row = {
                    block.getNum(),
                    DateUtil.timeZoneTransfer(block.getTime(), "0", timeZone),
                    block.getTxQty(),
                    EnergonUtil.format(Convert.fromVon(block.getReward(), Convert.Unit.LAT).setScale(18,RoundingMode.DOWN)),
                    EnergonUtil.format(Convert.fromVon(block.getTxFee(), Convert.Unit.LAT).setScale(18,RoundingMode.DOWN))
            };
            rows.add(row);
        });

        /** 初始化输出流对象 */
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Writer outputWriter = new OutputStreamWriter(baos, Charset.defaultCharset());
        try {
        	/** 设置返回的头，防止csv乱码 */
			outputWriter.write(new String(new byte[] { (byte) 0xEF, (byte) 0xBB,(byte) 0xBF }));
		} catch (IOException e) {
        	logger.error("数据输出错误:",e);
		}
        /** 厨师书writer对象 */
        CsvWriter writer = new CsvWriter(outputWriter, new CsvWriterSettings());
        writer.writeHeaders(
                i18n.i(I18nEnum.DOWNLOAD_BLOCK_CSV_NUMBER, local),
                i18n.i(I18nEnum.DOWNLOAD_BLOCK_CSV_TIMESTAMP, local),
                i18n.i(I18nEnum.DOWNLOAD_BLOCK_CSV_TRANSACTION_COUNT, local),
                i18n.i(I18nEnum.DOWNLOAD_BLOCK_CSV_REWARD, local),
                i18n.i(I18nEnum.DOWNLOAD_BLOCK_CSV_TXN_FEE, local)
        );
        writer.writeRowsAndClose(rows);
        /** 设置下载返回对象 */
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
		long blockNumber = req.getNumber();
		/** 区分是否查询上一个块还是下一个块 */
		NavigateEnum navigateEnum = NavigateEnum.valueOf(req.getDirection().toUpperCase());
		if (navigateEnum == NavigateEnum.PREV) {
			blockNumber -= 1;
		} else if (navigateEnum == NavigateEnum.NEXT) {
			blockNumber += 1;
		}
		return this.queryBlockByNumber(blockNumber);
	}

	private BlockDetailResp queryBlockByNumber(long blockNumber) {
		/** 根据区块号查询对应数据 */
		
		ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
		constructor.must(new ESQueryBuilders().term("num", blockNumber));
		ESResult<Block> blockList = new ESResult<>();
		try {
			blockList = blockESRepository.search(constructor, Block.class, 1, 1);
		} catch (IOException e) {
			logger.error("获取区块错误。", e);
		}
		BlockDetailResp blockDetailResp = new BlockDetailResp();
		if (blockList.getRsData() != null && blockList.getRsData().size() > 0) {
			Block block = blockList.getRsData().get(0);
			BeanUtils.copyProperties(block, blockDetailResp);
			blockDetailResp.setBlockReward(block.getReward().toString());
			blockDetailResp.setDelegateQty(block.getDQty());
			blockDetailResp.setExtraData(block.getExtra());
			blockDetailResp.setNumber(block.getNum());
			blockDetailResp.setParentHash(block.getPHash());
			blockDetailResp.setProposalQty(block.getPQty());
			blockDetailResp.setStakingQty(block.getSQty());
			blockDetailResp.setStatTxGasLimit(block.getTxGasLimit().toString());
			blockDetailResp.setTimestamp(block.getTime().getTime());
			blockDetailResp.setServerTime(new Date().getTime());
			blockDetailResp.setTransferQty(block.getTranQty());
			blockDetailResp.setGasLimit(block.getGasLimit().toString());
			blockDetailResp.setGasUsed(block.getGasUsed().toString());
			

			/** 取上一个区块,如果存在则设置标识和hash */
	        blockDetailResp.setFirst(false);
	        if(blockNumber == 1) {
	        	blockDetailResp.setTimeDiff(0l);
	        	blockDetailResp.setFirst(true);
	        } 

	        /** 设置last标识 **/
	        /** 查询现阶段最大区块数 */
	        blockDetailResp.setLast(false);
			NetworkStat networkStatRedis = statisticCacheService.getNetworkStatCache();
			Long bNumber = networkStatRedis.getCurNumber();
			if(blockNumber >= bNumber) {
				/** 当前区块没有下一个块，则表示这是最后一个块，设置last标识   */
				blockDetailResp.setLast(true);
			}
			
			blockDetailResp.setTimestamp(block.getTime().getTime());
		}
		return blockDetailResp;
	}

}
