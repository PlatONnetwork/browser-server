package com.platon.browser.service;

import com.alibaba.fastjson.JSON;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dto.elasticsearch.ESResult;
import com.platon.browser.elasticsearch.TransactionESRepository;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.elasticsearch.service.impl.ESQueryBuilderConstructor;
import com.platon.browser.elasticsearch.service.impl.ESQueryBuilders;
import com.platon.browser.param.DelegateCreateParam;
import com.platon.browser.param.DelegateExitParam;
import com.platon.browser.param.DelegateRewardClaimParam;
import com.platon.browser.param.StakeCreateParam;
import com.platon.browser.param.StakeExitParam;
import com.platon.browser.param.StakeIncreaseParam;
import com.platon.browser.util.DateUtil;
import com.platon.browser.util.EnergonUtil;
import com.platon.browser.utils.HexTool;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.web3j.utils.Convert;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ExportGallyService {
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
	private TransactionESRepository transactionESRepository;

	@Value("${paging.pageSize}")
	private int transactionPageSize;
	@Value("${paging.maxCount}")
	private int maxCount;
	
	private static String exportBlock;
	
	private static String exportDBBlock;
	
	private static BigInteger eblock ;
	
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
//		constructor.must(new ESQueryBuilders().range("num", eblock.subtract(blockChainConfig.getSettlePeriodBlockCount()).longValue(), eblock.longValue()));
		List<Object> types = new ArrayList<>();
		types.add("1");
		types.add("2");
		constructor.must(new ESQueryBuilders().terms("type", types));
//		rowHead[11] = eblock.subtract(blockChainConfig.getSettlePeriodBlockCount());
//		rowHead[12] = eblock;
		constructor.setAsc("seq");
		// 分页查询区块数据
		ESResult<Transaction> esResult = null;
		for (int pageNo = 1; pageNo <= Integer.MAX_VALUE; pageNo++) {
			try {
				esResult = transactionESRepository.search(constructor, Transaction.class, pageNo, transactionPageSize);
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
				log.info("【exportTxh()】第{}页,{}条记录", pageNo, txList.size());
			} catch (Exception e) {
				log.error("【exportTxh()】导出出错:", e);
				throw e;
			}
		}
		buildFile("tcontractxhash.csv", csvRows, null);
		log.info("交易数据导出成功,总行数：{}", csvRows.size());
		txInfoExportDone = true;
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
