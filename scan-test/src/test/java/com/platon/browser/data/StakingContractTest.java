package com.platon.browser.data;

import com.platon.abi.solidity.datatypes.BytesType;
import com.platon.abi.solidity.datatypes.generated.Uint16;
import com.platon.abi.solidity.datatypes.generated.Uint256;
import com.platon.contracts.ppos.DelegateContract;
import com.platon.contracts.ppos.StakingContract;
import com.platon.contracts.ppos.abi.Function;
import com.platon.contracts.ppos.dto.CallResponse;
import com.platon.contracts.ppos.dto.TransactionResponse;
import com.platon.contracts.ppos.dto.common.FunctionType;
import com.platon.contracts.ppos.dto.enums.StakingAmountType;
import com.platon.contracts.ppos.dto.req.StakingParam;
import com.platon.contracts.ppos.dto.req.UpdateStakingParam;
import com.platon.contracts.ppos.dto.resp.Node;
import com.platon.contracts.ppos.utils.EncoderUtils;
import com.platon.crypto.Credentials;
import com.platon.crypto.Hash;
import com.platon.crypto.RawTransaction;
import com.platon.crypto.TransactionEncoder;
import com.platon.parameters.NetworkParameters;
import com.platon.protocol.Web3j;
import com.platon.protocol.core.DefaultBlockParameterName;
import com.platon.protocol.core.methods.response.PlatonGetTransactionCount;
import com.platon.protocol.core.methods.response.PlatonSendTransaction;
import com.platon.protocol.http.HttpService;
import com.platon.tx.Transfer;
import com.platon.utils.Convert;
import com.platon.utils.Convert.Unit;
import com.platon.utils.Numeric;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StakingContractTest {

    private String nodeId = "0x3058ac78b0a05637218a417e562daaca2d640afb3d142ada765650cc0bed892d91d6e8128df0a59397ea051a2d91af5b532866f411811f4fd46de068ad0e168d";
    Long chainId = 298l;
    String blsPubKey = "5ccd6b8c32f2713faa6c9a46e5fb61ad7b7400e53fabcbc56bdc0c16fbfffe09ad6256982c7059e7383a9187ad93a002a7cda7a75d569f591730481a8b91b5fad52ac26ac495522a069686df1061fc184c31771008c1fedfafd50ae794778811";
    private Web3j web3j = Web3j.build(new HttpService("http://192.168.10.221:6789"));
    
    
    private Credentials superCredentials;
    private Credentials stakingCredentials;
    private Credentials benefitCredentials;
    private StakingContract stakingContract;
    private DelegateContract delegateContract;

    @Before
    public void init() throws Exception {
    	superCredentials = Credentials.create("0xa689f0879f53710e9e0c1025af410a530d6381eebb5916773195326e123b822b");
    	System.out.println("superCredentials balance="+ web3j.platonGetBalance(superCredentials.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance());

    	stakingCredentials = Credentials.create("0x009614c2b32f2d5d3421591ab3ffc03ac66c831fb6807b532f6e3a8e7aac31f1d9");
    	System.out.println("stakingCredentials balance="+ web3j.platonGetBalance(stakingCredentials.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance());
    	
    	benefitCredentials = Credentials.create("0x3581985348bffd03b286b37712165f7addf3a8d907b25efc44addf54117e9b91");
    	System.out.println("benefitCredentials balance="+ web3j.platonGetBalance(benefitCredentials.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance());
  	
        stakingContract = StakingContract.load( web3j,stakingCredentials);
        
        delegateContract = DelegateContract.load( web3j,stakingCredentials);
    }
    
    @Test
    public void transfer() throws Exception {
    	Transfer.sendFunds(web3j, superCredentials, "0xfbdf3c5bf983cdf67685883f8eaabfd4e31249ec", new BigDecimal("10000000"), Unit.KPVON).send();
    	System.out.println("stakingCredentials balance="+ web3j.platonGetBalance(stakingCredentials.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance());
    }
    
    @Test
    public void getStakingInfo() {
    	try {
    		CallResponse<Node> baseResponse = stakingContract.getStakingInfo(nodeId).send();
    		System.out.println(baseResponse);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

    @Test
    public void getPackageReward() {
    	try {
    		CallResponse<BigInteger> baseResponse = stakingContract.getPackageReward().send();
    		System.out.println(baseResponse);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    @Test
    public void getStakingReward() {
    	try {
    		CallResponse<BigInteger> baseResponse = stakingContract.getStakingReward().send();
    		System.out.println(baseResponse);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    @Test
    public void getAvgPackTime() {
    	try {
    		CallResponse<BigInteger> baseResponse = stakingContract.getAvgPackTime().send();
    		System.out.println(baseResponse);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

    @Test
    public void staking() throws Exception {   	
        try {
        	StakingAmountType stakingAmountType = StakingAmountType.FREE_AMOUNT_TYPE;
        	String benifitAddress = benefitCredentials.getAddress();
        	String externalId = "";
            String nodeName = "chendai-node3";
            String webSite = "www.baidu.com";
            String details = "chendai-node3-details";
            BigDecimal stakingAmount = Convert.toVon("5000000", Unit.KPVON);
            BigInteger rewardPer = BigInteger.valueOf(10L);

        	
            PlatonSendTransaction platonSendTransaction = stakingContract.stakingReturnTransaction(new StakingParam.Builder()
                    .setNodeId(nodeId)
                    .setAmount(stakingAmount.toBigInteger())  
                    .setStakingAmountType(stakingAmountType)
                    .setBenifitAddress(benifitAddress)
                    .setExternalId(externalId)
                    .setNodeName(nodeName)
                    .setWebSite(webSite)
                    .setDetails(details)
                    .setBlsPubKey(blsPubKey)
                    .setProcessVersion(web3j.getProgramVersion().send().getAdminProgramVersion())
                    .setBlsProof(web3j.getSchnorrNIZKProve().send().getAdminSchnorrNIZKProve())
                    .setRewardPer(rewardPer)
                    .build()).send();
            TransactionResponse baseResponse = stakingContract.getTransactionResponse(platonSendTransaction).send();
            System.out.println(baseResponse.toString());  //394
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void updateStakingInfo() {
        try {
        	String benifitAddress = benefitCredentials.getAddress();
        	String externalId = "";
            String nodeName = "chendai-node3-u";
            String webSite = "www.baidu.com-u";
            String details = "chendai-node3-details-u";
            BigInteger rewardPer = BigInteger.valueOf(2000L);

            PlatonSendTransaction platonSendTransaction = stakingContract.updateStakingInfoReturnTransaction(new UpdateStakingParam.Builder()
            		.setBenifitAddress(benifitAddress)
            		.setExternalId(externalId)
            		.setNodeId(nodeId)
            		.setNodeName(nodeName)
            		.setWebSite(webSite)
            		.setDetails(details)
                    .setRewardPer(rewardPer)
            		.build()).send();

            TransactionResponse baseResponse = stakingContract.getTransactionResponse(platonSendTransaction).send();
            System.out.println(baseResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void addStaking() {
        try {
        	StakingAmountType stakingAmountType = StakingAmountType.FREE_AMOUNT_TYPE;
            BigDecimal addStakingAmount = Convert.toVon("4000000", Unit.KPVON).add(new BigDecimal("999999999999999998"));
        	
            PlatonSendTransaction platonSendTransaction = stakingContract.addStakingReturnTransaction(nodeId, stakingAmountType, addStakingAmount.toBigInteger()).send();
            TransactionResponse baseResponse = stakingContract.getTransactionResponse(platonSendTransaction).send();
            System.out.println(baseResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void unStaking() {
        try {
            PlatonSendTransaction platonSendTransaction = stakingContract.unStakingReturnTransaction(nodeId).send();
            TransactionResponse baseResponse = stakingContract.getTransactionResponse(platonSendTransaction).send();
            System.out.println(baseResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void exec() {
        try {
            //1 getNonce
            BigInteger beginNonce = getNonce(stakingCredentials);

            List<String> rawDataList = new ArrayList<>();
//            rawDataList.add(createSignedDataTxCode1000(beginNonce.add(BigInteger.valueOf(0))));
//            rawDataList.add(createSignedDataTxCode1003(beginNonce.add(BigInteger.valueOf(1))));
//
//            rawDataList.add(createSignedDataTxCode1000(beginNonce.add(BigInteger.valueOf(2))));
//            rawDataList.add(createSignedDataTxCode1003(beginNonce.add(BigInteger.valueOf(3))));
//
//            rawDataList.add(createSignedDataTxCode1000(beginNonce.add(BigInteger.valueOf(4))));
//            rawDataList.add(createSignedDataTxCode1003(beginNonce.add(BigInteger.valueOf(5))));
            
            rawDataList.add(createSignedDataTxCode1004(beginNonce.add(BigInteger.valueOf(0))));
            rawDataList.add(createSignedDataTxCode1004(beginNonce.add(BigInteger.valueOf(1))));


            for (String rawData : rawDataList) {
                String txHashLocal = Hash.sha3(rawData);
                String hash = web3j.platonSendRawTransaction(txHashLocal).send().getTransactionHash();
                System.out.println("---->" + hash);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String createSignedDataTxCode1003(BigInteger nonce) throws  Exception{
        Credentials credentials = Credentials.create(stakingCredentials.getEcKeyPair());

        Function function = new Function(FunctionType.WITHDREW_STAKING_FUNC_TYPE,
                Arrays.asList(new BytesType(Numeric.hexStringToByteArray(nodeId))));

        BigInteger gasPrice = web3j.platonGasPrice().send().getGasPrice();
        BigInteger gasLimit = new BigInteger("472388");
        BigInteger amount = BigInteger.ZERO;
        String toAddress = NetworkParameters.getPposContractAddressOfStaking();

        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit, toAddress, amount, EncoderUtils.functionEncoder(function));

        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, Long.valueOf(chainId), credentials);

        return Numeric.toHexString(signedMessage);
    }


    private String createSignedDataTxCode1000(BigInteger nonce) throws  Exception{
        Credentials credentials = Credentials.create(stakingCredentials.getEcKeyPair());

        StakingAmountType stakingAmountType = StakingAmountType.FREE_AMOUNT_TYPE;
        String benifitAddress = benefitCredentials.getAddress();
        String externalId = "";
        String nodeName = "chendai-node3";
        String webSite = "www.baidu.com";
        String details = "chendai-node3-details";
        BigDecimal stakingAmount = Convert.toVon("5000000", Unit.KPVON);
        BigInteger rewardPer = BigInteger.valueOf(10L);

        StakingParam stakingParam =  new StakingParam.Builder()
                .setNodeId(nodeId)
                .setAmount(stakingAmount.toBigInteger())
                .setStakingAmountType(stakingAmountType)
                .setBenifitAddress(benifitAddress)
                .setExternalId(externalId)
                .setNodeName(nodeName)
                .setWebSite(webSite)
                .setDetails(details)
                .setBlsPubKey(blsPubKey)
                .setProcessVersion(web3j.getProgramVersion().send().getAdminProgramVersion())
                .setBlsProof(web3j.getSchnorrNIZKProve().send().getAdminSchnorrNIZKProve())
                .setRewardPer(rewardPer)
                .build();

        Function function = new Function(
                FunctionType.STAKING_FUNC_TYPE,
                stakingParam.getSubmitInputParameters());

        BigInteger gasPrice = web3j.platonGasPrice().send().getGasPrice();
        BigInteger gasLimit = new BigInteger("472388");
        BigInteger amount = BigInteger.ZERO;
        String toAddress = NetworkParameters.getPposContractAddressOfStaking();

        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit, toAddress, amount, EncoderUtils.functionEncoder(function));

        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, Long.valueOf(chainId), credentials);

        return Numeric.toHexString(signedMessage);
    }
    
    private String createSignedDataTxCode1004(BigInteger nonce) throws  Exception{
        Credentials credentials = Credentials.create(stakingCredentials.getEcKeyPair());

        StakingAmountType stakingAmountType = StakingAmountType.FREE_AMOUNT_TYPE;

        BigDecimal delegate = Convert.toVon("20", Unit.KPVON);
        Function function = new Function(FunctionType.DELEGATE_FUNC_TYPE,
				Arrays.asList(new Uint16(stakingAmountType.getValue())
				, new BytesType(Numeric.hexStringToByteArray(nodeId))
				, new Uint256(delegate.toBigInteger())));

        BigInteger gasPrice = web3j.platonGasPrice().send().getGasPrice();
        BigInteger gasLimit = new BigInteger("472388");
        BigInteger amount = BigInteger.ZERO;
        String toAddress = NetworkParameters.getPposContractAddressOfStaking();

        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit, toAddress, amount, EncoderUtils.functionEncoder(function));

        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, Long.valueOf(chainId), credentials);

        return Numeric.toHexString(signedMessage);
    }

    private BigInteger getNonce(Credentials credentials) throws IOException {
        PlatonGetTransactionCount ethGetTransactionCount = web3j.platonGetTransactionCount(
                credentials.getAddress(), DefaultBlockParameterName.PENDING).send();

        if (ethGetTransactionCount.getTransactionCount().intValue() == 0) {
            ethGetTransactionCount = web3j.platonGetTransactionCount(
                    credentials.getAddress(), DefaultBlockParameterName.LATEST).send();
        }

        return ethGetTransactionCount.getTransactionCount();
    }

}
