package com.platon.browser.service;

import com.alibaba.fastjson.JSON;
import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.entity.NodeExample;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.dto.elasticsearch.ESResult;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.elasticsearch.dto.Transaction.StatusEnum;
import com.platon.browser.elasticsearch.dto.Transaction.TypeEnum;
import com.platon.browser.elasticsearch.service.impl.ESQueryBuilderConstructor;
import com.platon.browser.elasticsearch.service.impl.ESQueryBuilders;
import com.platon.browser.param.*;
import com.platon.browser.util.DateUtil;
import com.platon.browser.util.EnergonUtil;
import com.platon.browser.util.SleepUtil;
import com.platon.browser.util.decode.innercontract.InnerContractDecodeUtil;
import com.platon.browser.util.decode.innercontract.InnerContractDecodedResult;
import com.platon.browser.utils.HexTool;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.PlatonGetTransactionCount;
import org.web3j.tx.Transfer;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;

import java.io.File;
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

@Slf4j
@Data
@Service
public class ExportGallyService extends ServiceBase {
	private volatile boolean txHashExportDone = false;
	private volatile boolean addressExportDone = false;
	private volatile boolean rpplanExportDone = false;
	private volatile boolean nodeExportDone = false;
	private volatile boolean delegationExportDone = false;
	private volatile boolean proposalExportDone = false;
	private volatile boolean voteExportDone = false;
	private volatile boolean delegationRewardExportDone = false;
	private volatile boolean txInfoExportDone = false;
	private volatile boolean stakingExportDone = false;
	private volatile boolean exportLegalTxDone = false;
	@Autowired
    private NodeMapper nodeMapper;
	protected static final BigInteger GAS_LIMIT = BigInteger.valueOf(470000);
	protected static final BigInteger GAS_PRICE = BigInteger.valueOf(10000000000L);
	protected Credentials adminCredentials = Credentials.create("009614c2b32f2d5d3421591ab3ffc03ac66c831fb6807b532f6e3a8e7aac31f1d9");
	
    protected ContractGasProvider provider = new ContractGasProvider(GAS_PRICE, GAS_LIMIT);;

//	@Value("${paging.maxCount}")
//	private int maxCount;
	@Value("${filepath}")
	private String filepath;
//	@Value("${addresspath}")
//	private String addresspath;

	@Value("${fileUrl}")
	private String fileUrl;
	@Override
	public String getFileUrl() {
		return fileUrl;
	}


