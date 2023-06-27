package com.platon.browser.bean;

import com.platon.browser.AgentTestBase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;

@Slf4j
@RunWith(MockitoJUnitRunner.Silent.class)
public class ComplementNodeOptTest extends AgentTestBase {

    @Test
    public void Test() throws InvocationTargetException, IllegalAccessException {
        List <com.platon.browser.elasticsearch.dto.Transaction> list = transactionList;
        ComplementNodeOpt complementNodeOpt = ComplementNodeOpt.newInstance();
        complementNodeOpt.setBNum(list.get(0).getNum());
        complementNodeOpt.setCreTime(new Date());
        complementNodeOpt.setDesc("test");
        complementNodeOpt.setId(1L);
        complementNodeOpt.setNodeId("0x12fdsfj1984h498r14r");
        complementNodeOpt.setTime(new Date());
        complementNodeOpt.setTxHash(list.get(0).getHash());
        complementNodeOpt.setType(list.get(0).getType());
        complementNodeOpt.setUpdTime(new Date());

        for(Method m:ComplementNodeOpt.class.getDeclaredMethods()){
            if(m.getName().contains("get")){
                m.invoke(complementNodeOpt);
            }
        }

        assertTrue(true);
    }
}
