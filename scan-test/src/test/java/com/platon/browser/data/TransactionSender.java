package com.platon.browser.data;

import com.platon.contracts.ppos.DelegateContract;
import com.platon.contracts.ppos.NodeContract;
import com.platon.contracts.ppos.ProposalContract;
import com.platon.contracts.ppos.RewardContract;
import com.platon.contracts.ppos.StakingContract;
import com.platon.contracts.ppos.dto.BaseResponse;
import com.platon.contracts.ppos.dto.CallResponse;
import com.platon.contracts.ppos.dto.TransactionResponse;
import com.platon.contracts.ppos.dto.common.ProposalType;
import com.platon.contracts.ppos.dto.enums.StakingAmountType;
import com.platon.contracts.ppos.dto.req.StakingParam;
import com.platon.contracts.ppos.dto.req.UpdateStakingParam;
import com.platon.contracts.ppos.dto.resp.Node;
import com.platon.contracts.ppos.dto.resp.Proposal;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.platon.crypto.CipherException;
import com.platon.crypto.Credentials;
import com.platon.protocol.Web3j;
import com.platon.protocol.core.DefaultBlockParameterName;
import com.platon.protocol.core.methods.response.PlatonBlockNumber;
import com.platon.protocol.core.methods.response.PlatonSendTransaction;
import com.platon.protocol.http.HttpService;
import com.platon.tx.Transfer;
import com.platon.tx.gas.GasProvider;
import com.platon.utils.Convert;
import com.platon.utils.Convert.Unit;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/15 20:58
 * @Description:
 */
public class TransactionSender {
	private static Long chainId = 201018l;
    private static Logger logger = LoggerFactory.getLogger(TransactionSender.class);
    private static Web3j currentValidWeb3j = Web3j.build(new HttpService("http://192.168.112.172:6778"));
//    private Web3j currentValidWeb3j = Web3j.build(new HttpService("http://192.168.112.171:5789"));
//    private Web3j currentValidWeb3j = Web3j.build(new HttpService("http://192.168.120.76:6797"));
    private static  Credentials delegateCredentials = Credentials.create("4484092b68df58d639f11d59738983e2b8b81824f3c0c759edd6773f9adadfe7");
//    private Credentials credentials1 = Credentials.create("00a56f68ca7aa51c24916b9fff027708f856650f9ff36cc3c8da308040ebcc7867");
    private static Credentials credentials = Credentials.create("a689f0879f53710e9e0c1025af410a530d6381eebb5916773195326e123b822b");
    private static NodeContract nodeContract = NodeContract.load(currentValidWeb3j);
    private static StakingContract stakingContract = StakingContract.load(currentValidWeb3j,credentials);
    private static DelegateContract delegateContract = DelegateContract.load(currentValidWeb3j,delegateCredentials);
    private static RewardContract rewardContract = RewardContract.load(currentValidWeb3j,delegateCredentials);
    private static ProposalContract proposalContract = ProposalContract.load(currentValidWeb3j,credentials);
    private String stakingPubKey = "0aa9805681d8f77c05f317efc141c97d5adb511ffb51f5a251d2d7a4a3a96d9a12adf39f06b702f0ccdff9eddc1790eb272dca31b0c47751d49b5931c58701e7";
    private String stakingBlsKey = "b601ed8838a8c02abd9e0a48aba3315d497ffcdde490cf9c4b46de4599135cdd276b45b49e44beb31eea4bfd1f147c0045c987baf45c0addb89f83089886e3b6e1d4443f00dc4be3808de96e1c9f02c060867040867a624085bb38d01bac0107";

//    private String stakingPubKey = "bfc9d6578bab4e510755575e47b7d137fcf0ad0bcf10ed4d023640dfb41b197b9f0d8014e47ecbe4d51f15db514009cbda109ebcf0b7afe06600d6d423bb7fbf";
//    private String stakingBlsKey = "b4713797d296c9fe1749d22eb59b03d9694ab896b71449b0e6daf2d1ecb3a9d3d6e9c258b37acb2d07fa82bcb55ced144fb4b056d6cd192a509859615b090128d6e5686e84df47951e1781625627907054975f76e427da8d32d3f30b9a53e60f";

//    private String stakingPubKey = "4fcc251cf6bf3ea53a748971a223f5676225ee4380b65c7889a2b491e1551d45fe9fcc19c6af54dcf0d5323b5aa8ee1d919791695082bae1f86dd282dba4150f";
//    private String stakingBlsKey = "4b86fc4f865658161d722937c48ac72e1d54d9e8afcbc2c78c5b84fb3ce0510187b5699beb9c91a130075b88cb00b90a2ec8c0c4715d084c7cc5fe734648eb8c935ef4660f7224abd4de28aae55cc25c34144f57a912204bf1eda85892392e13";
    public TransactionSender() throws IOException, CipherException {}

//    @Before
//	public void init() {
//    	HttpService httpService = new HttpService("http://192.168.112.172:8789");
//    	currentValidWeb3j = Web3j.build(httpService);
//    }
    // 发送转账交易
    @Test
    public void transfer() throws Exception {
//    	for( int i=0;i<30;i++) {
			Transfer.sendFunds(
			        currentValidWeb3j,
			        credentials,
			        "lax1vr8v48qjjrh9dwvdfctqauz98a7yp5se77fm2e",
			        BigDecimal.valueOf(1000000000),
			        Unit.KPVON
			).send();
//			Transfer.sendFunds(
//			        currentValidWeb3j,
//			        delegateCredentials,
//			        chainId,
//			        "0xfd9d508df262a1c968e0d6c757ab08b96d741f4b",
//			        BigDecimal.valueOf(10),
//			        Convert.Unit.ATP
//			).sendAsync();
//			Transfer.sendFunds(
//			        currentValidWeb3j,
//			        credentials1,
//			        chainId,
//			        "lax1vr8v48qjjrh9dwvdfctqauz98a7yp5se77fm2e",
//			        BigDecimal.valueOf(10),
//			        Convert.Unit.ATP
//			).sendAsync();
			BigInteger balance = currentValidWeb3j.platonGetBalance("lax1vr8v48qjjrh9dwvdfctqauz98a7yp5se77fm2e", DefaultBlockParameterName.LATEST).send().getBalance();
	        logger.debug("balance:{}",balance);
//    	}
    }

