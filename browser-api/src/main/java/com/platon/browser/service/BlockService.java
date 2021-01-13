package com.platon.browser.service;

import com.alaya.utils.Convert;
import com.github.pagehelper.Page;
import com.platon.browser.constant.Browser;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.service.elasticsearch.EsBlockRepository;
import com.platon.browser.service.elasticsearch.bean.ESResult;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.service.elasticsearch.query.ESQueryBuilderConstructor;
import com.platon.browser.service.elasticsearch.query.ESQueryBuilders;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.NavigateEnum;
import com.platon.browser.request.PageReq;
import com.platon.browser.request.newblock.BlockDetailNavigateReq;
import com.platon.browser.request.newblock.BlockDetailsReq;
import com.platon.browser.request.newblock.BlockDownload;
import com.platon.browser.request.newblock.BlockListByNodeIdReq;
import com.platon.browser.response.RespPage;
import com.platon.browser.response.block.BlockDetailResp;
import com.platon.browser.response.block.BlockListResp;
import com.platon.browser.utils.DateUtil;
import com.platon.browser.utils.EnergonUtil;
import com.platon.browser.utils.I18nUtil;
import com.platon.browser.utils.HexUtil;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *  区块方法逻辑具体实现
 *  @file BlockServiceImpl.java
 *  @description
 *	@author zhangrj
 *  @data 2019年8月31日
 */
@Service
public class BlockService {

	private final Logger logger = LoggerFactory.getLogger(BlockService.class);

	@Resource
	private StatisticCacheService statisticCacheService;
	@Resource
	private EsBlockRepository ESBlockRepository;
	@Resource
	private I18nUtil i18n;
	@Resource
	private CommonService commonService;

	private Lock lock = new ReentrantLock();

	private static final String ERROR_TIPS = "获取区块错误。";

	public RespPage<BlockListResp> blockList(PageReq req) {
		long startTime = System.currentTimeMillis();
		RespPage<BlockListResp> respPage = new RespPage<>();
		List<BlockListResp> lists = new ArrayList<>();
		/** 查询现阶段最大区块数 */
		NetworkStat networkStatRedis = statisticCacheService.getNetworkStatCache();
		Long bNumber = networkStatRedis.getCurNumber();
		/** 小于50万条查询redis */
		if (req.getPageNo() * req.getPageSize() < Browser.MAX_NUM) {
			/**
			 * 当页号等于1，重新获取数据，与首页保持一致
			 */
			List<Block> items;
			if(req.getPageNo() == 1) {
				/** 查询缓存最新的八条区块信息 */
				items = statisticCacheService.getBlockCache(0,1);
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
			} else {
				items = statisticCacheService.getBlockCache(req.getPageNo(), req.getPageSize());
			}
			lists.addAll(this.transferBlockListResp(items));
		} else {
			/** 查询超过五十万条数据，根据区块号倒序 */
			ESResult<Block> blocks = new ESResult<>();
			ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
			constructor.setDesc("num");
			constructor.setResult(new String[] { "num", "time", "txQty", "reward",
					"nodeName", "nodeId", "gasUsed", "txGasLimit", "size"});
			try {
				lock.lock();
				blocks = ESBlockRepository.search(constructor, Block.class, req.getPageNo(), req.getPageSize());
			} catch (IOException e) {
				logger.error(ERROR_TIPS, e);
			} finally {
				lock.unlock();
			}
			lists.addAll(this.transferBlockListResp(blocks.getRsData()));
			
		}
		Page<?> page = new Page<>(req.getPageNo(), req.getPageSize());
		page.setTotal(networkStatRedis.getCurNumber());
		respPage.init(page, lists);
		if(System.currentTimeMillis() - startTime > 100) {
			logger.error("perform-blockList,time6:{}", System.currentTimeMillis() - startTime);
		}
		return respPage;
	}
	
	/**
	 * 区块列表统一转换
	 * @method transferBlockListResp
	 * @return
	 */
	private List<BlockListResp> transferBlockListResp(List<Block> blocks) {
		List<BlockListResp> blockListResps = new ArrayList<>();
		/**
		 * 申明节点列表，减少整体查询次数
		 */
		Map<String, String> nodes = new HashMap<>();
		for(Block block : blocks) {
			BlockListResp blockListResp = new BlockListResp();
			BeanUtils.copyProperties(block, blockListResp);
			blockListResp.setBlockReward(new BigDecimal(block.getReward()));
			blockListResp.setNumber(block.getNum());
			blockListResp.setStatTxGasLimit(block.getTxGasLimit());
			blockListResp.setStatTxQty(block.getTxQty());
			blockListResp.setServerTime(new Date().getTime());
			blockListResp.setTimestamp(block.getTime().getTime());
			String nodeName = nodes.get(block.getNodeId());
			/**
			 * 判断节点名称是否需要重复查询
			 */
			if(StringUtils.isNotBlank(nodeName)) {
				blockListResp.setNodeName(nodeName);
			} else {
				blockListResp.setNodeName(commonService.getNodeName(block.getNodeId(), null));
				nodes.put(block.getNodeId(), blockListResp.getNodeName());
			}
			blockListResps.add(blockListResp);
		}
		nodes.clear();
		return blockListResps;
	}

