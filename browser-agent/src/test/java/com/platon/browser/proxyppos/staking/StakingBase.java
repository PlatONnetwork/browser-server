package com.platon.browser.proxyppos.staking;

import com.alaya.crypto.Credentials;
import com.alaya.parameters.NetworkParameters;
import com.alaya.protocol.Web3j;
import com.alaya.protocol.core.DefaultBlockParameterName;
import com.alaya.protocol.http.HttpService;
import com.alaya.tx.RawTransactionManager;
import com.alaya.tx.TransactionManager;
import com.platon.browser.proxyppos.ProxyContract;
import com.platon.browser.proxyppos.TestBase;

import java.math.BigInteger;

public class StakingBase extends TestBase {
    protected final String TARGET_CONTRACT_ADDRESS = NetworkParameters.getPposContractAddressOfStaking(chainId);
    protected void sendRequest(byte[] d1,byte[] d2) throws Exception {
        BigInteger contractBalance = defaultWeb3j.platonGetBalance(proxyStakingContractAddress, DefaultBlockParameterName.LATEST).send().getBalance();
        BigInteger delegatorBalance = defaultWeb3j.platonGetBalance(defaultCredentials.getAddress(chainId), DefaultBlockParameterName.LATEST).send().getBalance();
        System.out.println("*********************");
        System.out.println("*********************");
        System.out.println("ContractBalance("+proxyStakingContractAddress+"):"+contractBalance);
        System.out.println("OperatorBalance("+defaultCredentials.getAddress(chainId)+"):"+delegatorBalance);
        System.out.println("*********************");
        System.out.println("*********************");

        Credentials credentials = Credentials.create("a689f0879f53710e9e0c1025af410a530d6381eebb5916773195326e123b822b");
        Web3j web3j = Web3j.build(new HttpService("http://192.168.120.145:6790"));
        TransactionManager manager = new RawTransactionManager(web3j, credentials, chainId);
        ProxyContract contract = ProxyContract.load(proxyStakingContractAddress, web3j, manager, gasProvider, chainId);
        invokeProxyContract(
                contract,
                d1,TARGET_CONTRACT_ADDRESS,
                d2,TARGET_CONTRACT_ADDRESS
        );
    }
}
