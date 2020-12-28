package com.platon.browser.dao.param.ppos;

import com.platon.browser.TestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class RestrictingCreateTest extends TestBase {

	@Test
	public void test(){
		RestrictingCreate target = RestrictingCreate.builder()
				.itemList(Collections.emptyList())
				.build();
		target.setItemList(Collections.emptyList());
		target.getItemList();
		target.getBusinessType();
		assertTrue(true);
	}
}
