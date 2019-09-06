package com.platon.browser.service.util;
import java.text.ParseException;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.platon.browser.BrowserApiApplication;
import com.platon.browser.util.DateUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BrowserApiApplication.class)
public class DateUtilTest {
    @Test
    public void getGMT(){
    	Date date = new Date();
        System.out.println("before:"+ date);
        System.out.println("after:"+ DateUtil.getGMT(date));
    }
    
    @Test
    public void getCST() throws ParseException{
    	Date date = new Date();
        System.out.println("before:"+ date);
        String dt = DateUtil.getGMT(date);
        System.out.println("after:"+ DateUtil.getCST(dt));
    }

}

