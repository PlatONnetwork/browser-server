package com.platon.FilterTest;

import com.platon.browser.SpringbootApplication;
import com.platon.browser.client.Web3jClient;
import com.platon.browser.filter.NodeFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.web3j.platon.contracts.CandidateContract;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.EthBlock;

import java.math.BigInteger;

/**
 * User: dongqile
 * Date: 2019/1/11
 * Time: 15:00
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes= SpringbootApplication.class, value = "spring.profiles.active=1")
public class NodeFilterTest {

    private static Logger logger = LoggerFactory.getLogger(NodeFilterTest.class);

    @Autowired
    private NodeFilter nodeFilter;

    @Value("${chain.id}")
    private String chainId;

    @Autowired
    private Web3jClient web3jClient;


    @Test
    public void NodeFilterTest(){
        try{
            CandidateContract candidateContract = web3jClient.getCandidateContract();
            Web3j web3j = Web3jClient.getWeb3jClient();
            DefaultBlockParameter defaultBlockParameter = new DefaultBlockParameterNumber(new BigInteger(String.valueOf(78302L)));
            EthBlock ethBlock = web3j.ethGetBlockByNumber(defaultBlockParameter, true).send();
            String nodeInfoList = candidateContract.CandidateList().send();
            //nodeFilter.buid(nodeInfoList,ethBlock.getBlock().getNumber().longValue(),ethBlock);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}