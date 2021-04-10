package com.platon.browser.service;

import com.platon.contracts.ppos.dto.resp.Reward;
import com.platon.protocol.core.DefaultBlockParameter;
import com.platon.protocol.core.methods.response.PlatonGetBalance;
import com.platon.utils.Convert;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.SpecialApi;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.*;
import com.platon.browser.service.elasticsearch.bean.ESResult;
import com.platon.browser.service.elasticsearch.EsTransactionRepository;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.service.elasticsearch.query.ESQueryBuilderConstructor;
import com.platon.browser.service.elasticsearch.query.ESQueryBuilders;
import com.platon.browser.enums.ContractDescEnum;
import com.platon.browser.param.*;
import com.platon.browser.utils.DateUtil;
import com.platon.browser.utils.EnergonUtil;
import com.platon.browser.utils.HexUtil;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
//@Service
public class ExportService {
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
    private BlockChainConfig blockChainConfig;

	@Autowired
	private EsTransactionRepository ESTransactionRepository;
	@Autowired
	private AddressMapper addressMapper;
	@Autowired
	private NodeMapper nodeMapper;
	@Autowired
	private RpPlanMapper rpPlanMapper;
	@Autowired
	private DelegationMapper delegationMapper;
	@Autowired
	private ProposalMapper proposalMapper;
	@Autowired
	private VoteMapper voteMapper;
	@Autowired
	private NetworkStatMapper networkStatMapper;
    @Autowired
	private SpecialApi specialApi;

	@Value("${paging.pageSize}")
	private int transactionPageSize;
	@Value("${paging.maxCount}")
	private int maxCount;
	
	private static String exportBlock;
	
	private static String exportDBBlock;
	
	private static BigInteger eblock ;
	
	private static BigInteger dbblock ;

	@PostConstruct
	private void init() {
		eblock = new BigInteger(exportBlock);
		dbblock = new BigInteger(exportDBBlock);
		File destDir = new File(this.fileUrl);
		if (destDir.exists())
			destDir.delete();
		if (!destDir.exists())
			destDir.mkdirs();
	}

	/**
	 * 导出交易表交易hash
	 */
	@Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
	public void exportTxHash() {
		List<Object[]> csvRows = new ArrayList<>();
		ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
		constructor.setDesc("seq");
		// 分页查询区块数据
		ESResult<Transaction> esResult = null;
		for (int pageNo = 0; pageNo * this.transactionPageSize <= this.maxCount; pageNo++) {
			try {
				esResult = ESTransactionRepository.search(constructor, Transaction.class, pageNo, transactionPageSize);
			} catch (Exception e) {
				if (e.getMessage().contains("all shards failed")) {
					break;
				} else {
					log.error("【syncBlock()】查询ES出错:", e);
				}
			}
			if (esResult == null || esResult.getRsData() == null || esResult.getTotal() == 0
					|| esResult.getRsData().isEmpty()) {
				// 如果查询结果为空则结束
				break;
			}
			List<Transaction> txList = esResult.getRsData();
			try {
				txList.forEach(tx -> csvRows.add(new Object[] { tx.getHash() }));
				log.info("【exportTxHash()】第{}页,{}条记录", pageNo, txList.size());
			} catch (Exception e) {
				log.error("【exportTxHash()】导出出错:", e);
				throw e;
			}
		}
		buildFile("txhash.csv", csvRows, null);
		log.info("交易HASH导出成功,总行数：{}", csvRows.size());
		txHashExportDone = true;
	}

	/**
	 * 导出地址表地址
	 */
	@Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
	public void exportAddress() {
		List<Object[]> rows = new ArrayList<>();
		for (int pageNo = 1; pageNo * transactionPageSize < maxCount; pageNo++) {
			PageHelper.startPage(pageNo, transactionPageSize);
			List<Address> data = addressMapper.selectByExample(null);
			if (data.isEmpty())
				break;
			data.forEach(e -> rows.add(new Object[] { e.getAddress() }));
			log.info("【exportAddress()】第{}页,{}条记录", pageNo, rows.size());
		}
		this.buildFile("address.csv", rows, null);
		log.info("地址表导出成功,总行数：{}", rows.size());
		addressExportDone = true;
	}

