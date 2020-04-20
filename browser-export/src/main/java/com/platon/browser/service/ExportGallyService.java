package com.platon.browser.service;

import com.alibaba.fastjson.JSON;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.entity.NodeExample;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.dto.elasticsearch.ESResult;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.elasticsearch.dto.Transaction.TypeEnum;
import com.platon.browser.elasticsearch.service.impl.ESQueryBuilderConstructor;
import com.platon.browser.elasticsearch.service.impl.ESQueryBuilders;
import com.platon.browser.param.*;
import com.platon.browser.param.StakeModifyParam;
import com.platon.browser.util.DateUtil;
import com.platon.browser.util.EnergonUtil;
import com.platon.browser.util.SleepUtil;
import com.platon.browser.util.decode.innercontract.InnerContractDecodeUtil;
import com.platon.browser.util.decode.innercontract.InnerContractDecodedResult;
import com.platon.browser.utils.HexTool;
import lombok.Getter;
import lombok.Setter;
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
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.PlatonGetBalance;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class ExportGallyService extends ServiceBase {
	@Getter
	@Setter
	private static volatile boolean txHashExportDone = false;
	@Getter
	@Setter
	private static volatile boolean addressExportDone = false;
	@Getter
	@Setter
	private static volatile boolean rpplanExportDone = false;
	@Getter
	@Setter
	private static volatile boolean nodeExportDone = false;
	@Getter
	@Setter
	private static volatile boolean delegationExportDone = false;
	@Getter
	@Setter
	private static volatile boolean proposalExportDone = false;
	@Getter
	@Setter
	private static volatile boolean voteExportDone = false;
	@Getter
	@Setter
	private static volatile boolean delegationRewardExportDone = false;
	@Getter
	@Setter
	private static volatile boolean txInfoExportDone = false;
	@Getter
	@Setter
	private static volatile boolean stakingExportDone = false;
	@Autowired
	private PlatOnClient platonClient;
	@Autowired
    private NodeMapper nodeMapper;
	protected static final BigInteger GAS_LIMIT = BigInteger.valueOf(470000);
	protected static final BigInteger GAS_PRICE = BigInteger.valueOf(10000000000L);

	@Value("${paging.maxCount}")
	private int maxCount;
	@Value("${filepath}")
	private String filepath;
	@Value("${addresspath}")
	private String addresspath;
	
	@PostConstruct
	private void init() {
		File destDir = new File(fileUrl);
		if (destDir.exists())
			destDir.delete();
		if (!destDir.exists())
			destDir.mkdirs();
	}

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
			switch (Transaction.TypeEnum.getEnum(tx.getType())) {
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
		List<String> lines = readLines(filepath);
		int i = 0;
		for(String address: lines) {
			Object[] rowData = new Object[4];
			rowData[0] = address;
			
			try {
				PlatonGetBalance platonGetBalance = platonClient.getWeb3jWrapper().getWeb3j()
					.platonGetBalance(address, DefaultBlockParameterName.LATEST).send();
				rowData[1] = platonGetBalance.getBalance();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
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
	
	
	@Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
	public void transfer() {
		 List<String> lines = readLines(filepath);
		
		int i = 0;
		try {
//			for(String address: list) {
//				PlatonGetBalance platonGetBalance =platonClient.getWeb3jWrapper().getWeb3j().platonGetBalance(address, DefaultBlockParameterName.LATEST).send();
//				if(platonGetBalance.getBalance().compareTo(BigInteger.valueOf(0)) != 0) {
//					log.error("add:{}",address);
//				}
//			}
			Credentials credentials = WalletUtils.loadCredentials("88888888", addresspath);
			PlatonGetBalance platonGetBalance =platonClient.getWeb3jWrapper().getWeb3j().platonGetBalance("0xceca295e1471b3008d20b017c7df7d4f338a7fba", DefaultBlockParameterName.LATEST).send();
			log.error("platonGetBalance:{}",platonGetBalance.getBalance());
			for(String address: lines) {
					Transfer.sendFunds(
							platonClient.getWeb3jWrapper().getWeb3j(),
					        credentials,
					        "101",
					        address,
					        BigDecimal.valueOf(1),
					        Unit.LAT
					).send();
					
				
//					TransactionManager transactionManager = new RawTransactionManager(platonClient.getWeb3jWrapper().getWeb3j()
//							, credentials,101l);
//					PlatonSendTransaction ethSendTransaction = transactionManager.sendTransaction(GAS_PRICE, GAS_LIMIT, address, "", new BigInteger("1000000000000000000000"));
					SleepUtil.sleep(5);
//					log.error("transfer数据,address：{},i:{},status:{}",address,i,ethSendTransaction.getResult());
					i++;
			} 
		} catch ( Exception e1) {
			log.error("transerr", e1);
		}
		log.info("转账成功,总行数：{}",i);
		txInfoExportDone = true;
	}
	
	@Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
	public void exportMatchNode() {
		List<String> list = readLines(filepath);
		
		List<Object[]> csvRows = new ArrayList<>();
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
		typeList.add(String.valueOf(TypeEnum.STAKE_CREATE.getCode()));
		typeList.add(String.valueOf(TypeEnum.STAKE_MODIFY.getCode()));
		constructor.must(new ESQueryBuilders().terms("type", typeList));
		constructor.must(new ESQueryBuilders().range("time", 1587348000l, 1587646800l));
		
		
		traverseTx(constructor, tx->{
			Object[] rowData = new Object[6];
			InnerContractDecodedResult innerContractDecodedResult = InnerContractDecodeUtil.decode(tx.getInput(), null);
			switch (Transaction.TypeEnum.getEnum(tx.getType())) {
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
		
		buildFile("exportMatchNode.csv", csvRows, null);
		log.info("exportMatchNode数据导出成功,总行数：{}", csvRows.size());
		txInfoExportDone = true;
	}
	
}