    // 发送质押交易
    @Test
    public void staking() throws Exception {
    	String externalId = "5FD68B690010632B";
        String nodeName = "zrj-node1";
        String webSite = "www.baidu.com";
        String details = "chendai-node1-details";
        BigDecimal stakingAmount = Convert.toVon("20000000", Unit.KPVON);
        PlatonSendTransaction platonSendTransaction = stakingContract.stakingReturnTransaction(new StakingParam.Builder()
               .setNodeId(stakingPubKey)
               .setAmount(stakingAmount.toBigInteger())
               .setStakingAmountType(StakingAmountType.FREE_AMOUNT_TYPE)
               .setBenifitAddress("lax1vr8v48qjjrh9dwvdfctqauz98a7yp5se77fm2e")
               .setExternalId(externalId)
               .setNodeName(nodeName)
               .setWebSite(webSite)
               .setDetails(details)
               .setBlsPubKey(stakingBlsKey)
               .setProcessVersion(currentValidWeb3j.getProgramVersion().send().getAdminProgramVersion())
               .setBlsProof(currentValidWeb3j.getSchnorrNIZKProve().send().getAdminSchnorrNIZKProve())
               .setRewardPer(new BigInteger("100"))
               .build()).send();
        TransactionResponse baseResponse = stakingContract.getTransactionResponse(platonSendTransaction).send();
        System.out.println(baseResponse.toString());
        logger.debug("res:{}",baseResponse);
    }

    // 修改质押信息(编辑验证人)
    @Test
    public void updateStakingInfo() throws Exception {
    	UpdateStakingParam.Builder uBuilder = new UpdateStakingParam.Builder();
    	uBuilder.setBenifitAddress("lax1vr8v48qjjrh9dwvdfctqauz98a7yp5se77fm2e");
    	uBuilder.setDetails("Node of CDM");
    	uBuilder.setExternalId("5FD68B690010632B");
    	uBuilder.setNodeId(stakingPubKey);
    	uBuilder.setNodeName("cdm-004");
    	uBuilder.setWebSite("WWW.CCC.COM");
    	uBuilder.setBenifitAddress("lax1vr8v48qjjrh9dwvdfctqauz98a7yp5se77fm2e");
    	uBuilder.setRewardPer(new BigInteger("500"));
    	UpdateStakingParam updateStakingParam = new UpdateStakingParam(uBuilder);
        TransactionResponse res = stakingContract.updateStakingInfo(updateStakingParam).send();
        logger.debug("res:{}",res);
    }

