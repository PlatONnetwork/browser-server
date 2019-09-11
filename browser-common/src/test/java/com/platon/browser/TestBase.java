package com.platon.browser;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(classes= BrowserCommonApplication.class, value = "spring.profiles.active=test")
public class TestBase extends TestData{
	
}
