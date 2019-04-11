package com.platon.browser.FilterTest;

import com.alibaba.fastjson.JSON;
import com.platon.browser.TestBase;
import com.platon.browser.client.PlatonClient;
import com.platon.browser.dao.entity.NodeRanking;
import com.platon.browser.dao.mapper.CustomNodeRankingMapper;
import com.platon.browser.dao.mapper.NodeRankingMapper;
import com.platon.browser.dto.agent.CandidateDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.web3j.platon.contracts.CandidateContract;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

/**
 * User: dongqile
 * Date: 2019/4/8
 * Time: 16:45
 */
@RunWith(SpringRunner.class)
public class NodeJobTest extends TestBase {
    private static Logger logger = LoggerFactory.getLogger(NodeJobTest.class);

    @Autowired
    private NodeRankingMapper nodeRankingMapper;
    @Autowired
    private CustomNodeRankingMapper customNodeRankingMapper;
    @Autowired
    private PlatonClient platonClient;

    @Test
    public void testDB(){
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("C:\\Users\\dongql\\Desktop\\test.json"));
            StringBuffer stringBuffer = new StringBuffer();
            bufferedReader.lines().forEach(line->stringBuffer.append(line));
            List <NodeRanking> list = JSON.parseArray(stringBuffer.toString(), NodeRanking.class);
            for(int i =0; i <= 10 ; i++){
                customNodeRankingMapper.insertOrUpdate(list);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }


    @Test
    public void nodeJobTest() throws Exception {
        CandidateContract candidateContract = platonClient.getCandidateContract("203");
        String nodelist = candidateContract.CandidateList().send();
        List <String> candidateStrArr = JSON.parseArray(nodelist, String.class);
        List <CandidateDto> nodes = JSON.parseArray(candidateStrArr.get(0), CandidateDto.class);
        List <CandidateDto> alternates = JSON.parseArray(candidateStrArr.get(1), CandidateDto.class);
        logger.debug("候选节点 {}",nodes);
        logger.debug("备选节点 {}",alternates);
    }
}