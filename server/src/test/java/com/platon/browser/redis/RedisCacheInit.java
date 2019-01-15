package com.platon.browser.redis;

import com.platon.browser.ServerApplication;
import com.platon.browser.service.ServiceTestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= ServerApplication.class)
public class RedisCacheInit extends ServiceTestBase {

    @Test
    public void initRedis(){

    }

}

