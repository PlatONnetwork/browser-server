package com.platon.browser.common.service.elasticsearch;

import com.platon.browser.AgentApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: AgentRJ
 * @Date: 2020/9/24
 * @Version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgentApplication.class)
public class EsTokenTransferRecordServiceTest {

    @Autowired
    private EsTokenTransferRecordService esTokenTransferRecordService;

    @Test
    public void save() {
        /*Set<ESTokenTransferRecord> records = new HashSet<>();
        records.add(ESTokenTransferRecord.builder().txHash("txhash-01").blockNumber(1l).blockTimestamp(new Date())
                .contract("contract").decimal(6).methodSign("methodSign").txFrom("from-02").result(1).build());
        records.add(ESTokenTransferRecord.builder().txHash("txhash-02").blockNumber(1l).blockTimestamp(new Date())
                .contract("contract").decimal(6).methodSign("methodSign").txFrom("from-02").result(1).build());
        records.add(ESTokenTransferRecord.builder().txHash("txhash-03").blockNumber(1l).blockTimestamp(new Date())
                .contract("contract").decimal(6).methodSign("methodSign").txFrom("from-03").result(1).build());
        esTokenTransferRecordService.save(records);
        System.out.println(".");*/
    }
}