    // 增持质押(增加自有质押)
    @Test
    public void addStaking() throws Exception {
    	BigDecimal stakingAmount = Convert.toVon("5000000", Unit.KPVON);
        TransactionResponse res = stakingContract.addStaking(
        		stakingPubKey,
                StakingAmountType.FREE_AMOUNT_TYPE,
                stakingAmount.toBigInteger()
        ).send();
        logger.debug("res:{}",res);
    }

    // 撤销质押(退出验证人)
    @Test
    public void unStaking() throws Exception {
        TransactionResponse res = stakingContract.unStaking(
        		stakingPubKey
        ).send();
        logger.debug("res:{}",res);
    }

    // 发送委托交易
    @Test
    public void delegate() throws Exception {
    	BigDecimal delegate = Convert.toVon("65000", Unit.KPVON);
        TransactionResponse res = delegateContract.delegate(
        		stakingPubKey,
                StakingAmountType.FREE_AMOUNT_TYPE,
                delegate.toBigInteger()
        ).send();
        logger.debug("res:{}",res);
    }


    public static void main(String[] args) throws Exception {
        TransactionSender ts = new TransactionSender();


        UpdateStakingParam.Builder uBuilder = new UpdateStakingParam.Builder();
//        uBuilder.setBenifitAddress("atp1cy2uat0eukfrxv897s5s8lnljfka5ewjj943gf");
        uBuilder.setDetails("Node of CDM");
//        uBuilder.setExternalId("5FD68B690010632B");
        uBuilder.setNodeId("0aa9805681d8f77c05f317efc141c97d5adb511ffb51f5a251d2d7a4a3a96d9a12adf39f06b702f0ccdff9eddc1790eb272dca31b0c47751d49b5931c58701e7");
//        uBuilder.setNodeName("cdm-004");
//        uBuilder.setWebSite("WWW.CCC.COM");
        uBuilder.setRewardPer(new BigInteger("950"));
        UpdateStakingParam updateStakingParam = new UpdateStakingParam(uBuilder);
        TransactionResponse res = stakingContract.updateStakingInfo(updateStakingParam).send();
        logger.debug("res:{}",res);




//    	Credentials credentials = WalletUtils.loadCredentials("88888888", "F:\\文件\\矩真文件\\区块链\\PlatScan\\fd9d508df262a1c968e0d6c757ab08b96d741f4b_88888888.json");
////    	Credentials credentials = WalletUtils.loadCredentials("11111111", "D:\\blockchain\\file\\60ceca9c1290ee56b98d4e160ef0453f7c40d219");
//    	byte[] byteArray = credentials.getEcKeyPair().getPrivateKey().toByteArray();
//        String privateKey = Hex.toHexString(byteArray);
//        logger.debug("Private Key:{}",privateKey);
////    	Credentials credentials = Credentials.create("a689f0879f53710e9e0c1025af410a530d6381eebb5916773195326e123b822b");
////    	WalletUtils.generateWalletFile("88888888", credentials.getEcKeyPair(), new File("d://"), true);
    }