	public RespPage<BlockListResp> blockListByNodeId(BlockListByNodeIdReq req) {
		RespPage<BlockListResp> respPage = new RespPage<>();
		/** 根据nodeId 查询区块列表，以区块号倒序  */
		ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
		constructor.must(new ESQueryBuilders().term("nodeId", req.getNodeId()));
		constructor.setDesc("num");
		constructor.setResult(new String[] { "num", "time", "txQty", "reward"});
		ESResult<Block> blocks = new ESResult<>();
		try {
			blocks = ESBlockRepository.search(constructor, Block.class, req.getPageNo(), req.getPageSize());
		} catch (Exception e) {
			logger.error(ERROR_TIPS, e);
			return respPage;
		}
		/** 初始化返回对象 */
		List<BlockListResp> lists = new ArrayList<>();
		for(Block block : blocks.getRsData()) {
			BlockListResp blockListResp = new BlockListResp();
			blockListResp.setBlockReward(new BigDecimal(block.getReward()));
			blockListResp.setNumber(block.getNum());
			blockListResp.setStatTxQty(block.getTxQty());
			blockListResp.setServerTime(new Date().getTime());
			blockListResp.setTimestamp(block.getTime().getTime());
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

	public BlockDownload blockListByNodeIdDownload(String nodeId, Long date, String local, String timeZone) {
		/** 设置下载返回对象 */
        BlockDownload blockDownload = new BlockDownload();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date now = new Date();
		SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		logger.error("now time:{}",format.format(now));
		String msg = dateFormat.format(now);
		String msg2 = dateFormat.format(new Date(date));
        logger.info("导出数据起始日期：{},结束时间：{}",msg2,msg);
        /** 限制最多导出3万条记录 */
        /** 设置根据时间和nodeId查询数据 */
		
		ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
		constructor.must(new ESQueryBuilders().term("nodeId", nodeId));
		constructor.must(new ESQueryBuilders().range("time", new Date(date).getTime(), now.getTime()));
		constructor.setDesc("num");
		constructor.setResult(new String[] { "num", "time", "txQty", "reward",
				"txFee"});
		ESResult<Block> blockList = new ESResult<>();
		try {
			blockList = ESBlockRepository.search(constructor, Block.class, 1, 30000);
		} catch (Exception e) {
			logger.error(ERROR_TIPS, e);
			return blockDownload;
		}
        /** 将查询数据转成对应list */
        List<Object[]> rows = new ArrayList<>();
        blockList.getRsData().forEach(block->{
            Object[] row = {
                    block.getNum(),
                    DateUtil.timeZoneTransfer(block.getTime(), "0", timeZone),
                    block.getTxQty(),
                    HexUtil.append(EnergonUtil.format(Convert.fromVon(block.getReward(), Convert.Unit.ATP).setScale(18,RoundingMode.DOWN))),
                    HexUtil.append(EnergonUtil.format(Convert.fromVon(block.getTxFee(), Convert.Unit.ATP).setScale(18,RoundingMode.DOWN)))
            };
            rows.add(row);
        });

        /** 初始化输出流对象 */
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
        	/** 设置返回的头，防止csv乱码 */
        	baos.write(new byte[] { (byte) 0xEF, (byte) 0xBB,(byte) 0xBF });
		} catch (IOException e) {
        	logger.error("数据输出错误:",e);
		}
        Writer outputWriter = new OutputStreamWriter(baos, StandardCharsets.UTF_8);
        /** 初始化writer对象 */
        CsvWriter writer = new CsvWriter(outputWriter, new CsvWriterSettings());
        writer.writeHeaders(
                i18n.i(I18nEnum.DOWNLOAD_BLOCK_CSV_NUMBER, local),
                i18n.i(I18nEnum.DOWNLOAD_BLOCK_CSV_TIMESTAMP, local),
                i18n.i(I18nEnum.DOWNLOAD_BLOCK_CSV_TRANSACTION_COUNT, local),
                i18n.i(I18nEnum.DOWNLOAD_BLOCK_CSV_REWARD, local),
                i18n.i(I18nEnum.DOWNLOAD_BLOCK_CSV_TXN_FEE, local)
        );
        writer.writeRowsAndClose(rows);
        
        blockDownload.setData(baos.toByteArray());
        blockDownload.setFilename("block-"+nodeId+"-"+date+".csv");
        blockDownload.setLength(baos.size());
        return blockDownload;
	}

	public BlockDetailResp blockDetails(BlockDetailsReq req) {
		return this.queryBlockByNumber(req.getNumber().longValue());
	}

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
		
		Block block = null;
		try {
			block = ESBlockRepository.get(String.valueOf(blockNumber), Block.class);
		} catch (IOException e) {
			logger.error(ERROR_TIPS, e);
		}
		BlockDetailResp blockDetailResp = new BlockDetailResp();
		if (block != null) {
			BeanUtils.copyProperties(block, blockDetailResp);
			blockDetailResp.setBlockReward(new BigDecimal(block.getReward()));
			blockDetailResp.setDelegateQty(block.getDQty());
			blockDetailResp.setExtraData(block.getExtra());
			blockDetailResp.setNumber(block.getNum());
			blockDetailResp.setParentHash(block.getPHash());
			blockDetailResp.setProposalQty(block.getPQty());
			blockDetailResp.setStakingQty(block.getSQty());
			blockDetailResp.setStatTxGasLimit(block.getTxGasLimit());
			blockDetailResp.setTimestamp(block.getTime().getTime());
			blockDetailResp.setServerTime(new Date().getTime());
			blockDetailResp.setTransferQty(block.getTranQty());
			blockDetailResp.setNodeName(commonService.getNodeName(block.getNodeId(), null));

			/** 取上一个区块,如果存在则设置标识和hash */
	        blockDetailResp.setFirst(false);
	        if(blockNumber == 0) {
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
