package com.platon.browser.common.service.elasticsearch;

import com.platon.browser.TestAgentApplication;
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
@SpringBootTest(classes = TestAgentApplication.class)
public class EsTokenTransferRecordServiceTest {

    @Autowired
    private EsTokenTransferRecordService esTokenTransferRecordService;

    @Test
    public void save() {
        /*Set<ESTokenTransferRecord> records = new HashSet<>();
        records.add(ESTokenTransferRecord.builder().seq(1l).hash("11txhash-01").bn(1l).bTime(new Date())
                .contract("0xfe00596d06310daf0f2afff1072c8adc57158e84").decimal(6).sign("methodSign").from("0x38b98a6358d9247a96bc39ee4a0c1020f5551151").result(1).build());
        records.add(ESTokenTransferRecord.builder().seq(2l).hash("112txhash-01").bn(1l).bTime(new Date()).tto("0x38b98a6358d9247a96bc39ee4a0c1020f5551151")
                .contract("0x855b837dcbcd18a7f71d58a3d0b768e9eef2acc6").decimal(6).sign("methodSign").from("0x855b837dcbcd18a7f71d58a3d0b768e9eef2acc6").result(1).build());
        records.add(ESTokenTransferRecord.builder().seq(3l).hash("113txhash-01").bn(1l).bTime(new Date()).tto("0x38b98a6358d9247a96bc39ee4a0c1020f5551151")
                .contract("0x38b98a6358d9247a96bc39ee4a0c1020f5551156").decimal(6).sign("methodSign").from("0x38b98a6358d9247a96bc39ee4a0c1020f5551159").result(1).build());
        esTokenTransferRecordService.save(records);
        System.out.println(".");*/
    }
}