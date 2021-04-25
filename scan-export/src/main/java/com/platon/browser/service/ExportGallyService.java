package com.platon.browser.service;

import com.alibaba.fastjson.JSON;
import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.entity.NodeExample;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.elasticsearch.dto.Transaction.TypeEnum;
import com.platon.browser.service.elasticsearch.query.ESQueryBuilderConstructor;
import com.platon.browser.service.elasticsearch.query.ESQueryBuilders;
import com.platon.browser.param.*;
import com.platon.browser.utils.DateUtil;
import com.platon.browser.utils.EnergonUtil;
import com.platon.browser.utils.SleepUtil;
import com.platon.browser.decoder.PPOSTxDecodeUtil;
import com.platon.browser.decoder.PPOSTxDecodeResult;
import com.platon.browser.utils.HexUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import com.platon.crypto.Credentials;
import com.platon.crypto.WalletUtils;
import com.platon.protocol.core.DefaultBlockParameter;
import com.platon.protocol.core.DefaultBlockParameterName;
import com.platon.tx.Transfer;
import com.platon.utils.Convert;
import com.platon.utils.Convert.Unit;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
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

	@Value("${paging.maxCount}")
	private int maxCount;
	@Value("${filepath}")
	private String filepath;
	@Value("${addresspath}")
	private String addresspath;

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
					HexUtil.append(EnergonUtil.format(
							Convert.fromVon(tx.getValue(), Convert.Unit.KPVON).setScale(18, RoundingMode.DOWN),
							18)),
					HexUtil.append(EnergonUtil.format(
							Convert.fromVon(tx.getCost(), Convert.Unit.KPVON).setScale(18, RoundingMode.DOWN),
							18)),
					HexUtil.append(EnergonUtil.format(
							Convert.fromVon(txAmount, Convert.Unit.KPVON).setScale(18, RoundingMode.DOWN), 18)),
					HexUtil.append(EnergonUtil.format(
							Convert.fromVon(reward, Convert.Unit.KPVON).setScale(18, RoundingMode.DOWN), 18)),
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
	
	
	@Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
	public void transfer() {
		Set<String> lines = readLines(filepath);
		
		int i = 0;
		try {
//			for(String address: list) {
//				PlatonGetBalance platonGetBalance =platonClient.getWeb3jWrapper().getWeb3j().platonGetBalance(address, DefaultBlockParameterName.LATEST).send();
//				if(platonGetBalance.getBalance().compareTo(BigInteger.valueOf(0)) != 0) {
//					log.error("add:{}",address);
//				}
//			}
			Credentials credentials = WalletUtils.loadCredentials("88888888", addresspath);
			BigInteger balance = getBalance("0xceca295e1471b3008d20b017c7df7d4f338a7fba",DefaultBlockParameterName.LATEST);
			log.error("platonGetBalance:{}",balance);
			for(String address: lines) {
					Transfer.sendFunds(
							getClient(),
					        credentials,
					        address,
					        BigDecimal.valueOf(1),
					        Unit.KPVON
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
				PPOSTxDecodeResult PPOSTxDecodeResult = PPOSTxDecodeUtil.decode(tx.getInput(), null);
				switch (tx.getTypeEnum()) {
				/** 创建验证人 */
				case STAKE_CREATE:
					StakeCreateParam stakeCreateParam= (StakeCreateParam) PPOSTxDecodeResult.getParam();
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
					StakeModifyParam stakeModifyParam= (StakeModifyParam) PPOSTxDecodeResult.getParam();
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
				rowData[1] = HexUtil.append(EnergonUtil
						.format(Convert.fromVon(getBalance(address,DefaultBlockParameter.valueOf(BigInteger.valueOf(4812771l))).toString(), Convert.Unit.KPVON).setScale(18, RoundingMode.DOWN), 18)); // 余额
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
}
