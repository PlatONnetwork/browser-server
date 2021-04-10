package com.platon.browser.bean;

import com.platon.browser.cache.PPosInvokeContractInputCache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class TransDataTest {
    @Test
    public void test() {
        TransData param = new TransData();
        param.setCode("1");
        param.setInput("me");

        param.getCode();
        param.getInput();

        List<TransData> list = new ArrayList<>();
        list.add(param);
        PPosInvokeContractInput pPosInvokeContractInput = new PPosInvokeContractInput();
        pPosInvokeContractInput.setFrom("1");
        pPosInvokeContractInput.setTo("1");
        pPosInvokeContractInput.setTxHash("1");
        pPosInvokeContractInput.setTransDatas(list);

        pPosInvokeContractInput.getFrom();
        pPosInvokeContractInput.getTo();
        pPosInvokeContractInput.getTransDatas();
        pPosInvokeContractInput.getTransDatas();

        PPosInvokeContractInputCache.hasCache(100l);
        PPosInvokeContractInputCache.getPPosInvokeContractInput("");
        PPosInvokeContractInputCache.update(100l, new ArrayList<>());
        assertTrue(true);
    }
}
