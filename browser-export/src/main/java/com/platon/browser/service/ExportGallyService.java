package com.platon.browser.service;

import com.alibaba.fastjson.JSON;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.entity.NodeExample;
import com.platon.browser.dao.mapper.NodeMapper;
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
import com.platon.browser.util.SleepUtil;
import com.platon.browser.utils.HexTool;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.PlatonGetBalance;
import org.web3j.protocol.core.methods.response.PlatonSendTransaction;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;
import org.web3j.utils.Numeric;

import javax.annotation.PostConstruct;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    private NodeMapper nodeMapper;
	@Autowired
	private TransactionESRepository transactionESRepository;
	protected static final BigInteger GAS_LIMIT = BigInteger.valueOf(470000);
	protected static final BigInteger GAS_PRICE = BigInteger.valueOf(10000000000L);

	@Value("${paging.pageSize}")
	private int transactionPageSize;
	@Value("${paging.maxCount}")
	private int maxCount;
	@Value("${filepath}")
	private String filepath;
	@Value("${addresspath}")
	private String addresspath;
	
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


	@Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
	public void exportMatch() {
		 List<String> list = new ArrayList<String>();
		
		try {
			File file = new File(filepath);
	        InputStream in = new FileInputStream(file);
	        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	        while (reader.ready()) {
	            String line = reader.readLine();
	            list.add(line.trim().toLowerCase());
	        }
	        reader.close();
	    } catch (Exception e) {
	        log.error("read error", e);
	    }
		
		List<Object[]> csvRows = new ArrayList<>();
		Object[] rowHead = new Object[4];
		rowHead[0] = "address";
		rowHead[1] = "balance";
		rowHead[2] = "tx";
		rowHead[3] = "nodeId";
		csvRows.add(rowHead);
		int i = 0;
		for(String address: list) {
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
			// 分页查询区块数据
			ESResult<Transaction> esResult = null;
			try {
				esResult = transactionESRepository.search(constructor, Transaction.class, 0, 100);
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
				rowData[2] = 0;
			} else {
				rowData[2] = esResult.getTotal();
				List<Transaction> txList = esResult.getRsData();
				try {
					txList.forEach(tx -> {
						rowData[2] = rowData[2]+";" + tx.getHash();
					});
				} catch (Exception e) {
					log.error("【exportMatch()】导出出错:", e);
					throw e;
				}
			}
			
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
		 List<String> list = new ArrayList<String>();
		
		try {
			File file = new File(filepath);
	        InputStream in = new FileInputStream(file);
	        BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
	        while (reader.ready()) {
	            String line = reader.readLine();
	            list.add(line.trim());
	        }
	        reader.close();
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		
//		String addresse = "0xffc957cd6fc1faa30a78550228fa1a1c3dad336d,"+
//				"0x6110f58e2dc8c193fe48354cdd4448d011a0da5e,"+
//				"0x23d8f8d9e4f2DEffE52F356D973A6d448D250184,"+
//				"0x03DeCF18263BA5bcF00c24AA2504d122263EFc20,"+
//				"0x1c0A84C11Ee5156401dc0cF6978ba2A4e48455C5,"+
//				"0xEeDfa28A2CDf1435163560C5168bfee278B7A4Ef,"+
//				"0x9a5eEe38CeABe39Dd09Ac4fe7447F4A39d44cc93,"+
//				"0x17d02d750e6c461a9ef963ca7bd779380b607a66,"+
//				"0x4954c66e62aa27c4b714cbf183904fd8331f853d,"+
//				"0xa074944679881ecea1255ccd95bca71c476925c4,"+
//				"0x17c807416fdccc5b167e734331a39ffc8ce44775,"+
//				"0x4557d8eb0b9328693506c6672c2fca688b4ab30b,"+
//				"0xf48228a422CeaE7Fc4fc11e6581db4cbbD4C3ac5,"+
//				"0x92F8Bd42E005177cc065010A68738eA2c372F6cB,"+
//				"0xfe597862671e14b266DFc17cec51a92526eb2570,"+
//				"0x4a7A83331E7EFA6EEc3a2b4639F7799e448BEB6E,"+
//				"0x40fabe27201e419a1894f66c02a248f01a0e094a,"+
//				"0x8e04ba6316fea813936bbfeefd399a5762dced87,"+
//				"0x25181676c4e2a66acf99818f6f7bb3db9942eab1,"+
//				"0x56a8154b77cdb61d6041d095e3bde1ffdfd09d13,"+
//				"0x85257a82e40f5faadc278e6b6efdd8a039472579,"+
//				"0xa5abd216b044a1bb66f3ee41cc7ec17e049fb21c,"+
//				"0x224b822d74212eabfffef5dcd3a697ef8520ffdc,"+
//				"0x6cbee363704f0a6bcf43d098e580cec85848800b,"+
//				"0x2896f92ed5b5afdbfbe2e145f71bbb7caab0cb9c,"+
//				"0x48cd789e31fa826280d9485d54772256b108a130,"+
//				"0xe335b3a8155fD22A799Fcc58B8F9C2551Ef3541D,"+
//				"0xD1ae4Fc1b4002c3eCF8A384dC6b1434350D2D947,"+
//				"0x41F50166CE36b9DD365e7E6EE7D93FdE7AB04159,"+
//				"0x3cD6Aca043933Af66c476f84b70197eA31A9d881,"+
//				"0x3312831D1A01436D06B88Eee6Cec0DE64d56adda,"+
//				"0x2505Dc07e2b64bdd7c456327c58968aE3EDd5C16,"+
//				"0x8EEc28E561c2dCaD23E79aDD0946C27087feDFB0,"+
//				"0xA9C7B1bcc040c7983fBCa1565fe244a77DD8a300,"+
//				"0x8dDBc20C14f2c8930b89F768B08AE37cB64AC1b7,"+
//				"0x4107fee23E8743930241B72b1efb32f104976a19,"+
//				"0xe0239379c60445B46D4935eDc22120dE02F94e85,"+
//				"0x773cdccc2c5782597886686ed46b11aa3BC8D25A,"+
//				"0xe5b2e079cbbce9e6793b49ead8249a4b7aef07f9,"+
//				"0x5f804ba6cb49be94d4c7b03a6cd40b8912e7f52e,"+
//				"0x4256f92e997a1a52d4a082f804c51b752cbb5ecb,"+
//				"0x86c2643f12c23ee3a933d4c225dc43cb2a7ef8d7,"+
//				"0x309e64aaba890d18f5511de5a974a835cdbd2549,"+
//				"0x2eF5BFc9396DCDd11A9Cb051ef4231610b50b312,"+
//				"0x052e2B73b652a081bC4bC6e0754D97Dda6e79F4b,"+
//				"0x39eF83EA934817Df4B41C1174028706f7D8056Ac,"+
//				"0x8D01eF5FEA4C5fA266Fc27220BdB3Bf2602C072c,"+
//				"0x8B42621507c4980b35aF3BE5a8a1A309F9d003fD,"+
//				"0x70d2921ba85029e5cbf3a6530cdfcdfff803d061,"+
//				"0x7e359fd6dfae4ef6b52f3c801c3726f784d06012,"+
//				"0xf1c4fe12fd5cf80e0af96426f2a54ad78852030f,"+
//				"0x4c71c890b2770ab3a9c01c27b05e835c6281f802,"+
//				"0xb919eddcbedc021f357cba13cec8196117fbd0d3,"+
//				"0xd90af630efd8a413610d676e779784d438f8b770,"+
//				"0x5f8bb36329c6b3d9f51b1231b04e2996fe8a527a,"+
//				"0x1b19bc9f7aa533734ee9df64899769974e3a1bbb,"+
//				"0xb2fb0344c0a946012d07405a6b01891bbb6a79b0,"+
//				"0x9f348ddbde046be8d4c0c78ee55eb423079b2494,"+
//				"0xb919eddcbedc021f357cba13cec8196117fbd0d3,"+
//				"0x989b91aff6926b55752732deb97ba7fb4f5eb249,"+
//				"0x35a9242747dd767c17d5f37c32620fd8682540f9,"+
//				"0xf7eeb89ca4936cd96489e7b20fb2746f69620f5e,"+
//				"0xc98b4f6b10f6fc01d5ef75e334e2bf8af4965e66,"+
//				"0x99c14283e07b68892b13870ceb598493fe55b091,"+
//				"0x610ec777d3b6c44740f047e420227cf1c2c45747,"+
//				"0x19e93642f069271b6630e84698b81c622d3493f5,"+
//				"0x6e1607ec1eac04bf3fcd20b2e179f3e39215bdfe,"+
//				"0x6c3960ebd86d2476de52c20b7d364dce9b06df3a,"+
//				"0x69a3041b53828d193d6228bc08d94f8c6894a357,"+
//				"0x681d21379ed5cae61762a6af2288fa838449dfec,"+
//				"0x84f6638e89dbd84c83469abf164455874ccf1c3f,"+
//				"0x43fe7605e3e8c87072879bfa6f916a75a9dbea61,"+
//				"0x7190e2d5318806e9e181cf81a2f4ca98f5f4d1e9,"+
//				"0x5D2410a2e5c4523Cc659518e47EffcEA6A407fa8,"+
//				"0x44EdBEb9f310a142f3a30FF6a38aDFCa0b4C8e00,"+
//				"0xC15f0cFDCc481Af36bdBb38D0A7dceb7E7ce6146,"+
//				"0x5061cd0E4DB05F5FA49eAa2D8Ca234f24BF72C1c,"+
//				"0x7C84B1dd37d27d4BBBCA48BFd81b81C7622d75C6,"+
//				"0x8670d58a9386a144e8bd1eb6548307f5e022fd8b,"+
//				"0x0df8cc6a88fbcf01090e4d5433e2c2b1979ce7d1,"+
//				"0x80f8a402ef5f9535bb138a8b1a1cbf1152e6c411,"+
//				"0x44bd87064dd5f98b1ca673548787d455f796782e,"+
//				"0x551e1b2029d74af34cff3c0ed991d926fdbb0398,"+
//				"0xad2a37835903196c7fbfc1937fe0930c3ca24a96,"+
//				"0x9dcc4749b13b4719579401f7acc839b888930ebd,"+
//				"0x56472f6e6683295918b5614f4d92939a5bac8e68,"+
//				"0x3a500ea2dafa6647b5d8e4d7149eec74f165f2bc,"+
//				"0xfd5b4eae0f9160ca2a6db8d945f607c65aee1acf,"+
//				"0xab872159B3776f48adE41E3893d65e5E217a4177,"+
//				"0x9682eca0cC46497F4496186263f4897954969c36,"+
//				"0x735F63925f9E8eB23424db9cf69aDe853dCE9D97,"+
//				"0x5B0f878Fd377aa3Cc3e75cFa71aB670e07b3f494,"+
//				"0x9086Ccc518E98523B59e952C2cA52C242D316382,"+
//				"0xf31e673f06b1fe1bd41997f67bf24e28873090f8,"+
//				"0x539128dd398867bec9d90c35db405b212ae1f0ad,"+
//				"0xa4e7a3cbc31c888b1c4b9a173ad7ef98540f4c99,"+
//				"0xfe305e1eb25860ae6c3b078a6b86c09369d2e543,"+
//				"0xcf68dba19328a2c74993b4174cfbc9b95358a036,"+
//				"0x6810d64777cd3c6152e5b114217b4abeb843b410,"+
//				"0x22f874b9f7becaeb1c961829364b6c4705a4de95,"+
//				"0x9eb50295bd1c1ccab1be860e373d444c30815686,"+
//				"0xca7a1b96b61d4e4812ab7ad5c8dc96e69f17a016,"+
//				"0xc5620c58f6578b2eb8f4c7e156b311c4eeb32be6,"+
//				"0x88f351644dca8760eb7c564333e9a6e655e6851d,"+
//				"0xd2aea1de91c3d1648b9c018884cde8bae1a0c31f,"+
//				"0xd8e299d45e45959e35b5933ab25624c0f32b9026,"+
//				"0x432c75e4fc4b8aef97e68914a42ee69e65965788,"+
//				"0x8af8750d588fb871c1d40c915c5317419e05248e,"+
//				"0xd29ee8c5921114a55f0845d3a6e4030f9890ca92,"+
//				"0x9deb6ad4685e7b8ae2fe0df11f2d17ff9d5340c4,"+
//				"0x1a9d3437ec4f4edeb1945f5730f66ab59026a84c,"+
//				"0xeacf327f584deee4eb43ba3789940e4ecb0276c0,"+
//				"0xc35aB19d1156FB3004454b312f6816Aa941cd411,"+
//				"0xe876C5c0BCF45A53EdA4948a129c4742bA534c13,"+
//				"0x5E00f04dCa0b68340282Ee186ca7BBD2D742e823,"+
//				"0xb124e66a39F26620c7A7b36fdf929f560dd679F8,"+
//				"0x6FBd9A9D246e15e1FEa34537DdF6A3E53F65CC46,"+
//				"0x79a599fAEF0eDB15519a597c09E7aB507af8c402,"+
//				"0xa453490AF6c209616ad237520eAECfFdA2C48fCA,"+
//				"0x3757f4b430470beCFe4AbB49F33358aABF706e1c,"+
//				"0x014536044b40f058E98929052f9ba4D36F334358,"+
//				"0xF5f04f4C482A57c96D9946f0D27B3e98a4899156,"+
//				"0x813bA8A55787b779fc2a434D99505Ad08c7a1085,"+
//				"0x2a28a2886F79f9D361aAC835AC08FFcf906F4297,"+
//				"0x2512BE6F82605d8Db1C2D9677e14d8D9D41b8540,"+
//				"0xff58198B819f6136eB134AaE718b9afFDD94CB2d,"+
//				"0xe0D2433488FC651ecD3c3CA3a48FD42646f4b088,"+
//				"0xc562f66047446beEd73D8f90274C7cD25261d472,"+
//				"0x2Ece565286772465d1c9e6CFA421f5A00d9C978A,"+
//				"0x5B85d1BA8094CDB2bA9C9Ffc2F2bB347F2d2131d,"+
//				"0x02477cc572E3104E2c78d6559C8319AE210c19A0,"+
//				"0xdae3869e9b059cea300a50753172ef57814b32d1,"+
//				"0x09ef878b99bc78a18b56cd8399caeb9f2c2b435c,"+
//				"0x5A13e82CB2D76579598B950144BdE03EDa0b3e20,"+
//				"0x9bCA09e6623960ccF7464c36b48713a6a1E0BC66,"+
//				"0x0f6d6F2fAD8c9b421b73DdaE1217C5bF81a3282a,"+
//				"0x541677f0dddc786b25e1485764a65280bfc4b42d,"+
//				"0x459fa639a7c4e45d86da12b847842406190b7d06,"+
//				"0x4286469f65e4ab040047da2d2485fb1be1fb60a7,"+
//				"0xd6a6a853fc80da7de30caddee06b4808f718c1b2,"+
//				"0xbec2ad707943cb63acfc06880fffe881f9225538,"+
//				"0xaa0faf8e82f201005170cc36a79ae87cb3bf4776,"+
//				"0xd5da8648a6213b83c1613eb3a9fa701da9aa0518,"+
//				"0x728ca0a7d22998a83ef20e829a179ebb4330ef4a,"+
//				"0x45d6ea355c4ba017038a5a3a8ce339cab5dd0ef1,"+
//				"0xc716ee7115b1886a23524cf9d59c1a0651b28338,"+
//				"0x3cdbc1c6f2512be9ca86890bfc233731206e54ee,"+
//				"0xa9eca9a3f04cc292411c77058b9f39fc1d6a5dcd,"+
//				"0xcf0e60dbd2093b8494cff2196ac465acb9990a35,"+
//				"0x47c5a8bd41ada5944bd6c7b48c61fcee43567344,"+
//				"0xd152504d918a749ead31a55f1526b66b489bf6f0,"+
//				"0x988bde5b7512976040f87e8a664e37535e358781,"+
//				"0xa53839e66d5d73b7dacc57d0a72e97f332ec9a49,"+
//				"0x90e5fc8a23be442044515579dfa0736b8bd57bd9,"+
//				"0xb0db1983adb24d630e9f4fbedf82f7b8d671f7b2,"+
//				"0xe639bb1dcd1d6455a98e4c59a0fb2334f57cd94c,"+
//				"0x77461c4caf0d4d8141ae416061db040c2ea3cbc7,"+
//				"0x5097dbdf0fee22ccf8b16226a4dbe45825fab96d,"+
//				"0x780dd32f43225ccad6278b8812bcaa6ef8080674,"+
//				"0xb38aadf13529c83093402c5acdda78ef7b887c41,"+
//				"0x53bbD9AB257524E3061887184170F125a65ECEf5,"+
//				"0x678A9536232ea7E65d2B6aBE0D3bF8B5996DCAC0,"+
//				"0x6421bA1Dab811E763c2543075bFd00C14f3D282C,"+
//				"0x343887231Bc5261a007BB75132Ed102BE6FFEb3c,"+
//				"0x0cDE7d5475333c590E3cd6528E20f558aB24ae76,"+
//				"0x1B11b41C4E6f12688B48522Ba6A6CE7Bd43e20a2,"+
//				"0xDbD2B0d8df6A7551172bE3fbed8f510F28888a9d,"+
//				"0x3ed7842A416b9d83FB9a01AcD7d6c319f9C1aE25,"+
//				"0xE6aee1B7E5CbD472C27a07b1a864928aaFB6De91,"+
//				"0x55d5dD0860607a3a00Ce8EEb0dF925c05738F787,"+
//				"0x867bd685771569ea387ADDd27fB0Da5ABED49ab8,"+
//				"0x144Ef0Cc699cbb3B096100Ed9B2fc00595f50EA2,"+
//				"0x9075effe0552eDF1CFB8Edf36b0fc35c59e50f49,"+
//				"0xe76A400961BfBfFec80579f6D10EADF2952499B4,"+
//				"0x2A9AFC21CB278E4b2c83c43085F61A211b82C5c1,"+
//				"0xa1982141a76cfe51c3e633b07905054e3877b934,"+
//				"0x431e4815Ec900CF063be28C497Cb7B043b611625,"+
//				"0xbf1EA4cEAF2b196e1942d77724269D2ccf547cC4,"+
//				"0x281baFdA19fBB0D96FBE9f51607A9993cCf84dd7,"+
//				"0x062f4936Edf0d502f294b5D234c57E980B6C83fF,"+
//				"0x6fe09b4e2342934f2d9174bdfa456afd26169903,"+
//				"0x22177032de809878813c1a3fcfd9d113f0fb058e,"+
//				"0xd8b5571c75c51eff9455c7da4c4bde612b976e77,"+
//				"0x7df8a3f825dad8f7214926e71fff3780edd475ad,"+
//				"0x7b08ade5378e78624bd5cbc8db328e5abdd94f33,"+
//				"0x65f42cdda1abe10247e51b1c6807cfe258372d2e,"+
//				"0x1eA12CcF3E7192C42391ab2E833F02AB0377C12B,"+
//				"0x85585c32A6ad22Ef89269D10cabe8292194825De,"+
//				"0x229520576935C760894F9a4E57C9C1f8557653C1,"+
//				"0x37ef8D2df3099643945777dE02ffdEc3CBBb1d34,"+
//				"0x3c6e946134DB5DbA89eC88613349E1d8Dc5C7B98,"+
//				"0xdae3869e9b059cea300a50753172ef57814b32d1,"+
//				"0x09ef878b99bc78a18b56cd8399caeb9f2c2b435c,"+
//				"0xd88a0ff3a16d05015a3de8365dc1bfce754ee5ca,"+
//				"0xd93cbbc82c007135e4d29eeb7a991080e9fecb8b,"+
//				"0xf97206a76528446ab0f878e0625bb428454dd87f,"+
//				"0xf5ac12bda3ac3f1db68b6ed938d7b9c11dc745ee,"+
//				"0x2a8286e36942a12d823c43437fc530e071015b22,"+
//				"0x9d9c1723b28ce6ad0db9d487b7d2cf9c03ab9a74,"+
//				"0xa2ce609a29ae3f5a26adb756e0087f182cd41306,"+
//				"0x0d3b6c883b16f2fb7bc90fbe9ed7744d2fec8e9a,"+
//				"0x746b6684843e8f5fb6b02b8215c6210b90f0815f,"+
//				"0x55B3f2053614CCC32124d90Eb44Aaa9660573cd3,"+
//				"0x265e1286188C6B3c92B8E69ed896b450D3aAB503,"+
//				"0x74CaD6860Bcf04eFb40359C7d1b8FE48f5FFEFee,"+
//				"0x18840afB057FC83577FdbDcD4EE379a35951017f,"+
//				"0x7806A28fE524Ef3B0fe94f957722677CAa7EeB1E,"+
//				"0x19Fb2866865921317287a6839fcb19a3bda1c6e5,"+
//				"0xb80e42c4c147589408f2297abf2475c7913c4775,"+
//				"0x99895e446c41570a6627fdee41d6290ccb777470,"+
//				"0xaa4d9597e9bc60789063455a93b65695cfcd386a,"+
//				"0x937ba06be472925fe2a1099d8ef94a338e7c17b5,"+
//				"0x3fa5aab43cce9f77e9cad6bd8753f376a852a7ef,"+
//				"0xb266acda823c8ad46fecf0c27f7a10aa860e6c32,"+
//				"0x54474191fb1a55c9c08ca0cd07763bcb8b4cf150,"+
//				"0x89577503c6d7651e33d9a83f1f2fbb9043a5859f,"+
//				"0x4a64c704aadb3eb5ab559c3d871cb6ff64135f5d,"+
//				"0x2ca20d50eb83a53980fa5debd31d65b2e273a6f3,"+
//				"0x46926bf70b2a4d7d042a77ead6735c25596f5c1b,"+
//				"0x18a63dcdca6e10e2fa1d101fac0d6aa9f5421eed,"+
//				"0x4fedc1b2144f1f70c0f52b8886c162bcedf2c6ac,"+
//				"0xf4836be0df58fa5940e512588f37638b2872d384,"+
//				"0x7755286e2d687a96ea94f270916e6fb9e0be5d90,"+
//				"0xe1b176e713283c9235c46d6091247d4bf912256b,"+
//				"0x764fe4d2fce4c7602b070c0702c2d499f08e8e54,"+
//				"0x47d089f3dc9f99d52bb2f3d0a8bde8c18d6e09f9,"+
//				"0xbd1f7e3c2a0fdc8b66b17914c8c27a02873ad27f,"+
//				"0xb761d59ae6efb8b46dccc7c6b5ec1f3a6957bc44,"+
//				"0x215197f9478f6e210549f80b220c5aac522c7b22,"+
//				"0x8c24596ea121b041b8e68fb3076d73e8f86eaddd,"+
//				"0x68d2f7992b0fb6f89a28b426437fcb55f4935bab,"+
//				"0xa4b96e577c95415ff48343bc5c46920cfebfeb65,"+
//				"0x8d9be3e6cf77bf97123af7179c79934fd192a433,"+
//				"0xaed45854bf4bfc6d05a743abdfd2402884fd897b,"+
//				"0xe0672f76fd8b39e68660de00f6519bef3547e0e9,"+
//				"0xccaeb71f7c79f8a0425c54e7881556b601360559,"+
//				"0xaa5cc52a82eb8d588b7b112a5840ec453d9728c9,"+
//				"0x5fceb0c35ef2458e0d3e38bf07080e352f4b1a9e,"+
//				"0xf8301b074fba6da563c7017d796b4fcb12e2642d,"+
//				"0x648b1614fb2e26a8f61ecc8d5fb7307df48e86db,"+
//				"0x66455b3aeb69a540d391539ec9ce543ba79ee28d,"+
//				"0xd06ed28991904c97e4b3632bdeace2a889c2a7a9,"+
//				"0x32def249b074ca01a979c3d19c7b83bb5468fb0f,"+
//				"0x2389151d8e4ceb3a4b15a0270646a249b7395d88,"+
//				"0x9c7156cc605151ef52c79084b0d306dd9406811e,"+
//				"0x79c51717ad4091fa8fea412d77d002cb9d87651b,"+
//				"0x7720ee650c136270129d9d532a9c3583675128ab,"+
//				"0xcb6b83a7627ff81d1ce76c1cf63a091f07002e2d,"+
//				"0x04a7bd9d7ec4a9eec51bfd8d5e9a4b8d218798e9,"+
//				"0x1b1a324f3006fce3f70c23a7e355b725619e7445,"+
//				"0x84aa33caa2cffb18c9cd3d69b1e795f1711b980b,"+
//				"0xd4b3111f904ff7bebee8788ee47c2b36ee6f6fa1,"+
//				"0xcd7f31b28445bbf1d4cdca23e75e8914fd4c0801,"+
//				"0x8a1749b308c835bf84ea693e33f27762cf1de2a1,"+
//				"0xa2a7879e37ac34f2c363eb36bd539ac9ee4f2dbf,"+
//				"0xf31ef88dfcb1b457d8dc6fff7013fb7cf82ab48a,"+
//				"0xf20e96dbe07c3895811211c73fdd586852b44a49,"+
//				"0xd2c1a47bc833c0471c8ba5c2cb374e2f68a6bc3f,"+
//				"0xa8fdd65faa857ebd81aae6da03c0a8ec39a0c613,"+
//				"0xe985679a1ae3c4607011c32a9a55d15c18ad53ca,"+
//				"0x16e58f732a8f3e783768e1674048ea561d415faa,"+
//				"0x1ada4f3baa8e4bee4b583fb3a9d96f85bbb98d79,"+
//				"0xa4979e70a8202b33f89b7baa6b5bdc13505ddb78,"+
//				"0xadcf20732eedb620031e7ac1cd141b093e7a4c5e,"+
//				"0x49703b5c81eaff58d58582a643574eb19e0edaeb,"+
//				"0x1c0a9a130e5e3a15af8f8aabf5bfa2800abd34f5,"+
//				"0x0120b94d6ebf1b135ebfe3de407e6b352bafc6af,"+
//				"0x3339d848f2b0574c770d52f5e707c82993a14dc7,"+
//				"0x95ba05aa2feda8a40faf3d7cc9c2f022826dc9fb,"+
//				"0x92c518ee60033689f01af2aaca3d5a5c599993c7,"+
//				"0xa013208d736e017bfeb1adabcf52947951272a95,"+
//				"0xabad625da334d4df1d1a02d431181235e3da6d63,"+
//				"0xa3b36440e07f870555bb9bb6d435e0b81cc07f90,"+
//				"0xec19de843d85d9694b4f08ab8107d896bc791844,"+
//				"0xd1455c2519db72ff675b82609d4574c0b04aafe3,"+
//				"0x9f030b185182c0abbb9466020065a5c4aa5ba7a9,"+
//				"0x8297f74868c0b48f40edc8ce9b76673e07b40488,"+
//				"0x0a939ffdedb19097c809dc19077c22fd9da6c954,"+
//				"0x8fc219bf00c9ccaf6989021248c02e8f54411214,"+
//				"0xf5848824938882a6e486f4bc70ca81d9d5c0a4d3,"+
//				"0x937873c668bdd5e58e33306df116b4308dbd5580,"+
//				"0x4959b877e6cb5b359da201f973207afcf24b38d5,"+
//				"0xf9f35860de2375a9c66873e1578cdec4a5b10c94,"+
//				"0x14202edd3860ae8682c0546bed50316f39558fdd,"+
//				"0xc3e7b3baf6237dbb21f0f2633c2766fd00ae2b0b,"+
//				"0x679ecab266ef69afd34e125fe4b527f9d11769aa,"+
//				"0x2b6d9d82F7E7C29c51AfdBA9a5603B9FE440f91b,"+
//				"0xbd35Aa1611bF7C69b18C2415c89c877cD4Bd42c4,"+
//				"0x739E8b0a8Dd2E99072F5538B5facfAc6B74F98Bf,"+
//				"0xa8DE001Ce3647bD2609d401A2F05Ae89FBD43a68,"+
//				"0xE9518fd81F0e573221E3529eA79258B60cE3C18b,"+
//				"0x027B56eA26C50A4ef986Ffb148C96015652e5DcA,"+
//				"0x0F3d75d106c0e33da38d65B3826B8ecE5607c47E,"+
//				"0xFbdD736ec58d57fc00029f6fD1BA7C9d9f976774,"+
//				"0x777495F52cFAE33a48E18dB1AA7C9c1655446486,"+
//				"0x016A5E0c2C3244Dd3cAB8A3733EaD3F2a4572e62,"+
//				"0x1af9E4a005f50701a9a9341dd5F7C80D2358F0C6,"+
//				"0xC0f6bA60479c4940B8Aefbd9edA3bCece62867b4,"+
//				"0xF125385A7b70a7cF40E3c6e2f8283F7Eb115Cfea,"+
//				"0x0edc413B705cB20037D8e501a9202D65e802DAe4,"+
//				"0x06b87dB53e1089f41850b8EBc2Aa92904b0295a3,"+
//				"0x7588046158a93d5d1d5b8b4c73f97dfcf0f6836a,"+
//				"0x6a38de4d2c247ae8e11e220f512f9cdf5ef3eab6,"+
//				"0x1c5a880426c8515467eef7724fbbe2e2c76d7da8,"+
//				"0xc2cb7bc86f91e2987e1f4bf054de5deb5a2595a9,"+
//				"0x0ed54399ed714fe890381af64f024427158e4065,"+
//				"0x0c9ddf18c0b89a142ef412dab58e888bc67498ff,"+
//				"0x7d6fe086b1bb0c4c69e09f9d4055a21b9d7c7813,"+
//				"0xf0e875c46595608b7ffe101a43b8134364444910,"+
//				"0xbb5ae45f67fbf8ca41bc3cb6bc8a0fdb29cdc2b5,"+
//				"0xc421196e09a05c9346fe4b6d73eed66074c49b58,"+
//				"0x5b79d0d4002a43f318b24d6ca532baaca04db0c6,"+
//				"0xe76e2e7f4ef8a74d3938b10508d0883323c7a2b0,"+
//				"0x62581e14dd6d356def28e743818edb1c69dd4b9e,"+
//				"0x0179cb1299007a5632d7aff66603df8e07afdabf,"+
//				"0x34d92b3d6419037beb3fdf79c944e598852b0bc5,"+
//				"0x6c0ef73bda93d8d05584380f379994aa460aa519,"+
//				"0x7844a997c8f3d13f706e9b259e741653dd824afc,"+
//				"0x500c69e0da67bd952cb1ab3f123a4200e6b044ec,"+
//				"0xa4b5f28f1f1d4f57b65c28fb68007bcf136a7171,"+
//				"0xa33f5d3fd12fb23aa4ebbe3922111e8a4fed5779,"+
//				"0x7dbd84caa7e147068e660b26def3e9ed4c7245bc,"+
//				"0xe36015855e72e6c8c0a104fa83e7827f8a693bd5,"+
//				"0x1f402e956c45a2470616aa3dc9f49785e35cd403,"+
//				"0x951c1d675d8a7ffc8fb1712556f926da75828e5d,"+
//				"0x28c9ce9596f1e4398e8443ec841a7430611a8f2b,"+
//				"0xe21d041c917a4bd4be40a6891a09256f0ccc7143,"+
//				"0xa64f33624f1269d44349f385e7eb40afae27270f,"+
//				"0xe9fa3a0886bbe7656660ccce7460f720aa1ec26d,"+
//				"0xba0f3943cc5a0fc7f289e5b49faba68812e27872,"+
//				"0xe3aea9a2d6c04fd43fe81217867324673e1ae34c,"+
//				"0xcc8980e4386ca99b031e0aa12fa88944d1d9881f,"+
//				"0x4680381e658017d6209081e4317b7a00cc2c64f3,"+
//				"0x7820fe1d272f3752817c41c4eeccaf8830ce95be,"+
//				"0x4ab457a0ab361d7520399ae9946b7196ed021ed3,"+
//				"0x336ba4507626c21ba4df11a0fee7190ff273cfe1,"+
//				"0xe94b6208a6d0732b60f1b6f6c80632afffbd6cee,"+
//				"0x1b94c0a02dc35d5bc55a50ca8bfbe7fc7d583a52,"+
//				"0x3576b303b41f792f4955a918a0adb1432b48661e,"+
//				"0x420393be12c9c2a5b8ebb1d836d5ed3e2b544c73,"+
//				"0x2e9aa5608da9049a310bf69d33fecc391d4525b3,"+
//				"0x4dffdc34769df578b59ed0522a5a15c663b00b94,"+
//				"0x77fdd8bfc6209eb3f90649b14d702518c89578df,"+
//				"0x6843fd23240e9a247837ccf98e503e93b1b0dd7c,"+
//				"0x6843fd23240e9a247837ccf98e503e93b1b0dd7c,"+
//				"0x508f615a8f8090cd37b403ac20229f394b130db6,"+
//				"0x00a720127212967f7d9e195bd8b45046eb35c76f,"+
//				"0xc3971ae3462a66478077cce5050c41a385d8c14b,"+
//				"0xfc95f0b22fbae90cb0b4217a969cc6631cfdd3ad,"+
//				"0xe296fc092152b794e177559d9e2135a51e5fe20e,"+
//				"0x7242ecbc7e4a659ec68f3a38f3ef0e109997190f,"+
//				"0x80c35a5f7dc98b8d93e855eea315cac3c3f43186,"+
//				"0x2cc4cb6b349a664671f3a5d2d0916da8624415f5,"+
//				"0xdaa592bedbc7d771702df7e94c3d12b4a1cafa1b,"+
//				"0xf1e6d5a68473a05d4c69faedce92f2385d1227e8,"+
//				"0xbf67cbb809dc1f34f7ae5b94d6b10f40ec42effe,"+
//				"0x2c5d465362cf2ccf21acaed2134425d43ca697a1,"+
//				"0x5fba9d72f660f349954851358ebaa66c93160fdb,"+
//				"0x82477f724c068cfb8fda08236d72d40ac9c4afa5,"+
//				"0x82477f724c068cfb8fda08236d72d40ac9c4afa5,"+
//				"0xa3a5eb0f6879d1be7847af14bcc8dbb1aa1fdc55,"+
//				"0xdd245ca4d9f28a5627aeaa43b2f645f8ffcadef9,"+
//				"0x817f392fd5cb5a6fbd1c03dc026069578527d210,"+
//				"0xdfe9ec320cf7ac05fec06333cdf02416e13e9d8f,"+
//				"0xf3bf56d896a4c40debdc631d38eb79a7e1b7c346,"+
//				"0xcb0c9beacc070e04427ae1ec7dfeaa9485451d43,"+
//				"0x094db4b1b75dbbc0d10466a104360d2b1bec352c,"+
//				"0x3ca9a67f5f15edcc07a9d08189050ba82a8e9a24,"+
//				"0xee3e57a4c762d52a72042a2770537bf3a48620cf,"+
//				"0x85ae9d7772360f9ef202699b752887dcd5bd49d9,"+
//				"0x02587c0387fc9f52041a909086314d50612dfa59,"+
//				"0x94439abf2f8018573a58380e224b82d184ce8fef,"+
//				"0xd8c7f56f518e210aea88b7416449e25aedbf9376,"+
//				"0x1f3a0c86b7728702a4e26c66aac4369062ad3681,"+
//				"0x31f585e920f62670fbdb4bfcd09143835a0323d7,"+
//				"0x0497b939acd11513fd2e6be0b2b84d0afdc8d54c,"+
//				"0xd5e1e714057d12f5327f2ce17a178d146705c34e,"+
//				"0x7f245100b334f5212843e1832e506304ff845c3e,"+
//				"0x12b31420b2d108f36e08d46bbcdee2df2cb284ac,"+
//				"0x905c854e4c6e4174f7599fa5f69f68f49e5a1ba6,"+
//				"0x51121e7861a3f1d2497877500df755f7b378fe1b,"+
//				"0x9dc8f5e1f35afe0e4fe528abf5d462c188075221,"+
//				"0xcadaa54ce36af6b944b0f85b74d3931a32d7de6c,"+
//				"0xf199ea7cbb2527f8f8bad842cbcf73ba9fcfe68d,"+
//				"0x0ad0c34e6f972722c636db1161e63b601ed300f1,"+
//				"0xdff8b3c4fa723d608d0868b51ec0be8f6afb535f,"+
//				"0x69427d43d354ef1d9e1ebf72da3fa0df972ce65a,"+
//				"0xe7c1f2bfc27fa0721c9359eb4d0d74eedb0746f9,"+
//				"0x34d695f60de8ea845c6adf03f57d78ce6f3efbbe,"+
//				"0x970c00e5d8006de06310e29dd02fb24c26709c7d,"+
//				"0xbe45cb9d6109c83d2fe38e557a36c0e07100af15,"+
//				"0xb99fb9db4a9e66555e5daea18cbec86d974f4ca0,"+
//				"0xc49c54335825522604369c9a4f398d41a6e1017b,"+
//				"0xc1ef4263938084326f21610872aa3244433f320f,"+
//				"0x462f91b0244370590f0261f91c5deddcb07e74e3,"+
//				"0x2bc4d2e4e923bcc1aade41904428a36d084e9339,"+
//				"0x2596735c96c592c6b1552552b5d39b2e85613b03,"+
//				"0x59f259d5fafbb20d1ff56efcfbd97bfbb7ce0d97,"+
//				"0x8987dd7cf645ddf90af8da5b19eccf686372cb4e,"+
//				"0x66cddcf018162541038bdc70ae2c6457b87c0d30,"+
//				"0xec49a75fd7919816a7ccb5e84429cbae2749da5f,"+
//				"0x4b2b21d9c9cc55dc9c1b20ab9af8e5acf72e014d,"+
//				"0xa2b41db8581ee31f1429c5009e43d9dee90ad06e,"+
//				"0xbebef47afdc680a4959129f70c638d749fa9ea3a,"+
//				"0x507d1031474ca2d89ffca1aee152a46d2650188c,"+
//				"0x210b76b6cba4db5836ba08dba0a7716721d53c4e,"+
//				"0x6859f2aad8adc50def60d7c2dcf2521c34c2a9d2,"+
//				"0xc1d7a7a4ae2d5d7b31bdd787f4cb528e46f71995,"+
//				"0x3eebb323e346d95197710a57006fab35badbcf58,"+
//				"0xca802230a245537f980ab918c35d29954711eafb";
//		String addresse = "0x8eec28e561c2dcad23e79add0946c27087fedfb0,0x66cddcf018162541038bdc70ae2c6457b87c0d30";
//		String[] list = addresse.split(",");
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
			String temp = "0x60ceca9c1290ee56b98d4e160ef0453f7c40d219";
			for(String address: list) {
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
