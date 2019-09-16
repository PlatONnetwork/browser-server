package com.platon.browser.test;

import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.platon.browser.BrowserApiApplication;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BrowserApiApplication.class)
@Slf4j
@EnableEncryptableProperties
public class JasyptTest {

    @Autowired
    StringEncryptor encryptor;
    
    @Test
    public void encry() {
        String username = encryptor.encrypt("root");//加密root
        log.info("username:" + username);
        String password = encryptor.encrypt("Juzhen123!");//加密123456
        log.info("password:" + password);
   }
}
