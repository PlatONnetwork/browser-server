package com.platon.browser.test;

import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.platon.browser.BrowserApiApplication;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BrowserApiApplication.class)
public class JasyptTest {

    @Autowired
    StringEncryptor encryptor;
    
    @Test
    public void encry() {
        String username = encryptor.encrypt("root");//加密root
        System.out.println("username:" + username);
        String password = encryptor.encrypt("Juzhen123!");//加密123456
        System.out.println("password:" + password);
        assertTrue(true);
   }
}