	/**
	 * 导出交易表交易hash
	 */
	@Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
	public void exportAllTx() {
//		while (this.checkNumer().compareTo(BigInteger.ZERO) == 0) {
//			log.debug("wait block");
//		}

		List<Object[]> csvRows = new ArrayList<>();
		// 定义表头
		csvRows.add(new String[]{
				"tx hash",
				"tx block",
				"tx time",
				"tx type",
				"from",
				"to",
				"value",
				"tx fee cost",
				"tx amount",
				"tx reward",
				"tx info"
		});
		// 遍历交易数据
		ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
//		constructor.must(new ESQueryBuilders().range("num", eblock.subtract(blockChainConfig.getSettlePeriodBlockCount()).longValue(), eblock.longValue()));
		List<Object> types = new ArrayList<>();
		types.add("1");
		types.add("2");
		constructor.must(new ESQueryBuilders().terms("type", types));
//		rowHead[11] = eblock.subtract(blockChainConfig.getSettlePeriodBlockCount());
//		rowHead[12] = eblock;
		constructor.setAsc("seq");
		traverseTx(constructor, tx -> {
			BigDecimal txAmount = BigDecimal.ZERO;
			BigDecimal reward = BigDecimal.ZERO;
			switch (tx.getTypeEnum()) {
				/** 创建验证人 */
				case STAKE_CREATE:
					StakeCreateParam createValidatorParam = JSON.parseObject(tx.getInfo(), StakeCreateParam.class);
					txAmount = createValidatorParam.getAmount();
					break;
				/**
				 * 增加质押
				 */
				case STAKE_INCREASE:
					StakeIncreaseParam increaseStakingParam = JSON.parseObject(tx.getInfo(),
							StakeIncreaseParam.class);
					txAmount = increaseStakingParam.getAmount();
					break;
				/**
				 * 退出验证人
				 */
				case STAKE_EXIT:
					// nodeId + nodeName + applyAmount + redeemLocked + redeemStatus +
					// redeemUnLockedBlock
					StakeExitParam exitValidatorParam = JSON.parseObject(tx.getInfo(), StakeExitParam.class);
					txAmount = exitValidatorParam.getAmount();
					break;
				/**
				 * 委托
				 */
				case DELEGATE_CREATE:
					DelegateCreateParam delegateParam = JSON.parseObject(tx.getInfo(), DelegateCreateParam.class);
					txAmount = delegateParam.getAmount();
					break;
				/**
				 * 委托赎回
				 */
				case DELEGATE_EXIT:
					// nodeId + nodeName + applyAmount + redeemLocked + redeemStatus
					// 通过txHash关联un_delegation表
					DelegateExitParam unDelegateParam = JSON.parseObject(tx.getInfo(), DelegateExitParam.class);
					txAmount = unDelegateParam.getAmount();
					reward = unDelegateParam.getReward();
					break;
				/**
				 * 领取奖励
				 */
				case CLAIM_REWARDS:
					DelegateRewardClaimParam delegateRewardClaimParam = JSON.parseObject(tx.getInfo(),
							DelegateRewardClaimParam.class);
					for (com.platon.browser.param.claim.Reward rewardTemp : delegateRewardClaimParam
							.getRewardList()) {
						reward = reward.add(rewardTemp.getReward());
					}
					break;
				default:
					break;
			}
			if(reward == null) {
				reward = BigDecimal.ZERO;
			}
			Object[] row = { tx.getHash(), tx.getNum(), DateUtil.timeZoneTransfer(tx.getTime(), "0", "+8"),
					Transaction.TypeEnum.getEnum(tx.getType()).getDesc(), tx.getFrom(), tx.getTo(),
					/** 数值von转换成lat，并保留十八位精确度 */
					HexTool.append(EnergonUtil.format(
							Convert.fromVon(tx.getValue(), Convert.Unit.LAT).setScale(18, RoundingMode.DOWN),
							18)),
					HexTool.append(EnergonUtil.format(
							Convert.fromVon(tx.getCost(), Convert.Unit.LAT).setScale(18, RoundingMode.DOWN),
							18)),
					HexTool.append(EnergonUtil.format(
							Convert.fromVon(txAmount, Convert.Unit.LAT).setScale(18, RoundingMode.DOWN), 18)),
					HexTool.append(EnergonUtil.format(
							Convert.fromVon(reward, Convert.Unit.LAT).setScale(18, RoundingMode.DOWN), 18)),
					tx.getInfo(), };
			csvRows.add(row);
		});
		buildFile("tcontractxhash.csv", csvRows, null);
		log.info("交易数据导出成功,总行数：{}", csvRows.size());
		txInfoExportDone = true;
	}


