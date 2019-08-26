package com.platon.browser.data;

import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.platon.BaseResponse;
import org.web3j.platon.StakingAmountType;
import org.web3j.platon.bean.Node;
import org.web3j.platon.bean.ProgramVersion;
import org.web3j.platon.bean.StakingParam;
import org.web3j.platon.contracts.DelegateContract;
import org.web3j.platon.contracts.NodeContract;
import org.web3j.platon.contracts.RestrictingPlanContract;
import org.web3j.platon.contracts.StakingContract;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.PlatonSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ChainId;
import org.web3j.tx.Transfer;
import org.web3j.tx.gas.DefaultWasmGasProvider;
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;

import com.platon.browser.exception.IssueEpochChangeException;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/15 20:58
 * @Description:
 */
public class TransactionSender {
	private static String chainId = "100";
    private static Logger logger = LoggerFactory.getLogger(TransactionSender.class);
    private Web3j currentValidWeb3j = Web3j.build(new HttpService("http://192.168.112.172:8789"));
//    private Web3j currentValidWeb3j = Web3j.build(new HttpService("http://192.168.120.76:6797"));
//    private Credentials credentials = Credentials.create("00e6bd52b0015d9767c2308f4e75083aa455dd345a936a1c48abaee5795db51ccb");
    private Credentials credentials = Credentials.create("00a56f68ca7aa51c24916b9fff027708f856650f9ff36cc3c8da308040ebcc7867");
    NodeContract nodeContract = NodeContract.load(currentValidWeb3j);
    StakingContract stakingContract = StakingContract.load(currentValidWeb3j,credentials,new DefaultWasmGasProvider(),chainId);
    DelegateContract delegateContract = DelegateContract.load(currentValidWeb3j,credentials,new DefaultWasmGasProvider(),chainId);
    public TransactionSender() throws IOException, CipherException {}

    // 发送转账交易
    @Test
    public void transfer() throws Exception {
    	for(int i=0;i<30;i++) {
	        Transfer.sendFunds(
	                currentValidWeb3j,
	                credentials,
	                chainId,
	                "0x8b77ac9fabb6fe247ee91ca07ea4f62c6761e79b",
	                BigDecimal.valueOf(100),
	                Convert.Unit.VON
	        ).send();
	        BigInteger balance = currentValidWeb3j.platonGetBalance("0x8b77ac9fabb6fe247ee91ca07ea4f62c6761e79b", DefaultBlockParameterName.LATEST).send().getBalance();
	        logger.debug("balance:{}",balance);
    	}
    }

    // 发送质押交易
    @Test
    public void staking() throws Exception {
    	String externalId = "";
        String nodeName = "chendai-node1";
        String webSite = "www.baidu.com";
        String details = "chendai-node1-details";
        BigDecimal stakingAmount = Convert.toVon("5000000", Unit.LAT).add(BigDecimal.valueOf(1L));
        
        PlatonSendTransaction platonSendTransaction = stakingContract.stakingReturnTransaction(new StakingParam.Builder()
               .setNodeId("0x0aa9805681d8f77c05f317efc141c97d5adb511ffb51f5a251d2d7a4a3a96d9a12adf39f06b702f0ccdff9eddc1790eb272dca31b0c47751d49b5931c58701e7")
               .setAmount(stakingAmount.toBigInteger())  
               .setStakingAmountType(StakingAmountType.FREE_AMOUNT_TYPE)
               .setBenifitAddress("0x60ceca9c1290ee56b98d4e160ef0453f7c40d219")
               .setExternalId(externalId)
               .setNodeName(nodeName)
               .setWebSite(webSite)
               .setDetails(details)
               .setBlsPubKey("5c5fa09a6caad18b7bd35c3a8991948763df4d7ea11070295b32c0f7d499d041f8645dab84537d85d95811c34faebea42ef4066b3dda6920b5f35e8a163506b7")
               .build()).send();
        BaseResponse baseResponse = stakingContract.getStakingResult(platonSendTransaction).send();
        System.out.println(baseResponse.toString());
        logger.debug("res:{}",baseResponse);
    }

    // 修改质押信息(编辑验证人)
    @Test
    public void updateStakingInfo() throws Exception {
        BaseResponse res = stakingContract.updateStakingInfo(
                "0x0aa9805681d8f77c05f317efc141c97d5adb511ffb51f5a251d2d7a4a3a96d9a12adf39f06b702f0ccdff9eddc1790eb272dca31b0c47751d49b5931c58701e7",
                "0x60ceca9c1290ee56b98d4e160ef0453f7c40d219",
                "PID-002","cdm-004","WWW.CCC.COM","Node of CDM"
        ).send();
        logger.debug("res:{}",res);
    }

    // 增持质押(增加自有质押)
    @Test
    public void addStaking() throws Exception {
        BaseResponse res = stakingContract.addStaking(
                "0x0aa9805681d8f77c05f317efc141c97d5adb511ffb51f5a251d2d7a4a3a96d9a12adf39f06b702f0ccdff9eddc1790eb272dca31b0c47751d49b5931c58701e7",
                StakingAmountType.FREE_AMOUNT_TYPE,
                BigInteger.valueOf(555)
        ).send();
        logger.debug("res:{}",res);
    }

