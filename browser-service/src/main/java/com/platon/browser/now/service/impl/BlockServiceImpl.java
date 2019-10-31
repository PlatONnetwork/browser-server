package com.platon.browser.now.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.platon.browser.common.BrowserConst;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.BlockExample;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.NavigateEnum;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.now.service.BlockService;
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
	private BlockMapper blockMapper;

	@Autowired
	private I18nUtil i18n;

	@Override
	public RespPage<BlockListResp> blockList(PageReq req) {
		RespPage<BlockListResp> respPage = new RespPage<>();
		List<BlockListResp> lists = new LinkedList<>();
		/** 查询现阶段最大区块数 */
		NetworkStat networkStatRedis = statisticCacheService.getNetworkStatCache();
		Long bNumber = networkStatRedis.getCurNumber();
		/** 小于50万条查询redis */
		if (req.getPageNo().intValue() * req.getPageSize().intValue() < BrowserConst.MAX_NUM) {
			/**
			 * 当页号等于1，重新获取数据，与首页保持一致
			 */
			List<Block> items;
			if(req.getPageNo().intValue() == 1) {
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
			for (Block blockRedis:items) {
				BlockListResp blockListNewResp = new BlockListResp();
				BeanUtils.copyProperties(blockRedis, blockListNewResp);
				blockListNewResp.setServerTime(new Date().getTime());
				blockListNewResp.setTimestamp(blockRedis.getTime().getTime());
				lists.add(blockListNewResp);
			}
		} else {
			/** 查询超过五十万条数据，根据区块号倒序 */
			BlockExample blockExample = new BlockExample();
			blockExample.setOrderByClause(" number desc");
			PageHelper.startPage(req.getPageNo(), req.getPageSize());
			List<Block> blocks = blockMapper.selectByExample(blockExample);
			for(Block block:blocks) {
				BlockListResp blockListNewResp = new BlockListResp();
				BeanUtils.copyProperties(block, blockListNewResp);
				blockListNewResp.setServerTime(new Date().getTime());
				blockListNewResp.setTimestamp(block.getTime().getTime());
				lists.add(blockListNewResp);
			}
		}
		Page<?> page = new Page<>(req.getPageNo(), req.getPageSize());
		page.setTotal(bNumber);
		respPage.init(page, lists);
		return respPage;
	}

	@Override
	public RespPage<BlockListResp> blockListByNodeId(BlockListByNodeIdReq req) {
		/** 更具nodeId 查询区块列表，以区块号倒序  */
		BlockExample blockExample = new BlockExample();
		blockExample.setOrderByClause("number desc ");
		BlockExample.Criteria criteria = blockExample.createCriteria();
		criteria.andNodeIdEqualTo(req.getNodeId());
		PageHelper.startPage(req.getPageNo(), req.getPageSize());
		Page<Block> blockPage = blockMapper.selectByExample(blockExample);
		/** 初始化返回对象 */
		List<Block> blocks = blockPage.getResult();
		RespPage<BlockListResp> respPage = new RespPage<>();
		List<BlockListResp> lists = new LinkedList<>();
		for (Block block : blocks) {
			BlockListResp blockListResp = new BlockListResp();
			BeanUtils.copyProperties(block, blockListResp);
			blockListResp.setServerTime(new Date().getTime());
			blockListResp.setTimestamp(block.getTime().getTime());
			lists.add(blockListResp);
		}
		/** 设置返回的分页数据 */
		Page<?> page = new Page<>(req.getPageNo(), req.getPageSize());
		if(blockPage.getTotal() > 5000) {
			page.setTotal(5000);
		} else {
			page.setTotal(blockPage.getTotal());
		}
		respPage.init(page, lists);
		return respPage;
	}

	@Override
	public BlockDownload blockListByNodeIdDownload(String nodeId, Long date, String local, String timeZone) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date now = new Date();
		String msg = dateFormat.format(now);
        logger.info("导出数据起始日期：{},结束时间：{}",date,msg);
        /** 限制最多导出3万条记录 */
        PageHelper.startPage(1,30000);
        /** 设置根据时间和nodeId查询数据 */
        BlockExample blockExample = new BlockExample();
        blockExample.setOrderByClause("number desc ");
        BlockExample.Criteria criteria = blockExample.createCriteria();
        criteria.andNodeIdEqualTo(nodeId);
        criteria.andCreTimeBetween(new Date(date), now);
        List<Block> blockList = blockMapper.selectByExample(blockExample);
        /** 将查询数据转成对应list */
        List<Object[]> rows = new ArrayList<>();
        blockList.forEach(block->{
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
		Block block = blockMapper.selectByPrimaryKey(blockNumber);
		BlockDetailResp blockDetailResp = new BlockDetailResp();
		if (block != null) {
			BeanUtils.copyProperties(block, blockDetailResp);
			blockDetailResp.setTxQty(block.getTxQty());
			blockDetailResp.setTransferQty(block.getTranQty());
			blockDetailResp.setDelegateQty(block.getdQty());
			blockDetailResp.setStakingQty(block.getsQty());
			blockDetailResp.setProposalQty(block.getpQty());

			blockDetailResp.setTimestamp(block.getTime().getTime());
			blockDetailResp.setServerTime(new Date().getTime());

			/** 取上一个区块,如果存在则设置标识和hash */
			BlockExample blockExample = new BlockExample();
	        blockExample.createCriteria().andNumEqualTo(blockNumber - 1);
	        List<Block> blocks = blockMapper.selectByExample(blockExample);
	        if (blocks.size()>1){
	            logger.error("duplicate block: block number {}",blockNumber);
	            throw new BusinessException(RetEnum.RET_FAIL.getCode(), i18n.i(I18nEnum.BLOCK_ERROR_DUPLICATE));
	        }
	        if(blocks.isEmpty()){
	        	blockDetailResp.setTimeDiff(0l);
	            /** 当前块没有上一个块证明这是第一个块, 设置first标识  */
	            blockDetailResp.setFirst(true);
	        }else{
	            Block prevBlock = blocks.get(0);
	            blockDetailResp.setTimeDiff(blockDetailResp.getTimestamp()-prevBlock.getTime().getTime());
	        }

	        /** 设置last标识 **/
	        blockExample = new BlockExample();
	        blockExample.createCriteria().andNumEqualTo(blockNumber+1);
	        blocks = blockMapper.selectByExample(blockExample);
	        if(blocks.isEmpty()){
	            /** 当前区块没有下一个块，则表示这是最后一个块，设置last标识   */
	        	blockDetailResp.setLast(true);
	        }
			blockDetailResp.setTimestamp(block.getTime().getTime());
		}
		return blockDetailResp;
	}

}
