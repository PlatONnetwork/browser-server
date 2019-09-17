package com.platon.browser.handler.delagation;

import com.platon.browser.TestBase;
import com.platon.browser.engine.handler.EventContext;
import com.platon.browser.engine.handler.delegation.DelegateHandler;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertTrue;

/**
 * @Auther: dongqile
 * @Date: 2019/9/5
 * @Description: 委托处理业务测试类
 */

public class DelegateHandlerTest extends TestBase {

    @Autowired
    private DelegateHandler delegateHandler;

    @Test
    public void testHandler(){
        EventContext eventContext = new EventContext();
        try{
            delegateHandler.handle(eventContext);
            assertTrue(true);
        }catch (Exception e){
            assertTrue(false);
        }
    }
}