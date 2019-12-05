package com.platon.browser.elasticsearch;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.Silent.class)
public class RedisBlockServiceTest {
    final class CDM{}
	
	@Test
	public void testTetCacheKey() {
		CDM cdm = mock(CDM.class);
        System.out.println(cdm);
	}
	
}