	@Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
	public void exportMatch() {
		List<Object[]> csvRows = new ArrayList<>();
		// 定义表头
		csvRows.add(new String[]{
			"address",
			"balance",
			"tx",
			"nodeId"
		});
		// 读取文件内容
		Set<String> lines = readLines(filepath);
		int i = 0;
		for(String address: lines) {
			Object[] rowData = new Object[4];
			rowData[0] = address;
			rowData[1] = getBalance(address,DefaultBlockParameterName.LATEST);
			ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
			constructor.buildMust(new BoolQueryBuilder().should(QueryBuilders.termQuery("from", address))
					.should(QueryBuilders.termQuery("to", address)));
			// 遍历交易数据
			traverseTx(constructor,tx -> rowData[2] = rowData[2]+";" + tx.getHash());
			
			NodeExample nodeExample = new NodeExample();
			NodeExample.Criteria  criteria = nodeExample.createCriteria();
			criteria.andBenefitAddrEqualTo(address.toLowerCase());
			List<Node> nodes = nodeMapper.selectByExample(nodeExample);
			rowData[3] = nodes.size();
			nodes.forEach(node->{
				rowData[3] = rowData[3]+";" + node.getNodeId();
			});
			
			NodeExample nodeExample1 = new NodeExample();
			NodeExample.Criteria  criteria1 = nodeExample1.createCriteria();
			criteria1.andStakingAddrEqualTo(address.toLowerCase());
			nodes = nodeMapper.selectByExample(nodeExample1);
			rowData[3] = rowData[3]+ ";" + nodes.size();
			nodes.forEach(node->{
				rowData[3] = rowData[3]+";" + node.getNodeId();
			});
			csvRows.add(rowData);
			log.info("exportMatch数据,address：{},i:{}",address,i);
			i++;
		} 
		
		buildFile("exportMatch.csv", csvRows, null);
		log.info("exportMatch数据导出成功,总行数：{}", csvRows.size());
		txInfoExportDone = true;
	}
	
	
//	@Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
//	public void transfer() {
//		Set<String> lines = readLines(filepath);
//		
//		int i = 0;
//		try {
////			for(String address: list) {
////				PlatonGetBalance platonGetBalance =platonClient.getWeb3jWrapper().getWeb3j().platonGetBalance(address, DefaultBlockParameterName.LATEST).send();
////				if(platonGetBalance.getBalance().compareTo(BigInteger.valueOf(0)) != 0) {
////					log.error("add:{}",address);
////				}
////			}
//			Credentials credentials = WalletUtils.loadCredentials("88888888", addresspath);
//			BigInteger balance = getBalance("0xceca295e1471b3008d20b017c7df7d4f338a7fba",DefaultBlockParameterName.LATEST);
//			log.error("platonGetBalance:{}",balance);
//			for(String address: lines) {
//					Transfer.sendFunds(
//							getClient(),
//					        credentials,
//					        "101",
//					        address,
//					        BigDecimal.valueOf(1),
//					        Unit.LAT
//					).send();
//					
//				
////					TransactionManager transactionManager = new RawTransactionManager(platonClient.getWeb3jWrapper().getWeb3j()
////							, credentials,101l);
////					PlatonSendTransaction ethSendTransaction = transactionManager.sendTransaction(GAS_PRICE, GAS_LIMIT, address, "", new BigInteger("1000000000000000000000"));
//					SleepUtil.sleep(5);
////					log.error("transfer数据,address：{},i:{},status:{}",address,i,ethSendTransaction.getResult());
//					i++;
//			} 
//		} catch ( Exception e1) {
//			log.error("transerr", e1);
//		}
//		log.info("转账成功,总行数：{}",i);
//		txInfoExportDone = true;
//	}
	
	@Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
	public void exportMatchNode() {
		Set<String> list = readLines(filepath);
		List<Object[]> csvRows = new ArrayList<>();
		try {
			Object[] rowHead = new Object[6];
			rowHead[0] = "node";
			rowHead[1] = "hash";
			rowHead[2] = "txInfo";
			rowHead[3] = "benefitAddress";
			rowHead[4] = "isBenefit(1:false 0:true)";
			rowHead[5] = "type";
			csvRows.add(rowHead);
			ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
			List<Object> typeList = new ArrayList<Object>();
			typeList.add(TypeEnum.STAKE_CREATE.getCode());
			typeList.add(TypeEnum.STAKE_MODIFY.getCode());
			constructor.must(new ESQueryBuilders().terms("type", typeList));
			constructor.must(new ESQueryBuilders().range("time", 1587348000000l, 1587646800000l));
			
			
			traverseTx(constructor, tx->{
				Object[] rowData = new Object[6];
				InnerContractDecodedResult innerContractDecodedResult = InnerContractDecodeUtil.decode(tx.getInput(), null);
				switch (tx.getTypeEnum()) {
				/** 创建验证人 */
				case STAKE_CREATE:
					StakeCreateParam stakeCreateParam= (StakeCreateParam)innerContractDecodedResult.getParam();
					rowData[0] = stakeCreateParam.getNodeId();
					rowData[1] = tx.getHash();
					rowData[2] = tx.getInfo();
					rowData[3] = stakeCreateParam.getBenefitAddress();
					rowData[4] = 1;
					for(String address: list) {
						if(address.equals(stakeCreateParam.getBenefitAddress().toLowerCase())) {
							rowData[4] =0;
							break;
						}
					}
					rowData[5] = Transaction.TypeEnum.STAKE_CREATE.getDesc();
					break;
				/**
				 * 增加质押
				 */
				case STAKE_MODIFY:
					StakeModifyParam stakeModifyParam= (StakeModifyParam)innerContractDecodedResult.getParam();
					rowData[0] = stakeModifyParam.getNodeId();
					rowData[1] = tx.getHash();
					rowData[2] = tx.getInfo();
					rowData[3] = stakeModifyParam.getBenefitAddress();
					rowData[4] = 1;
					if(StringUtils.isNotBlank(stakeModifyParam.getBenefitAddress())) {
						for(String address: list) {
							if(address.equals(stakeModifyParam.getBenefitAddress().toLowerCase())) {
								rowData[4] =0;
								break;
							}
						}
					}
					rowData[5] = Transaction.TypeEnum.STAKE_MODIFY.getDesc();
					break;
				default:
					break;
				}
				log.info("exportMatchNode数据导出,hash：{}", tx.getHash());
				csvRows.add(rowData);
			});	
		} catch (Exception e) {
			log.error("exportMatchNode error", e);
		}
		
		buildFile("exportMatchNode.csv", csvRows, null);
		log.info("exportMatchNode数据导出成功,总行数：{}", csvRows.size());
		txInfoExportDone = true;
	}


