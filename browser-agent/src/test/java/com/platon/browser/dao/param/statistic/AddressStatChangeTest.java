package com.platon.browser.dao.param.statistic;

import com.platon.browser.TestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AddressStatChangeTest extends TestBase {

	@Test
	public void test(){
		AddressStatChange target = AddressStatChange.builder()
				.addressStatItemList(Collections.emptyList())
				.build();
		target.setAddressStatItemList(Collections.emptyList());
		target.getAddressStatItemList();
		target.getBusinessType();
		assertTrue(true);
	}
}
