package com.platon.browser.contract;

import com.alibaba.fastjson.JSON;
import com.platon.browser.TestBase;
import com.platon.browser.client.PlatonClient;
import com.platon.browser.dto.Candidate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
public class CandidateContractTest extends TestBase {

    private static final Logger logger = LoggerFactory.getLogger(CandidateContractTest.class);

    @Autowired
    private PlatonClient platon;

    @Test
    public void CandidateList(){
        chainsConfig.getChainIds().forEach(chainId->{
            try {
                String res = platon.getCandidateContract(chainId).GetCandidateList().send();
                List<String> candidateStrArr = JSON.parseArray(res,String.class);
                // 候选
                List<Candidate> candidates = JSON.parseArray(candidateStrArr.get(0),Candidate.class);
                // 备选
                List<Candidate> alternates = JSON.parseArray(candidateStrArr.get(1),Candidate.class);
                logger.error("候选：{}",candidates);
                logger.error("备选：{}",alternates);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