	/**
	 * 导出合法的交易类型：委托，委托赎回，获取委托收益，转账<特定from>
	 */
	@Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
	public void exportLegalTx() {
		List<Object[]> csvRows = new ArrayList<>();
		try {
			// 定义表头
			csvRows.add(new String[]{
					"address",
					"balance",
					"illegal tx info"
			});
			// 读取文件内容
			Set<String> lines = readLines(filepath);
			class Counter{
				int illegalTxCount = 0;
				StringBuilder sb = new StringBuilder();
				void reset(){
					illegalTxCount=0;
					sb.setLength(0);
					sb.append("[");
				}
				String getRs(){
					String str = sb.toString();
//					str = str.substring(0,str.lastIndexOf(":"));
					str = str+"]";
					str = illegalTxCount+str;
					return str;
				}
			}
			Counter counter = new Counter();
			int i = 0;
			for(String address: lines) {
				log.info("exportLegalTx数据1,address：{},i:{}",address,i);
				counter.reset();// 重置计数器
				Object[] rowData = new Object[3];
				rowData[0] = address; // 地址
				rowData[1] = HexTool.append(EnergonUtil
						.format(Convert.fromVon(getBalance(address,DefaultBlockParameter.valueOf(BigInteger.valueOf(4812771l))).toString(), Convert.Unit.LAT).setScale(18, RoundingMode.DOWN), 18)); // 余额
				ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
				constructor.buildMust(new BoolQueryBuilder().should(QueryBuilders.termQuery("from", address))
						.should(QueryBuilders.termQuery("to", address)));
				constructor.must(new ESQueryBuilders().range("time", 1587348000000l, 1587646800000l));
				// 遍历交易数据
				traverseTx(constructor,tx -> {
					boolean illegal = true;
					switch (tx.getTypeEnum()) {
						case DELEGATE_CREATE:
						case DELEGATE_EXIT:
						case CLAIM_REWARDS:
							// 合法交易
							illegal = false;
							break;
						case TRANSFER:
							if("0xceca295e1471b3008d20b017c7df7d4f338a7fba".equals(tx.getFrom())){
								// 合法交易
								illegal = false;
							}
					}
					log.info("exportLegalTx数据2,address：{},illegal:{}",address,illegal);
					if(illegal){
						// 非法交易
						counter.illegalTxCount++;
						counter.sb.append(tx.getHash()).append(":");
					}
				});
				rowData[2] = counter.getRs();
				csvRows.add(rowData);
				log.info("exportLegalTx数据3,address：{},i:{}",address,i);
				i++;
			}
		} catch (Exception e) {
			log.error("addressLegalTx error", e);
		}
		buildFile("addressLegalTx.csv", csvRows, null);
		log.info("交易数据导出成功,总行数：{}", csvRows.size());
		exportLegalTxDone = true;
	}
	