    // 撤销质押(退出验证人)
    @Test
    public void unStaking() throws Exception {
        BaseResponse res = stakingContract.unStaking(
                "0x0aa9805681d8f77c05f317efc141c97d5adb511ffb51f5a251d2d7a4a3a96d9a12adf39f06b702f0ccdff9eddc1790eb272dca31b0c47751d49b5931c58701e7"
        ).send();
        logger.debug("res:{}",res);
    }

    // 发送委托交易
    @Test
    public void delegate() throws Exception {
        BaseResponse res = delegateContract.delegate(
                "0x15245d4dceeb7552b52d70e56c53fc86aa030eab6b7b325e430179902884fca3d684b0e896ea421864a160e9c18418e4561e9a72f911e2511c29204a857de71a",
                StakingAmountType.FREE_AMOUNT_TYPE,
                BigInteger.valueOf(1000)
        ).send();
        logger.debug("res:{}",res);
    }
    
    
    public static void main(String[] args) throws IOException, CipherException {
    	Credentials credentials = WalletUtils.loadCredentials("88888888", "F:\\文件\\矩真文件\\区块链\\PlatScan\\fd9d508df262a1c968e0d6c757ab08b96d741f4b_88888888.json");
    	byte[] byteArray = credentials.getEcKeyPair().getPrivateKey().toByteArray();
        String privateKey = Hex.toHexString(byteArray);

        logger.debug("Private Key:{}",privateKey);
	}
    
    

    // 发送解委托交易
    @Test
    public void unDelegate() throws Exception {
        BaseResponse res = delegateContract.unDelegate(
                "0x00cc251cf6bf3ea53a748971a223f5676225ee4380b65c7889a2b491e1551d45fe9fcc19c6af54dcf0d5323b5aa8ee1d919791695082bae1f86dd282dba41000",
                BigInteger.valueOf(53086),
                BigInteger.valueOf(1000)
        ).send();
        logger.debug("res:{}",res);
    }

    @Test
    public void getBlockNumber() throws IssueEpochChangeException, IOException {
        Web3j web3j = Web3j.build(new HttpService("http://192.168.120.76:6797"));
        long blockNum = 11777;
        /*while (true){
            try {
                PlatonBlock.Block block = web3j.platonGetBlockByNumber(DefaultBlockParameter.valueOf(BigInteger.valueOf(blockNum)),true).send().getBlock();
                if(block==null){
                    logger.error("loss:{}",blockNum);
                }
            } catch (IOException e) {
                logger.error("loss:{}",blockNum);
                e.printStackTrace();
            }

            ++blockNum;
        }*/


        /*Integer[] numbers = {11777,11829,11870,11889,11900,11939};
        for (Integer blockNum:numbers){
            try {
                PlatonBlock.Block block = web3j.platonGetBlockByNumber(DefaultBlockParameter.valueOf(BigInteger.valueOf(blockNum)),true).send().getBlock();
                if(block==null){
                    logger.error("loss:{}",blockNum);
                }
            } catch (IOException e) {
                logger.error("loss:{}",blockNum);
                e.printStackTrace();
            }
        }*/

        /*String incentivePoolAccountAddr = "0x1000000000000000000000000000000000000003";
        try {
            // 根据激励池地址查询前一增发周期末激励池账户余额：查询前一增发周期末块高时的激励池账户余额
            BigInteger incentivePoolAccountBalance = web3j.platonGetBalance(incentivePoolAccountAddr, DefaultBlockParameter
                    .valueOf(BigInteger.valueOf(1))).send().getBalance();
            logger.debug("激励池账户余额:{}",incentivePoolAccountBalance.toString());
            // 计算当前增发周期内的每个结算周期的质押奖励
            BigDecimal settleReward = new BigDecimal(incentivePoolAccountBalance.toString())
                    .multiply(BigDecimal.valueOf(0.5)) // 取出激励池余额中属于质押奖励的部分
                    .divide(BigDecimal.valueOf(1680/240),0, RoundingMode.FLOOR); // 除以结算周期轮数，精度取16位小数
            logger.debug("当前结算周期奖励:{}",settleReward.longValue());

            BigDecimal blockReward = new BigDecimal(incentivePoolAccountBalance)
                    .multiply(BigDecimal.valueOf(0.5)) // 取出激励池余额中属于区块奖励的部分
                    .divide(BigDecimal.valueOf(1680),0, RoundingMode.FLOOR); // 除以一个增发周期的总区块数，精度取10位小数
            logger.debug("当前区块奖励:{}",blockReward.longValue());
        } catch (IOException e) {
            throw new IssueEpochChangeException("查询激励池(addr="+incentivePoolAccountAddr+")在块号("+1+")的账户余额失败:"+e.getMessage());

        }*/

        BigInteger blockNumber = web3j.platonBlockNumber().send().getBlockNumber();
        logger.error("{}",blockNumber);
    }
}
