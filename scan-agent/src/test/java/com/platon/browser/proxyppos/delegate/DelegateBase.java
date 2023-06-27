package com.platon.browser.proxyppos.delegate;

import com.platon.browser.proxyppos.ProxyContract;
import com.platon.browser.proxyppos.TestBase;
import com.platon.parameters.NetworkParameters;
import com.platon.protocol.Web3j;
import com.platon.protocol.core.DefaultBlockParameterName;
import com.platon.protocol.http.HttpService;
import com.platon.tx.RawTransactionManager;
import com.platon.tx.TransactionManager;

import java.math.BigInteger;

public class DelegateBase extends TestBase {
    protected final String TARGET_CONTRACT_ADDRESS = NetworkParameters.getPposContractAddressOfStaking();
    protected void sendRequest(byte[] d1, byte[] d2) throws Exception {
        // lax1alckh87rsx0ks2vkh4wzt3477xk3vymcwldxf4
        Web3j web3j = Web3j.build(new HttpService("http://192.168.120.145:6790"));
        TransactionManager manager = new RawTransactionManager(web3j, delegateCredentials);
        // 委托不能与质押使用同一个代理合约，否则会提示“当前账户不允许委托”
        ProxyContract contract = ProxyContract.load(proxyDelegateContractAddress, web3j, manager, gasProvider, chainId);
        BigInteger contractBalance = web3j.platonGetBalance(proxyDelegateContractAddress, DefaultBlockParameterName.LATEST).send().getBalance();
        BigInteger delegatorBalance = web3j.platonGetBalance(delegateCredentials.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance();
        System.out.println("*********************");
        System.out.println("*********************");
        System.out.println("ContractBalance("+proxyDelegateContractAddress+"):"+contractBalance);
        System.out.println("OperatorBalance("+delegateCredentials.getAddress()+"):"+delegatorBalance);
        System.out.println("*********************");
        System.out.println("*********************");
        invokeProxyContract(
                contract,
                d1,TARGET_CONTRACT_ADDRESS,
                d2,TARGET_CONTRACT_ADDRESS
        );
    }
}
