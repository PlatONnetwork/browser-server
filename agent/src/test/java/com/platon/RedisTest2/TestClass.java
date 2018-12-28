package com.platon.RedisTest2;

import com.platon.browser.agent.client.Web3jClient;
import com.platon.browser.agent.contract.CandidateConstract;
import com.platon.browser.agent.contract.CandidateContract;
import com.platon.browser.agent.job.NodeSynchronizeJob;
import com.platon.browser.common.dto.agent.CandidateDto;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TestClass {
    public static void main(String[] args) throws Exception {
        URL resource = NodeSynchronizeJob.class.getClassLoader().getResource("platonbrowser.json");
        String path = resource.getPath();
        Credentials credentials = WalletUtils.loadCredentials("88888888", path);
        Web3j web3j = Web3j.build(new HttpService("http://192.168.9.76:6788"));
        List<CandidateDto> candidateDtoList = new ArrayList<>();
        //build contract
        CandidateContract candidateConstract = CandidateContract.load("","0x1000000000000000000000000000000000000001", web3j,credentials, DefaultGasProvider.GAS_PRICE, DefaultGasProvider.GAS_LIMIT);
        //call contract function get nodeInfo rangking 1-200 from blockchain
        String nodeInfoList = candidateConstract.CandidateList().send();
        System.out.println(nodeInfoList);
    }
}
