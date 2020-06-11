package com.platon.browser.service;

import com.platon.browser.client.ReceiptResult;
import com.platon.browser.client.SpecialApi;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.evm.bean.CollectionBlock;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.PlatonBlock;
import org.web3j.protocol.core.methods.response.PlatonGetTransactionCount;
import org.web3j.tx.gas.ContractGasProvider;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
@Data
@Service
public class ExportTpsService extends ServiceBase {
	private volatile boolean txInfoExportDone = false;
	@Autowired
    private NodeMapper nodeMapper;
	@Autowired
    private SpecialApi specialApi;
	protected static final BigInteger GAS_LIMIT = BigInteger.valueOf(470000);
	protected static final BigInteger GAS_PRICE = BigInteger.valueOf(10000000000L);
	protected Credentials adminCredentials = Credentials.create("009614c2b32f2d5d3421591ab3ffc03ac66c831fb6807b532f6e3a8e7aac31f1d9");
	
    protected ContractGasProvider provider = new ContractGasProvider(GAS_PRICE, GAS_LIMIT);;

	@Value("${filepath}")
	private String filepath;

	@Value("${fileUrl}")
	private String fileUrl;
	@Value("${evmContract}")
	private String evmContract;
	@Value("${wasmContract}")
	private String wasmContract;
	@Value("${beginBlock}")
	private long beginBlock;
	@Value("${endBlock}")
	private long endBlock;
	private Long startTime = 0l;
	@Override
	public String getFileUrl() {
		return fileUrl;
	}
	
	private BlockingQueue<CollectionBlock> queue=new LinkedBlockingQueue<>();
	
	@Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
	public void exportTpsData() {
		try {
			ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(2);
			EXECUTOR_SERVICE.submit(() -> this.explainBlock());
			long begin = 0l;
			begin = beginBlock;
			long end = 0l;
			end = endBlock;
			for(;begin < end;) {
				PlatonBlock platonBlock = getClient().platonGetBlockByNumber(DefaultBlockParameter.valueOf(BigInteger.valueOf(begin)),true).send();
//				ReceiptResult receiptResult = specialApi.getReceiptResult(getClientWrapper(),BigInteger.valueOf(beginBlock));
				CollectionBlock block = CollectionBlock.newInstance().updateWithRawBlockAndReceiptResult(platonBlock.getBlock());
				queue.put(block);
				log.info("input num:{}", block.getNum());
				begin++;
			}
			CollectionBlock lastBlock = CollectionBlock.newInstance();
			lastBlock.setNum(10000000000l);
			queue.put(lastBlock);
		} catch (Exception e) {
			log.error("system error", e);
			System.exit(0);
		}

	}
	
	int m = 1;
	public void explainBlock() {
		try {
			Map<Long,List<Block>> cacheMap = new HashMap<>();
			
			List<Object[]> csvRows = new ArrayList<>();
			Object[] rowHead = new Object[4];
			rowHead[0] = "时间区间";
			rowHead[1] = "区块区间";
			rowHead[2] = "交易笔数";
			rowHead[3] = "tps";
			csvRows.add(rowHead);
			while (true) {
				CollectionBlock block = queue.take();
				long blockNum = block.getNum();
				log.info("start,startTime:{},txCount:{},blockNUM:{}", startTime,block.getTxQty(),blockNum);
				if(blockNum == 10000000000l) break;
				BigDecimal seconds = BigDecimal.valueOf(block.getTime().getTime()).divide(BigDecimal.valueOf(1000),0, RoundingMode.CEILING);
				Long now = seconds.longValue();
				if (blockNum == beginBlock) {
					List<Block> blocks = new ArrayList<Block>();
					blocks.add(block);
					cacheMap.put(now, blocks);
					startTime = now;
					continue;
				}
				List<Block> blocks = cacheMap.get(startTime);
				if(blocks == null) {
					blocks = new ArrayList<Block>();
				}
				if(now <= startTime + m) {
					blocks.add(block);
				} else {
					while (now > startTime + m) {
						this.setData( blocks, cacheMap, block, csvRows);
					}
					blocks.add(block);
					cacheMap.put(startTime, blocks);
				}
			}
			buildFile("exportTpsData.csv", csvRows, null);
			log.info("exportTpsData数据导出成功,总行数：{}", csvRows.size());
			txInfoExportDone = true;
		} catch (Exception e) {
			log.error("system error", e);
			System.exit(0);
		}
	}
	
	public void setData(List<Block> blocks, Map<Long,List<Block>> cacheMap,Block block,List<Object[]> csvRows) {
		Object[] rowData = new Object[4]; 
		rowData[0] = startTime;
		rowData[1] = "";
		rowData[2] = 0;
		rowData[3] = 0;
		int txCount=0;
		for(Block b:blocks) {
			txCount += b.getTxQty();
			rowData[1] = rowData[1] + "," + b.getNum() ;
		}
		rowData[2] = txCount;
		int tps = BigDecimal.valueOf(txCount).divide(BigDecimal.ONE,0,RoundingMode.CEILING).intValue();
		rowData[3] = tps;
		
		startTime = startTime+m;
		blocks.clear();
		cacheMap.clear();
		cacheMap.put(startTime, blocks);
		log.info("startTime:{},txCount:{}", startTime,txCount);
		csvRows.add(rowData);
	}
	
	@Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
	public void exportMaxData() {
		try {
			List<Object[]> csvRows = new ArrayList<>();
			Object[] rowHead = new Object[3];
			rowHead[0] = "时间区间";
			rowHead[1] = "区块区间";
			rowHead[2] = "交易笔数";
			csvRows.add(rowHead);
			long begin = 0l;
			begin = beginBlock;
			long end = 0l;
			end = endBlock;
			long num = 0l;
			for(;begin < end;) {
				PlatonBlock platonBlock = getClient().platonGetBlockByNumber(DefaultBlockParameter.valueOf(BigInteger.valueOf(begin)),true).send();
				CollectionBlock block = CollectionBlock.newInstance().updateWithRawBlockAndReceiptResult(platonBlock.getBlock());
				log.info("input num:{}", block.getNum());
				begin++;
				if(block.getTxQty() > 1800 || num == block.getNum()) {
					Object[] rowData = new Object[3];
					rowData[0] = block.getTime().getTime();
					rowData[1] = block.getNum();
					rowData[2] = block.getTxQty();
					csvRows.add(rowData);
					num = block.getNum()+1;
				}
			}
			buildFile("exportMaxData.csv", csvRows, null);
			log.info("exportMaxData数据导出成功,总行数：{}", csvRows.size());
			txInfoExportDone = true;
		} catch (Exception e) {
			log.error("system error", e);
			System.exit(0);
		}

	}
}
