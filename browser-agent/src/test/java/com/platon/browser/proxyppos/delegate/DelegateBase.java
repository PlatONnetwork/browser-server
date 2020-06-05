package com.platon.browser.proxyppos.delegate;

import com.platon.browser.proxyppos.ProxyContract;
import com.platon.browser.proxyppos.TestBase;
import com.platon.sdk.utlis.NetworkParameters;
import org.web3j.crypto.Credentials;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;

public class DelegateBase extends TestBase {
    protected final String TARGET_CONTRACT_ADDRESS = NetworkParameters.getPposContractAddressOfStaking(chainId);
    protected void sendRequest(byte[] d1, byte[] d2) throws Exception {
        // lax1alckh87rsx0ks2vkh4wzt3477xk3vymcwldxf4
        Credentials credentials = Credentials.create("5d7f539ac15de26de6abbb664291e613882842d3dbe4ec79b57af8bc6bb834aa");
        TransactionManager manager = new RawTransactionManager(web3j, credentials, chainId);
        ProxyContract contract = ProxyContract.load(proxyContractAddress, web3j, manager, gasProvider, chainId);
        invokeProxyContract(
                contract,
                d1,TARGET_CONTRACT_ADDRESS,
                d2,TARGET_CONTRACT_ADDRESS
        );
    }
}
