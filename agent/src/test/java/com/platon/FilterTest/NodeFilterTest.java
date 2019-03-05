package com.platon.FilterTest;

import com.alibaba.fastjson.JSON;
import com.platon.TestBase;
import com.platon.browser.common.dto.agent.CandidateDto;
import com.platon.browser.common.util.CalculatePublicKey;
import com.platon.browser.dao.entity.NodeRanking;
import com.platon.browser.dao.entity.NodeRankingExample;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.web3j.platon.contracts.CandidateContract;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * User: dongqile
 * Date: 2019/1/11
 * Time: 15:00
 */
@RunWith(SpringRunner.class)
public class NodeFilterTest extends TestBase {

    private static Logger logger = LoggerFactory.getLogger(NodeFilterTest.class);


    @Test
    public void NodeFilterTest () {
        try {
            CandidateContract candidateContract = web3jClient.getCandidateContract(chainId);
            DefaultBlockParameter defaultBlockParameter = new DefaultBlockParameterNumber(new BigInteger(String.valueOf(400L)));
            EthBlock ethBlock = web3j.ethGetBlockByNumber(defaultBlockParameter, true).send();
            String nodeInfoList = candidateContract.CandidateList(new BigInteger(String.valueOf(400L))).send();
            List <EthBlock.TransactionResult> list = ethBlock.getBlock().getTransactions();
            List <Transaction> list1 = new ArrayList <>();
            List <TransactionReceipt> list2 = new ArrayList <>();
            for (EthBlock.TransactionResult transactionResult : list) {
                Transaction txList = (Transaction) transactionResult.get();
                EthTransaction ethTransaction = web3j.ethGetTransactionByHash(txList.getHash()).send();
                Optional <Transaction> value = ethTransaction.getTransaction();
                Transaction transaction = value.get();
                list1.add(transaction);
                EthGetTransactionReceipt ethGetTransactionReceipt = web3j.ethGetTransactionReceipt(transaction.getHash()).send();
                Optional <TransactionReceipt> transactionReceipt = ethGetTransactionReceipt.getTransactionReceipt();
                TransactionReceipt receipt = transactionReceipt.get();
                list2.add(receipt);
            }
            //Block block = blockFilter.blockAnalysis(ethBlock, list2, list1);
            //nodeFilter.build(nodeInfoList, ethBlock.getBlock().getNumber().longValue(), ethBlock, block.getBlockReward());
            NodeRankingExample nodeRankingExample = new NodeRankingExample();
            nodeRankingExample.createCriteria().andChainIdEqualTo(chainId).andIsValidNotEqualTo(1);
            List <NodeRanking> nodeRankings = nodeRankingMapper.selectByExample(nodeRankingExample);
            BigInteger a = CalculatePublicKey.testBlock(ethBlock);
            logger.info(a.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void nodeIdEqualsPublicKeyTest () {
        try {
            CandidateContract candidateContract = web3jClient.getCandidateContract(chainId);
            DefaultBlockParameter defaultBlockParameter = new DefaultBlockParameterNumber(new BigInteger(String.valueOf(2985L)));
            EthBlock ethBlock = web3j.ethGetBlockByNumber(defaultBlockParameter, true).send();
            String nodeInfoList = candidateContract.CandidateList(new BigInteger(String.valueOf(2985L))).send();
            List <CandidateDto> list = JSON.parseArray(nodeInfoList, CandidateDto.class);
            BigInteger a = CalculatePublicKey.testBlock(ethBlock);
            String node38793 = new BigInteger("65093423df6a0050ca9143f6150f2061932f9b26bf3ae30330e167d4c475d8039ce7b8467dfd706f59a1c625f54deb821048d725f84fe8a9091e98d2f350f9f9", 16).toString();
            logger.info(node38793);
            logger.info(a.toString());
            list.forEach(candidateDto -> {
                String n = new BigInteger(candidateDto.getCandidateId(), 16).toString();
                candidateDto.getCandidateId();
                logger.info(n);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Test
    public void newCadidateListTest () {
        String nodeInfoString = "[[{\"Deposit\":1200000000000000000000000,\"BlockNumber\":21,\"TxIndex\":0,\"CandidateId\":\"1f3a8672348ff6b789e416762ad53e69063138b8eb4d8780101658f24b2369f1a8e09499226b467d8bc0c4e03e1dc903df857eeb3c67733d21b6aaee2840e429\",\"Host\":\"123.125.71.38\",\"Port\":\"8545\",\"Owner\":\"0x740ce31b3fac20dac379db243021a51e80ad00d7\",\"From\":\"0x740ce31b3fac20dac379db243021a51e80ad00d7\",\"Extra\":\"{\\\"nodeName\\\": \\\"Platon-Beijing\\\", \\\"nodePortrait\\\": \\\"\\\",\\\"nodeDiscription\\\": \\\"PlatON-引力区\\\",\\\"nodeDepartment\\\": \\\"JUZIX\\\",\\\"officialWebsite\\\": \\\"https://www.platon.network/\\\",\\\"time\\\":1546503651190}\",\"Fee\":8000,\"TicketId\":\"0x25c942d427e2825e0c27c1d170e3c40405d4bb951645f0d86849490544fe69a8\"}],[{\"Deposit\":1100000000000000000000000,\"BlockNumber\":19273,\"TxIndex\":0,\"CandidateId\":\"2f3a8672348ff6b789e416762ad53e69063138b8eb4d8780101658f24b2369f1a8e09499226b467d8bc0c4e03e1dc903df857eeb3c67733d21b6aaee2840e429\",\"Host\":\"169.230.104.141\",\"Port\":\"16789\",\"Owner\":\"0x5a5c4368e2692746b286cee36ab0710af3efa6cf\",\"From\":\"0x5a5c4368e2692746b286cee36ab0710af3efa6cf\",\"Extra\":\"{\\\"nodeName\\\": \\\"Platon-California\\\", \\\"nodePortrait\\\": \\\"\\\",\\\"nodeDiscription\\\": \\\"PlatON-加利福尼亚超级节点\\\",\\\"nodeDepartment\\\": \\\"JUZIX\\\",\\\"officialWebsite\\\": \\\"https://www.platon.network/\\\",\\\"time\\\":1546506999144}\",\"Fee\":7000,\"TicketId\":\"0x0000000000000000000000000000000000000000000000000000000000000000\"},{\"Deposit\":1080000000000000000000000,\"BlockNumber\":19285,\"TxIndex\":0,\"CandidateId\":\"3f3a8672348ff6b789e416762ad53e69063138b8eb4d8780101658f24b2369f1a8e09499226b467d8bc0c4e03e1dc903df857eeb3c67733d21b6aaee2840e429\",\"Host\":\"115.68.28.11\",\"Port\":\"2333\",\"Owner\":\"0x493301712671ada506ba6ca7891f436d29185821\",\"From\":\"0x493301712671ada506ba6ca7891f436d29185821\",\"Extra\":\"{\\\"nodeName\\\": \\\"Platon-HuoBiPool\\\", \\\"nodePortrait\\\": \\\"\\\",\\\"nodeDiscription\\\": \\\"PlatON-火币服务池\\\",\\\"nodeDepartment\\\": \\\"HuoBi\\\",\\\"officialWebsite\\\": \\\"https://www.huobi.com/\\\",\\\"time\\\":1546503997752}\",\"Fee\":7500,\"TicketId\":\"0x0000000000000000000000000000000000000000000000000000000000000000\"}]]";
        try {
            List <String> candidateStrArr = JSON.parseArray(nodeInfoString, String.class);
            // 候选
            List <CandidateDto> candidates = JSON.parseArray(candidateStrArr.get(0), CandidateDto.class);
            // 备选
            List <CandidateDto> alternates = JSON.parseArray(candidateStrArr.get(1), CandidateDto.class);
            logger.error("候选：{}", candidates);
            logger.error("备选：{}", alternates);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}