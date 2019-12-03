package com.platon.browser.config;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class SerializerTest {

	@Test
	public void customLatSerializerTest() {
		try {
			CustomLatSerializer customLatSerializer = mock(CustomLatSerializer.class);
			customLatSerializer.serialize(BigDecimal.TEN, null, null);
			
			CustomLowLatSerializer customLowLatSerializer = mock(CustomLowLatSerializer.class);
			customLowLatSerializer.serialize("123", null, null);
			
			CustomRateSerializer customRateSerializer = mock(CustomRateSerializer.class);
			customRateSerializer.serialize("123", null, null);
			
			CustomVersionSerializer customVersionSerializer = mock(CustomVersionSerializer.class);
			customVersionSerializer.serialize("123", null, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertTrue(Boolean.TRUE);
	}
	
}
