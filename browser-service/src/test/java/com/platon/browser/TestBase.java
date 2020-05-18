package com.platon.browser;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.platon.browser.client.AccuVerifiersCount;
 
@RunWith(SpringRunner.class)
@SpringBootTest(classes= BrowserServiceApplication.class, value = "spring.profiles.active=test")
public class TestBase extends TestData{

	@Test
	public void testAccuVerifiersCount() {
		AccuVerifiersCount accuVerifiersCount = new AccuVerifiersCount();
		assertNotNull(accuVerifiersCount);
	}
}
