package com.platon.browser.service;

import com.platon.contracts.ppos.dto.resp.Node;
import com.platon.crypto.Credentials;
import com.platon.protocol.core.DefaultBlockParameter;
import com.platon.protocol.core.methods.response.PlatonBlock;
import com.platon.tx.gas.ContractGasProvider;
import com.platon.browser.client.SpecialApi;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.evm.bean.CollectionBlock;
import com.platon.browser.utils.HexUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
	
	@Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
	public void exportData() {
		try {
			List<Object[]> csvNodeRows = new ArrayList<>();
			Object[] nodeRowHead = new Object[2];
			nodeRowHead[0] = "开始区块";
			nodeRowHead[1] = "节点列表";
			csvNodeRows.add(nodeRowHead);
			
			
			List<Object[]> csvZeroRows = new ArrayList<>();
			Object[] zeroRowHead = new Object[3];
			zeroRowHead[0] = "零出块节点id";
			zeroRowHead[1] = "节点名称";
			zeroRowHead[2] = "开始区块";
			csvZeroRows.add(zeroRowHead);
			long begin = 0l;
			begin = beginBlock;
			long end = 0l;
			end = endBlock;
			
			Map<String, NodeData> nodeMap = new HashMap<>();
			Map<String, Integer> nodeZeroMap = new HashMap<>();
			List<Node> nodes = null;
			Map<String, String> nodeNameMap = new HashMap<>();
			for(;begin < end;) {
				log.info("beginNum:"+ begin);
				PlatonBlock platonBlock = getClient().platonGetBlockByNumber(DefaultBlockParameter.valueOf(BigInteger.valueOf(begin)),true).send();
				CollectionBlock block = CollectionBlock.newInstance().updateWithRawBlockAndReceiptResult(platonBlock.getBlock());
				
				if((begin-1)%chainConfig.getConsensusPeriodBlockCount().longValue()==0) {
					if(nodes!=null) {
						for(Node node:nodes) {
							if(!nodeZeroMap.containsKey(node.getNodeId())) {
								Object[] rowData = new Object[3];
								rowData[0] = node.getNodeId();
								rowData[1] = nodeNameMap.get(node.getNodeId());
								rowData[2] = begin - chainConfig.getConsensusPeriodBlockCount().longValue();
								csvZeroRows.add(rowData);
							}
						}
						nodeZeroMap.clear();
					}
					
					Object[] rowData = new Object[2];
					rowData[0] = begin-1;
					nodes = specialApi.getHistoryValidatorList(getClient(), BigInteger.valueOf(begin));
					StringBuilder sBuilder = new StringBuilder();
					for(Node node:nodes) {
						if(!nodeNameMap.containsKey(HexUtil.prefix(node.getNodeId()))) {
							com.platon.browser.dao.entity.Node node2 = nodeMapper.selectByPrimaryKey(HexUtil.prefix(node.getNodeId()));
							if (node2 != null)
							nodeNameMap.put(HexUtil.prefix(node.getNodeId()), node2.getNodeName());
						}
						
						node.setNodeId(HexUtil.prefix(node.getNodeId()));
						sBuilder.append(node.getNodeId()).append("(").append(nodeNameMap.get(HexUtil.prefix(node.getNodeId())))
							.append(")").append(";");
						if(nodeMap.containsKey(node.getNodeId())) {
							NodeData nodeData = nodeMap.get(node.getNodeId());
							nodeData.setExpectBlock(nodeData.getExpectBlock() + 10);
							nodeMap.put(block.getNodeId(),nodeData);
						} else {
							NodeData nodeData = new NodeData();
							nodeData.setExpectBlock(10);
							nodeData.setNodeId(node.getNodeId());
							nodeData.setStartNum(begin);
							nodeData.setBlock(0);
							nodeMap.put(node.getNodeId(), nodeData);
						}
					}
					rowData[1] = sBuilder.toString();
					csvNodeRows.add(rowData);
					log.info("beginNum:{},data:{}", begin,rowData[1]);
				}
				if(nodeMap.containsKey(block.getNodeId())) {
					NodeData nodeData = nodeMap.get(block.getNodeId());
					nodeData.setBlock(nodeData.getBlock()+1);
					nodeMap.put(block.getNodeId(),nodeData);
					if(nodeZeroMap.containsKey(block.getNodeId())) {
						Integer data = nodeZeroMap.get(block.getNodeId());
						nodeZeroMap.put(block.getNodeId(), data+1);
					} else {
						nodeZeroMap.put(block.getNodeId(), 1);
					}
				}
				
				begin++;
			}
			
			buildFile("exportValidatorNodeData.csv", csvNodeRows, null);
			log.info("exportValidatorNodeData数据导出成功,总行数：{}", csvNodeRows.size());
			

			buildFile("exportZeroNodeData.csv", csvZeroRows, null);
			log.info("exportZeroNodeData数据导出成功,总行数：{}", csvZeroRows.size());
			
			List<Object[]> csvBlockRateRows = new ArrayList<>();
			Object[] blockRateRowHead = new Object[6];
			blockRateRowHead[0] = "节点id";
			blockRateRowHead[1] = "节点名称";
			blockRateRowHead[2] = "已出区块";
			blockRateRowHead[3] = "预计出块";
			blockRateRowHead[4] = "出块率";
			blockRateRowHead[5] = "开始出块";
			csvBlockRateRows.add(blockRateRowHead);
			for(Entry<String, NodeData> node: nodeMap.entrySet()) {
				Object[] rowData = new Object[6];
				rowData[0] = node.getKey();
				rowData[1] = nodeNameMap.get(node.getKey());
				rowData[2] = node.getValue().getBlock();
				rowData[3] = node.getValue().getExpectBlock();
				rowData[4] = BigDecimal.valueOf(node.getValue().getBlock()).divide(
						BigDecimal.valueOf(node.getValue().getExpectBlock()), 2, RoundingMode.HALF_UP);;
				rowData[5] = node.getValue().getStartNum();
				csvBlockRateRows.add(rowData);
			}

			buildFile("exportBlockRateNodeData.csv", csvBlockRateRows, null);
			log.info("exportBlockRateNodeData数据导出成功,总行数：{}", csvBlockRateRows.size());
			txInfoExportDone = true;
		} catch (Exception e) {
			log.error("system error", e);
			System.exit(0);
		}

	}
	
	
}

@Data
class NodeData {
	private String nodeId;
	
	private Integer expectBlock;
	
	private Integer block;
	
	private Long startNum;
}