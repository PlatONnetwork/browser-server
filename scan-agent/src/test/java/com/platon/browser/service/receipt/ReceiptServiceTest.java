package com.platon.browser.service.receipt;

import com.platon.AgentApplicationTest;
import com.platon.browser.AgentTestBase;
import com.platon.browser.bean.Receipt;
import com.platon.browser.bean.ReceiptResult;
import com.platon.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @description: MySQL/ES/Redis启动一致性自检服务测试
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-13 11:41:00
 **/

@Slf4j
@SpringBootTest(classes = { AgentApplicationTest.class })
@ActiveProfiles("platon")
public class ReceiptServiceTest extends AgentTestBase {

    @Resource
    private ReceiptService receiptService;
    @Test
    public void getReceiptAsync() throws ExecutionException, InterruptedException {
        // 异步获取交易回执
        CompletableFuture<ReceiptResult> receiptCF = receiptService.getReceiptAsync(58035682L);
        ReceiptResult receiptResult = receiptCF.get();
        System.out.println("receiptResult.getMap().size():" + receiptResult.getMap().size());

        Iterator<Receipt> receiptIt = receiptResult.getMap().values().iterator();
        int idx = 0;
        while (receiptIt.hasNext()){
            System.out.println("idx:" + (idx++));
            Receipt receipt = receiptIt.next();
            /*receipt.setContractSuicided(new ArrayList<>());*/
            System.out.println("receipt:" + JSONUtil.toJSONString(receipt));
        }
    }



}