    //提案交易
    @Test
    public void paramPrposal(){
        try{
            PlatonBlockNumber platonBlockNumber = currentValidWeb3j.platonBlockNumber().send();
            Proposal proposal = new Proposal();
            proposal.setSubmitBlock(platonBlockNumber.getBlockNumber());
            proposal.setProposalType(ProposalType.PARAM_PROPOSAL);
            proposal.setPiPid("2000");
            proposal.setVerifier("0x0aa9805681d8f77c05f317efc141c97d5adb511ffb51f5a251d2d7a4a3a96d9a12adf39f06b702f0ccdff9eddc1790eb272dca31b0c47751d49b5931c58701e7");
            proposal.setModule("staking");
            proposal.setName("maxValidators");
            proposal.setNewValue("26");
            BaseResponse baseResponse = proposalContract.submitProposal(proposal).send();
            logger.debug("res:{}",baseResponse.getCode());
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    // 发送解委托交易
    @Test
    public void unDelegate() throws Exception {
    	BigDecimal delegate = Convert.toVon("5000", Unit.KPVON);
        TransactionResponse res = delegateContract.unDelegate(
        		stakingPubKey,
                BigInteger.valueOf(86),
                delegate.toBigInteger(),new GasProvider() {
        			
        			@Override
        			public BigInteger getGasPrice() {
        				return BigInteger.valueOf(10000000000l);
        			}
        			
        			@Override
        			public BigInteger getGasLimit() {
        				return BigInteger.valueOf(300000l);
        			}
        		}
        ).send();
        logger.debug("res:{}",res);
    }
    
 // 发送解委托奖励提取交易
    @Test
    public void withdrawReward() throws Exception {
        TransactionResponse res = rewardContract.withdrawDelegateReward(new GasProvider() {
			
			@Override
			public BigInteger getGasPrice() {
				return BigInteger.valueOf(10000000000l);
			}
			
			@Override
			public BigInteger getGasLimit() {
				return BigInteger.valueOf(3000000l);
			}
		}).send();
        logger.debug("res:{}",res);
    }
    
    @Test
    public void testPress() throws Exception {
//    	String path = "d://111//";
//    	FileWriter fileWriter = new FileWriter("d://111//summary.txt");
//    	for(int i=0;i<1000;i++) {
//    		String wpath = WalletUtils.generateFullNewWalletFile("88888888", new File(path));
//    		wpath = path + wpath;
//    		Credentials credentialsTemp = WalletUtils.loadCredentials("88888888", wpath);
//    		Transfer.sendFunds(
//			        currentValidWeb3j,
//			        credentials,
//			        chainId,
//			        credentialsTemp.getAddress(),
//			        BigDecimal.valueOf(10000),
//			        Convert.Unit.ATP
//			).send();
//    		byte[] byteArray = credentials.getEcKeyPair().getPrivateKey().toByteArray();
//            String privateKey = Hex.toHexString(byteArray);
//    		fileWriter.write(privateKey + ";");
//    	}
//    	fileWriter.close();
    	
    	FileReader fileReader = new FileReader("d://111//summary.txt");
    	char[] buf=new char[1024];
		//read(char [])返回读到的字符个数
		int num=0;
		String data = "";
		while((num=fileReader.read(buf))!=-1) //记住即可，read方法如果没有可读取的了，则返回-1，所以就是一直读取，并将读取的内容存入ch，一直到结尾
		{
			data = data + new String(buf,0,num);//打印读取的结果，由于ch是int类型，将其强制转换为String类型
		}
		fileReader.close();
		System.out.println(data);
		String[] pData = data.split(";");
		List<Credentials> credentialsList = new ArrayList<>();
		for(String privateKey: pData) {
			Credentials c = Credentials.create(privateKey);
			credentialsList.add(c);
		}
//		for(Credentials c:credentialsList) {
//			Transfer.sendFunds(
//			        currentValidWeb3j,
//			        credentials,
//			        chainId,
//			        c.getAddress(),
//			        BigDecimal.valueOf(100),
//			        Convert.Unit.ATP
//			).send();
//		}
		
		
		String urls = "http://192.168.112.171:6789,http://192.168.112.171:7789,http://192.168.112.171:5789,"
				+ "http://192.168.112.172:6789,http://192.168.112.172:7789,http://192.168.112.172:8789";
		List<Web3j> web3js = new ArrayList<>();
		for(String url:urls.split(",") ) {
			Web3j web3j = Web3j.build(new HttpService(url));
			web3js.add(web3j);
		}
		for (int i = 0; i < 100; i++) {
			for(Credentials c:credentialsList) {
				for(Web3j web3j : web3js) {
					Transfer.sendFunds(
							web3j,
					        c,
					        "lax1vr8v48qjjrh9dwvdfctqauz98a7yp5se77fm2e",
					        BigDecimal.valueOf(1),
					        Convert.Unit.KPVON
					).sendAsync();
					System.out.println("发送交易："+i);
				}
			}
		}
    }

    @Test
    public void getBlockNumber() throws Exception {
        Web3j web3j = Web3j.build(new HttpService("http://192.168.112.171:6789"));
//        long blockNum = 11777;
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

        try {
            CallResponse<List <Node>> result = nodeContract.getVerifierList().send();
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }


        BigDecimal bg = Convert.toVon("1", Unit.KPVON);
        System.out.println(bg);
        bg = Convert.fromVon("10000000000000000000000000", Unit.KPVON);
        System.out.println(bg);

        CallResponse<Node> nodes = stakingContract.getStakingInfo("0xef97cb9caf757c70e9aca9062a9f6607ce89c3e7cac90ffee56d3fcffffa55aebd20b48c0db3924438911fd1c88c297d6532b434c56dbb5d9758f0794c6841dc").send();
        System.out.println(nodes);
    }

    @Test
    public void TestAll() throws Exception {
    	this.transfer();
    	this.staking();
    	this.updateStakingInfo();
    	this.addStaking();
    	this.delegate();
    }
}
