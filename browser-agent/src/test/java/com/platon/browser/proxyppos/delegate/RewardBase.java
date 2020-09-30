package com.platon.browser.proxyppos.delegate;

import com.alaya.parameters.NetworkParameters;
import com.alaya.protocol.Web3j;
import com.alaya.protocol.core.DefaultBlockParameterName;
import com.alaya.protocol.http.HttpService;
import com.alaya.tx.RawTransactionManager;
import com.alaya.tx.TransactionManager;
import com.platon.browser.proxyppos.ProxyContract;
import com.platon.browser.proxyppos.TestBase;

import java.math.BigInteger;

public class RewardBase extends TestBase {
    protected final String TARGET_CONTRACT_ADDRESS = NetworkParameters.getPposContractAddressOfReward(chainId);
    protected void sendRequest(byte[] d1, byte[] d2) throws Exception {
        // lax1alckh87rsx0ks2vkh4wzt3477xk3vymcwldxf4
        Web3j web3j = Web3j.build(new HttpService("http://192.168.120.145:6790"));
        TransactionManager manager = new RawTransactionManager(web3j, delegateCredentials, chainId);
        ProxyContract contract = ProxyContract.load(proxyDelegateContractAddress, web3j, manager, gasProvider, chainId);
        BigInteger contractBalance = web3j.platonGetBalance(proxyDelegateContractAddress, DefaultBlockParameterName.LATEST).send().getBalance();
        BigInteger delegatorBalance = web3j.platonGetBalance(delegateCredentials.getAddress(chainId), DefaultBlockParameterName.LATEST).send().getBalance();
        System.out.println("*********************");
        System.out.println("*********************");
        System.out.println("ContractBalance("+proxyDelegateContractAddress+"):"+contractBalance);
        System.out.println("OperatorBalance("+delegateCredentials.getAddress(chainId)+"):"+delegatorBalance);
        System.out.println("*********************");
        System.out.println("*********************");
        invokeProxyContract(
                contract,
                d1,TARGET_CONTRACT_ADDRESS,
                d2,TARGET_CONTRACT_ADDRESS
        );
    }
}
