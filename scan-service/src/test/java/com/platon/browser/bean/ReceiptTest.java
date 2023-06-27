package com.platon.browser.bean;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ReceiptTest {
    @Test
    public void test(){
        Receipt receipt = new Receipt();
        receipt.setBlockNumber(444L);
        receipt.setFailReason("2024");
        receipt.setGasUsed("0x222");
        List<com.platon.protocol.core.methods.response.Log> logs = new ArrayList<>();
        com.platon.protocol.core.methods.response.Log log = new com.platon.protocol.core.methods.response.Log();
        log.setRemoved(false);
        log.setLogIndex("0");
        log.setData("0xf857838203ed83823302b842b8400aa9805681d8f77c05f317efc141c97d5adb511ffb51f5a251d2d7a4a3a96d9a12adf39f06b702f0ccdff9eddc1790eb272dca31b0c47751d49b5931c58701e78a893635c9adc5dea00000");
        log.setAddress("0x2e95e3ce0a54951eb9a99152a6d5827872dfb4fd");
        log.setBlockNumber("");
        log.setTransactionHash("0x22988d54c85c358f83d80e062e4ed0540fd33ce7e9a8267a438be01af987fcfb");
        logs.add(log);

        receipt.setLogs(logs);
        receipt.setLogStatus(1);
        receipt.setStatus("0x01");
        receipt.setTransactionHash("0xd34343");
        receipt.setTransactionIndex("98");

        log.setData(null);
        receipt.decodeLogs();

        receipt.setLogs(null);
        receipt.decodeLogs();


        receipt.getBlockNumber();
        receipt.getFailReason();
        receipt.getGasUsed();
        receipt.getLogs();
        receipt.getLogStatus();
        receipt.getStatus();
        receipt.getTransactionHash();
        receipt.getTransactionIndex();

        ReceiptResult receiptResult = new ReceiptResult();
        receiptResult.setMap(Collections.emptyMap());
        receiptResult.getMap();

        assertTrue(true);
    }
}
