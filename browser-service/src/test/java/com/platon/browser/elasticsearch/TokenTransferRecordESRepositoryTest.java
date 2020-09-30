package com.platon.browser.elasticsearch;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * @Author: AgentRJ
 * @Date: 2020/9/24
 * @Version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TokenTransferRecordESRepositoryTest {

    @Autowired
    private TokenTransferRecordESRepository tokenTransferRecordESRepository;

    @Test
    public void testTemplateExists() {
        try {
            boolean exists = tokenTransferRecordESRepository.existsTemplate(tokenTransferRecordESRepository.getDefaultIndexTemplateName());
            tokenTransferRecordESRepository.putIndexTemplate(tokenTransferRecordESRepository.getDefaultIndexTemplateName(), tokenTransferRecordESRepository.defaultIndexTemplate());
            boolean after = tokenTransferRecordESRepository.existsTemplate(tokenTransferRecordESRepository.getDefaultIndexTemplateName());
            System.out.println("..");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}