	@Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
	public void exportContractData() {
		Map<String, Set<String>> nodesMap = new HashMap<>();
		try {
			File file = new File(filepath);
			if(file.isDirectory()) {
	            File next[]=file.listFiles();
	            for (File f:next) {
	            	Set<String> addresslist = new HashSet<>();
//	                if(next[i].isDirectory()) {
//	                	File addressFile[]=file.listFiles();
//	                	for(File addF:addressFile) {
//	                		if(addF.getPath().endsWith(".csv"))
//	                		addresslist = readLines(addF.getPath());
//	                	}
//	                }
	            	if(f.getPath().endsWith(".csv")) {
	            		addresslist = readLines(f.getPath());
	            	}
	            	String[] name = f.getName().split("\\.");
	                log.info("file:{}", f.getName());
	                nodesMap.put(name[0], addresslist);
	            }
	        }
			List<Object[]> csvRows = new ArrayList<>();
			Object[] rowHead = new Object[9];
			rowHead[0] = "nodeName";
			rowHead[1] = "nodeId";
			rowHead[2] = "txNum";
			rowHead[3] = "EvmNum";
			rowHead[4] = "WasmNum";
			rowHead[5] = "TotalNum";
			rowHead[6] = "txPer";
			rowHead[7] = "EvmPer";
			rowHead[8] = "WasmPer";
			csvRows.add(rowHead);
			for(String nodeId:nodesMap.keySet()) {
				nodeId = HexTool.prefix(nodeId);
				BigInteger total = BigInteger.ZERO;
				Object[] rowData = new Object[9];
				Node node = nodeMapper.selectByPrimaryKey(nodeId);
				rowData[0] = node.getNodeName();
				rowData[1] = nodeId;
				for(String address : nodesMap.get(nodeId)) {
//					ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
//					constructor.must(new ESQueryBuilders().term("type", TypeEnum.TRANSFER.getCode()));
//					constructor.must(new ESQueryBuilders().term("from", address));
//					constructor.must(new ESQueryBuilders().term("status", StatusEnum.SUCCESS.getCode()));
//					ESResult<?> es;
//					try {
//						es = transactionESRepository.Count(constructor);
//						txTotal = txTotal + es.getTotal();
//					} catch (IOException e) {
//						log.error("query es error",e);
//					}
					
					BigInteger beginTx = this.getNonce(address, DefaultBlockParameter.valueOf(BigInteger.valueOf(1l)));
					BigInteger endTx = this.getNonce(address, DefaultBlockParameter.valueOf(BigInteger.valueOf(1l)));
					total = total.add(endTx.subtract(beginTx));
				}
				com.platon.browser.evm.bean.PressureContract evmPressureContract = com.platon.browser.evm.bean.PressureContract
						.load("", getClient(), adminCredentials, provider);
				BigInteger evmTotal =  evmPressureContract.getValue(nodeId).send();
				com.platon.browser.wasm.bean.PressureContract wasmPressureContract = com.platon.browser.wasm.bean.PressureContract
						.load("", getClient(), adminCredentials, provider);
				BigInteger wasmTotal =  wasmPressureContract.getValue(nodeId).send();
				BigInteger txTotal = total.subtract(wasmTotal).subtract(evmTotal);
				rowData[2] = txTotal;
				rowData[3] = evmTotal;
				rowData[4] = wasmTotal;
				rowData[5] = total;
				rowData[6] = new BigDecimal(txTotal).divide(new BigDecimal(total), 2, RoundingMode.HALF_UP);
				rowData[7] = new BigDecimal(evmTotal).divide(new BigDecimal(total), 2, RoundingMode.HALF_UP);
				rowData[8] = new BigDecimal(wasmTotal).divide(new BigDecimal(total), 2, RoundingMode.HALF_UP);
				csvRows.add(rowData);
				log.info("export nodeId:{}", nodeId);
			}
			buildFile("exportContractData.csv", csvRows, null);
			log.info("exportContractData数据导出成功,总行数：{}", csvRows.size());
		} catch (Exception e) {
			log.error("exportContractData error", e);
		}
		
		txInfoExportDone = true;
	}
	
	private BigInteger getNonce(String address, DefaultBlockParameter defaultBlockParameter) throws IOException {
        PlatonGetTransactionCount ethGetTransactionCount = getClient().platonGetTransactionCount(
        		address, defaultBlockParameter).send();

//        if (ethGetTransactionCount.getTransactionCount().intValue() == 0) {
//            ethGetTransactionCount = getClient().platonGetTransactionCount(
//            		address, DefaultBlockParameterName.LATEST).send();
//        }

        return ethGetTransactionCount.getTransactionCount();
    }
}