	/**
	 * 导出rpplan表地址
	 */
	@Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
	public void exportRpPlanAddress() {
		List<Object[]> rows = new ArrayList<>();
		try {
			for (int pageNo = 1; pageNo < 20; pageNo++) {
				PageHelper.startPage(pageNo, 1000);
				List<RpPlan> rpPlans = rpPlanMapper.selectByExample(null);
				if (rpPlans == null || rpPlans.size() == 0) {
					break;
				}
				for (RpPlan d : rpPlans) {
					Object[] row = new Object[1];
					row[0] = d.getAddress();
					rows.add(row);
				}
				log.info("【exportRpPlanAddress()】第{}页,{}条记录", pageNo, rows.size());
			}
		} catch (Exception e) {
			log.error("导出rpplan失败", e);
		}

		log.info("rpplan 导出成功。总共行数：{}", rows.size());
		this.buildFile("rpplan.csv", rows, null);
		log.info("rpplan导出成功,总行数：{}", rows.size());
		rpplanExportDone = true;
	}

	/**
	 * 导出提案hash
	 */
	@Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
	public void exportProposal() {
		List<Object[]> rows = new ArrayList<>();
		try {
			for (int pageNo = 1; pageNo < 200; pageNo++) {
				PageHelper.startPage(pageNo, 1000);
				List<Proposal> proposals = proposalMapper.selectByExample(null);
				if (proposals == null || proposals.size() == 0) {
					break;
				}
				for (Proposal d : proposals) {
					Object[] row = new Object[1];
					row[0] = d.getHash();
					rows.add(row);
				}
				log.info("【exportProposal()】第{}页,{}条记录", pageNo, rows.size());
			}
		} catch (Exception e) {
			log.error("导出proposals失败", e);
		}

		log.info("proposals 导出成功。总共行数：{}", rows.size());
		this.buildFile("proposals.csv", rows, null);
		log.info("proposals导出成功,总行数：{}", rows.size());
		proposalExportDone = true;
	}

	/**
	 * 导出vote hash
	 */
	@Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
	public void exportVote() {
		List<Object[]> rows = new ArrayList<>();
		try {
			for (int pageNo = 1; pageNo < 200; pageNo++) {
				PageHelper.startPage(pageNo, 1000);
				List<Vote> votes = voteMapper.selectByExample(null);
				if (votes == null || votes.size() == 0) {
					break;
				}
				for (Vote d : votes) {
					Object[] row = new Object[1];
					row[0] = d.getHash();
					rows.add(row);
				}
				log.info("【exportVote()】第{}页,{}条记录", pageNo, rows.size());
			}
		} catch (Exception e) {
			log.error("导出vote失败", e);
		}
		this.buildFile("votes.csv", rows, null);
		log.info("votes导出成功,总行数：{}", rows.size());
		voteExportDone = true;
	}

	/**
	 * 导出节点表地址
	 */
	@Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
	public void exportNodeId() {
		List<Object[]> rows = new ArrayList<>();
		PageHelper.startPage(1, 1500);
		List<Node> data = nodeMapper.selectByExample(null);
		data.forEach(e -> rows.add(new Object[] { e.getNodeId() }));
		this.buildFile("node.csv", rows, null);
		log.info("nodes导出成功,总行数：{}", rows.size());
		nodeExportDone = true;
	}

	/**
	 * 导出委托表地址和节点id
	 */
	@Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
	public void exportDelegationInfo() {
		List<Object[]> rows = new ArrayList<>();
		try {
			for (int pageNo = 1; pageNo * transactionPageSize < maxCount; pageNo++) {
				PageHelper.startPage(pageNo, transactionPageSize);
				DelegationExample delegationExample = new DelegationExample();
				delegationExample.setOrderByClause(" sequence desc");
				List<Delegation> delegations = delegationMapper.selectByExample(delegationExample);
				if (delegations == null | delegations.size() == 0) {
					break;
				}
				for (Delegation d : delegations) {
					Object[] row = new Object[2];
					row[0] = d.getDelegateAddr();
					row[1] = d.getNodeId();
					rows.add(row);
				}
				log.info("【exportDelegationInfo()】第{}页,{}条记录", pageNo, rows.size());
			}
		} catch (Exception e) {
			log.error("导出委托失败", e);
		}
		this.buildFile("delegation.csv", rows, null);
		log.info("delegations导出成功,总行数：{}", rows.size());
		delegationExportDone = true;
	}

	@Value("${fileUrl}")
	private String fileUrl;

