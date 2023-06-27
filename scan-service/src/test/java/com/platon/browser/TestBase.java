package com.platon.browser;

import org.springframework.boot.test.context.SpringBootTest;


//@RunWith(SpringRunner.class)
@SpringBootTest(classes= BrowserServiceApplication.class, value = "spring.profiles.active=test")
public class TestBase extends TestData{

//	@Test
//	public void testAccuVerifiersCount() {
//		AccuVerifiersCount accuVerifiersCount = new AccuVerifiersCount();
//		assertNotNull(accuVerifiersCount);
//	}
}