	public void buildFile(String fileName, List<Object[]> rows, String[] headers) {
		try {
			File file = new File(fileUrl);
			if (!file.exists()) {
				file.mkdir();
			}
			/** 初始化输出流对象 */
			FileOutputStream fis = new FileOutputStream(fileUrl + fileName);
			/** 设置返回的头，防止csv乱码 */
			fis.write(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF });
			OutputStreamWriter outputWriter = new OutputStreamWriter(fis, StandardCharsets.UTF_8);
			/** 厨师书writer对象 */
			CsvWriter writer = new CsvWriter(outputWriter, new CsvWriterSettings());
			if (headers != null) {
				writer.writeHeaders(headers);
			}
			writer.writeRowsAndClose(rows);
		} catch (IOException e) {
			log.error("数据输出错误:", e);
			return;
		}
		log.info("导出报表成功，路径：{}", fileUrl + fileName);
	}

	/**
	 * 导出委托表奖励
	 */
	@Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
	public void exportDelegationReward() {
		List<Object[]> rows = new ArrayList<>();
		Object[] rowHead = new Object[2];
		rowHead[0] = "委托地址";
		rowHead[1] = "待领取奖励";
		rows.add(rowHead);
		List<String> address = new ArrayList<>();
		try {
			for (int pageNo = 1; pageNo < Integer.MAX_VALUE; pageNo++) {
				PageHelper.startPage(pageNo, transactionPageSize);
				DelegationExample delegationExample = new DelegationExample();
				delegationExample.setOrderByClause(" sequence desc");
				List<Delegation> delegations = delegationMapper.selectByExample(delegationExample);
				if (delegations == null | delegations.size() == 0) {
					break;
				}
				for (Delegation d : delegations) {
					if (address.contains(d.getDelegateAddr())) {
						continue;
					}
					address.add(d.getDelegateAddr());
					Object[] row = new Object[2];
					row[0] = d.getDelegateAddr();
					List<String> nodes = new ArrayList<>();
					List<Reward> rewards = platonClient.getRewardContract()
							.getDelegateReward(d.getDelegateAddr(), nodes).send().getData();
					/**
					 * 当奖励为空时直接return
					 */
					BigDecimal allRewards = BigDecimal.ZERO;
					if (rewards != null) {
						for (Reward reward : rewards) {
							allRewards = allRewards.add(new BigDecimal(reward.getReward()));
						}
					}
					row[1] = HexUtil.append(EnergonUtil
							.format(Convert.fromVon(allRewards, Convert.Unit.KPVON).setScale(18, RoundingMode.DOWN), 18));
					rows.add(row);
				}
				log.info("【exportDelegationReward()】第{}页,{}条记录", pageNo, rows.size());
			}
		} catch (Exception e) {
			log.error("导出委托奖励失败", e);
		}
    	this.buildFile("delegationReward.csv", rows, null);
        log.info("delegations导出成功,总行数：{}", rows.size());
        delegationRewardExportDone = true;
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
		Object[] rowHead = new Object[13];
		rowHead[0] = "tx hash";
		rowHead[1] = "tx block";
		rowHead[2] = "tx time";
		rowHead[3] = "tx type";
		rowHead[4] = "from";
		rowHead[5] = "to";
		rowHead[6] = "value";
		rowHead[7] = "tx fee cost";
		rowHead[8] = "tx amount";
		rowHead[9] = "tx reward";
		rowHead[10] = "tx info";
		csvRows.add(rowHead);
		ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
		constructor.must(new ESQueryBuilders().range("num", eblock.subtract(blockChainConfig.getSettlePeriodBlockCount()).longValue(), eblock.longValue()));
		rowHead[11] = eblock.subtract(blockChainConfig.getSettlePeriodBlockCount());
		rowHead[12] = eblock;
		constructor.setDesc("seq");
		// 分页查询区块数据
		ESResult<Transaction> esResult = null;
		for (int pageNo = 0; pageNo <= Integer.MAX_VALUE; pageNo++) {
			try {
				esResult = ESTransactionRepository.search(constructor, Transaction.class, pageNo, transactionPageSize);
			} catch (Exception e) {
				if (e.getMessage().contains("all shards failed")) {
					break;
				} else {
					log.error("【syncBlock()】查询ES出错:", e);
				}
			}
			if (esResult == null || esResult.getRsData() == null || esResult.getTotal() == 0
					|| esResult.getRsData().isEmpty()) {
				// 如果查询结果为空则结束
				break;
			}
			List<Transaction> txList = esResult.getRsData();
			try {
				txList.forEach(tx -> {
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
				log.info("【exportTxh()】第{}页,{}条记录", pageNo, txList.size());
			} catch (Exception e) {
				log.error("【exportTxh()】导出出错:", e);
				throw e;
			}
		}
		buildFile(eblock.toString()+"txhash.csv", csvRows, null);
		log.info("交易数据导出成功,总行数：{}", csvRows.size());
//		txInfoExportDone = true;
	}

	/**
	 * @throws Exception 
	 * 导出验证人信息表
	 * @throws  
	 */
	@Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
	public void exportStaking() throws Exception  {
//		BigInteger blockNumer = this.checkNumer();
	//		List<com.alaya.contracts.ppos.dto.resp.Node> nodes =platonClient.getNodeContract().getValidatorList().send().getData();
		DeNodeContract deNodeContract = DeNodeContract.load(platonClient.getWeb3jWrapper().getWeb3j());
		List<com.platon.contracts.ppos.dto.resp.Node> nodes = deNodeContract.getValidatorList(eblock).send().getData();
		List<Object[]> rows = new ArrayList<>();
		Object[] rowHead = new Object[26];
		rows.add(rowHead);
		Field[] fields = nodes.get(0).getClass().getDeclaredFields();
		String[] fieldNamStrings = new String[fields.length];
		for (int i = 0; i < fieldNamStrings.length; i++) {
			rowHead[i] = fields[i].getName();
		}
		rowHead[24] = "BenefitBalance";
		rowHead[25] = "blockNumber";
		try {
			for (com.platon.contracts.ppos.dto.resp.Node node : nodes) {
				Object[] row = new Object[26];
				for(int i = 0; i < rowHead.length -2; i++) {
					Object value = this.getFieldValueByName((String)rowHead[i], node);
					if("shares,released,releasedHes,restrictingPlan,restrictingPlanHes,delegateEpoch,delegateTotal,delegateTotalHes,delegateRewardTotal"
							.contains((String)rowHead[i]) && value != null) {
						value = HexUtil.append(EnergonUtil
								.format(Convert.fromVon(new BigDecimal((BigInteger)value), Convert.Unit.KPVON).setScale(18, RoundingMode.DOWN), 18));
					}
					row[i] = value;
				}
				PlatonGetBalance balance = platonClient.getWeb3jWrapper().getWeb3j().platonGetBalance(node.getBenifitAddress(), DefaultBlockParameter.valueOf(eblock))
					.send();
				row[24] = HexUtil.append(EnergonUtil
						.format(Convert.fromVon(new BigDecimal(balance.getBalance()), Convert.Unit.KPVON).setScale(18, RoundingMode.DOWN), 18));
				row[25] = eblock;
				rows.add(row);
			}
		} catch (Exception e) {
			log.error("导出验证信息失败", e);
		}
		this.buildFile(eblock.toString() + "staking.csv", rows, null);
		log.info("staking导出成功,总行数：{}", rows.size());
		
		rowHead = new Object[4];
		rowHead[0] = "blockNumber";
		rowHead[1] = "address";
		rowHead[2] = "addressName";
		rowHead[3] = "Balance";
		rows = new ArrayList<>();
		rows.add(rowHead);
		for(String address  :ContractDescEnum.getAddresses()) {
			Object[] row = new Object[4];
			row[0] = eblock;
			row[1] = address;
			row[2] = ContractDescEnum.getMap().get(address).getContractName();
			PlatonGetBalance balance = platonClient.getWeb3jWrapper().getWeb3j().platonGetBalance(address, DefaultBlockParameter.valueOf(eblock))
					.send();
			row[3] = HexUtil.append(EnergonUtil
					.format(Convert.fromVon(new BigDecimal(balance.getBalance()), Convert.Unit.KPVON).setScale(18, RoundingMode.DOWN), 18));
			rows.add(row);
		}
		this.buildFile(eblock.toString() + "contract.csv", rows, null);
		log.info("contactValue导出成功,总行数：{}", rows.size());
		stakingExportDone = true;
	}
	
	/**
	 * @throws Exception 
	 * 导出验证人信息表
	 * @throws  
	 */
	@Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
	public void exportAllStaking() throws Exception  {
//		BigInteger blockNumer = this.checkNumer();
	//		List<com.alaya.contracts.ppos.dto.resp.Node> nodes =platonClient.getNodeContract().getValidatorList().send().getData();
		DeNodeContract deNodeContract = DeNodeContract.load(platonClient.getWeb3jWrapper().getWeb3j());
		List<com.platon.contracts.ppos.dto.resp.Node> nodes = deNodeContract.getCandidateList(eblock).send().getData();
		List<Object[]> rows = new ArrayList<>();
		Object[] rowHead = new Object[26];
		rows.add(rowHead);
		Field[] fields = nodes.get(0).getClass().getDeclaredFields();
		String[] fieldNamStrings = new String[fields.length];
		for (int i = 0; i < fieldNamStrings.length; i++) {
			rowHead[i] = fields[i].getName();
		}
		rowHead[24] = "BenefitBalance";
		rowHead[25] = "blockNumber";
		try {
			for (com.platon.contracts.ppos.dto.resp.Node node : nodes) {
				Object[] row = new Object[26];
				for(int i = 0; i < rowHead.length -2; i++) {
					Object value = this.getFieldValueByName((String)rowHead[i], node);
					if("shares,released,releasedHes,restrictingPlan,restrictingPlanHes,delegateEpoch,delegateTotal,delegateTotalHes,delegateRewardTotal"
							.contains((String)rowHead[i]) && value != null) {
						value = HexUtil.append(EnergonUtil
								.format(Convert.fromVon(new BigDecimal((BigInteger)value), Convert.Unit.KPVON).setScale(18, RoundingMode.DOWN), 18));
					}
					row[i] = value;
				}
				PlatonGetBalance balance = platonClient.getWeb3jWrapper().getWeb3j().platonGetBalance(node.getBenifitAddress(), DefaultBlockParameter.valueOf(eblock))
					.send();
				row[24] = HexUtil.append(EnergonUtil
						.format(Convert.fromVon(new BigDecimal(balance.getBalance()), Convert.Unit.KPVON).setScale(18, RoundingMode.DOWN), 18));
				row[25] = eblock;
				rows.add(row);
			}
		} catch (Exception e) {
			log.error("导出验证信息失败", e);
		}
		this.buildFile(eblock.toString() + "allstaking.csv", rows, null);
		log.info("staking导出成功,总行数：{}", rows.size());
		
		rowHead = new Object[4];
		rowHead[0] = "blockNumber";
		rowHead[1] = "address";
		rowHead[2] = "addressName";
		rowHead[3] = "Balance";
		rows = new ArrayList<>();
		rows.add(rowHead);
		for(String address  :ContractDescEnum.getAddresses()) {
			Object[] row = new Object[4];
			row[0] = eblock;
			row[1] = address;
			row[2] = ContractDescEnum.getMap().get(address).getContractName();
			PlatonGetBalance balance = platonClient.getWeb3jWrapper().getWeb3j().platonGetBalance(address, DefaultBlockParameter.valueOf(eblock))
					.send();
			row[3] = HexUtil.append(EnergonUtil
					.format(Convert.fromVon(new BigDecimal(balance.getBalance()), Convert.Unit.KPVON).setScale(18, RoundingMode.DOWN), 18));
			rows.add(row);
		}
		this.buildFile(eblock.toString() + "contract.csv", rows, null);
		log.info("contactValue导出成功,总行数：{}", rows.size());
		
		stakingExportDone = true;
	}
	
	/**
	 * @throws Exception 
	 * 导出验证人信息表
	 * @throws  
	 */
	@Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
	public void exportBalance() throws Exception  {
		BigInteger blockNumer = platonClient.getLatestBlockNumber();
		Object[] rowHead = new Object[4];
		rowHead[0] = "blockNumber";
		rowHead[1] = "address";
		rowHead[2] = "addressName";
		rowHead[3] = "Balance";
		List<Object[]> rows = new ArrayList<>();
		rows.add(rowHead);
		BigInteger balanceBlock = BigInteger.valueOf(10750);
		while(balanceBlock.compareTo(blockNumer) < 0) {
			for(String address  :ContractDescEnum.getAddresses()) {
				Object[] row = new Object[4];
				row[0] = balanceBlock;
				row[1] = address;
				row[2] = ContractDescEnum.getMap().get(address).getContractName();
				PlatonGetBalance balance = platonClient.getWeb3jWrapper().getWeb3j().platonGetBalance(address, DefaultBlockParameter.valueOf(balanceBlock))
						.send();
				row[3] = HexUtil.append(EnergonUtil
						.format(Convert.fromVon(new BigDecimal(balance.getBalance()), Convert.Unit.KPVON).setScale(18, RoundingMode.DOWN), 18));
				rows.add(row);
			}
			balanceBlock = balanceBlock.add(blockChainConfig.getSettlePeriodBlockCount());
		}
		this.buildFile("allcontract.csv", rows, null);
		log.info("contactValue导出成功,总行数：{}", rows.size());
	}

	
	/**
	 * @throws Exception 
	 * 导出验证人信息表
	 * @throws  
	 */
	@Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
	public void exportBenBalance() throws Exception  {
		Object[] rowHead = new Object[3];
		rowHead[0] = "blockNumber";
		rowHead[1] = "address";
		rowHead[2] = "Balance";
		List<Object[]> rows = new ArrayList<>();
		rows.add(rowHead);
		String allB = "0xa32582d52507009d0ea9fdfc985f4eea248df9f3,"+
				"0x37a1bd3525166e491bec03b4ec74f7c76263e7c7,"+
				"0xe3a0b6cb189584b5fd056b5eddbf68ecf2293e12,"+
				"0xbb352877e8e7f226a14138640778f5fb579fdc9e,"+
				"0xbb850a7e06cddd0876e1ff3bb1479a32189e2a41,"+
				"0xb251aaac1fa965fd1779cc3de6bc783c78e8cc45,"+
				"0xc47e7eefbd60cd25ea507994b173b78caa1578c9,"+
				"0x3e375c1f2231169f051e65da32559e510ae41f62,"+
				"0x79620b39207a23383c5cfd3ffa79878c127706e1,"+
				"0x3ad0db9d5da4aea296c077bb414f56c6a963c39e,"+
				"0x481d886727c74a016a958df6375b8359ab95078e,"+
				"0xd0be1c27aab6cf3acfb7bf114ec71909e5c081ff,"+
				"0x9223d47a2500e213c965c76697f3cd0131855cfd,"+
				"0x940ffb09ff6872c8b2877abebc9a350a293b0a26,"+
				"0x7177816101873b4d43a7d134f8f8e51782042a6d,"+
				"0xc0911b350173ece9decc0647ae622e8ae43aa5dc,"+
				"0xee82dbfcd659194df9d5963ae7b9627a5d43723b,"+
				"0x03f7b94c7d8dbe4ae4b48f5bc4de58c772e6d770,"+
				"0x071addcfe38be50c6512358821a43b6c492ec044,"+
				"0x5a40f060ea3686f31226c62b5afe81e8daa91067,"+
				"0xcf8fb315b7b8949ab72ea80bf49b727d0feaf772,"+
				"0xd8f02d215ada614fdd9134696d505f1c66eedff0,"+
				"0x79831e0e5881463111a05302be0f8cd5bdfac6fe,"+
				"0xfd29ae2c54f2061b4a04e8fdfda803037b4b029d,"+
				"0x1a9c37b88744a207fbaacf9c7f04c395688e3300";
		String[] addresses = allB.split(",");
		BigInteger balanceBlock = BigInteger.valueOf(548249);
		for(String address:addresses) {
			Object[] row = new Object[3];
			row[0] = balanceBlock;
			row[1] = address;
			PlatonGetBalance balance = platonClient.getWeb3jWrapper().getWeb3j().platonGetBalance(address, DefaultBlockParameter.valueOf(balanceBlock))
					.send();
			row[2] = HexUtil.append(EnergonUtil
					.format(Convert.fromVon(new BigDecimal(balance.getBalance()), Convert.Unit.KPVON).setScale(18, RoundingMode.DOWN), 18));
			rows.add(row);
		}
		this.buildFile("allbenefit111.csv", rows, null);
		log.info("allbenefit导出成功,总行数：{}", rows.size());
	}
	
	/**
	 * * * @Description 根据属性名 获取值（value） * @param name * @param user * @return
	 * * @throws IllegalAccessException
	 */
	public Object getFieldValueByName(String name, com.platon.contracts.ppos.dto.resp.Node node) {
		String firstletter = name.substring(0, 1).toUpperCase();
		String getter = "get" + firstletter + name.substring(1);

		Method method;
		Object value;
		try {
			method = node.getClass().getMethod(getter);
			value = method.invoke(node);
			return value;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public BigInteger checkNumer() {
//		try {
//			Thread.sleep(500l);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		BigInteger blockNumer = BigInteger.ZERO;
		try {
			blockNumer = platonClient.getLatestBlockNumber();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if(blockNumer.compareTo(eblock) <= 0) {
			return BigInteger.ZERO;
		}
		
		return blockNumer;
	}
	
	public boolean checkDatabaseNumber() {
		List<NetworkStat> networkStat = networkStatMapper.selectByExample(null);
		if(BigInteger.valueOf(networkStat.get(0).getCurNumber()).compareTo(dbblock) <= 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 查询节点对应的出块奖励、质押奖励和手续费奖励
	 * @throws IOException 
	 */
	@Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
	public void exportNodeInfo() throws Exception {
		Object[] rowHead = new Object[5];
		rowHead[0] = "nodeId";
		rowHead[1] = "nodeName";
		rowHead[2] = "stat_block_reward_value";
		rowHead[3] = "stat_staking_reward_value";
		rowHead[4] = "stat_fee_reward_value";
		List<Object[]> rows = new ArrayList<>();
		rows.add(rowHead);
		for (int pageNo = 1; pageNo < Integer.MAX_VALUE; pageNo++) {
			PageHelper.startPage(pageNo, transactionPageSize);
			List<Node> data = nodeMapper.selectByExample(null);
			if (data.isEmpty())
				break;
			for(Node node: data) {
				rows.add(new Object[] { node.getNodeId() ,node.getNodeName() 
						, HexUtil.append(EnergonUtil
								.format(Convert.fromVon(node.getStatBlockRewardValue(), Convert.Unit.KPVON).setScale(18, RoundingMode.DOWN), 18))
						, HexUtil.append(EnergonUtil
								.format(Convert.fromVon(node.getStatStakingRewardValue(), Convert.Unit.KPVON).setScale(18, RoundingMode.DOWN), 18))
						, HexUtil.append(EnergonUtil
										.format(Convert.fromVon(node.getStatFeeRewardValue(), Convert.Unit.KPVON).setScale(18, RoundingMode.DOWN), 18))});
			}
			log.info("【exportNodeInfo()】第{}页,{}条记录", pageNo, rows.size());
		}
		this.buildFile(dbblock.toString() + "NodeInfo.csv", rows, null);
		log.info("节点信息表导出成功,总行数：{}", rows.size());
		dbblock = dbblock.add(blockChainConfig.getSettlePeriodBlockCount());
		addressExportDone = true;
	}
	
	/**
	 * 导出地址表地址余额
	 * @method exportNodeInfo
	 * @throws IOException
	 */
	@Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
	public void exportAddressBalance() throws IOException {
		List<Object[]> rows = new ArrayList<>();
		for (int pageNo = 1; pageNo < Integer.MAX_VALUE; pageNo++) {
			PageHelper.startPage(pageNo, transactionPageSize);
			List<Address> data = addressMapper.selectByExample(null);
			if (data.isEmpty())
				break;
			for(Address address: data) {
				PlatonGetBalance balance = platonClient.getWeb3jWrapper().getWeb3j().platonGetBalance(address.getAddress(), DefaultBlockParameter.valueOf(eblock))
						.send();
				rows.add(new Object[] { address.getAddress() , HexUtil.append(EnergonUtil
						.format(Convert.fromVon(new BigDecimal(balance.getBalance()), Convert.Unit.KPVON).setScale(18, RoundingMode.DOWN), 18))});
			}
			log.info("【exportAddressBalance()】第{}页,{}条记录", pageNo, rows.size());
		}
		this.buildFile(exportBlock + "addressBalance.csv", rows, null);
		log.info("地址表导出成功,总行数：{}", rows.size());
		addressExportDone = true;
	}
	
	public void addCount(){
		eblock = eblock.add(blockChainConfig.getSettlePeriodBlockCount());
	}


	public static String getExportDBBlock() {
		return exportDBBlock;
	}

	@Value("${exportDBBlock}")
	public void setExportDBBlock(String exportDBBlock) {
		this.exportDBBlock = exportDBBlock;
	}

	public static String getExportBlock() {
		return exportBlock;
	}

	@Value("${exportBlock}")
	public void setExportBlock(String exportBlock) {
		this.exportBlock = exportBlock;
	}
}
