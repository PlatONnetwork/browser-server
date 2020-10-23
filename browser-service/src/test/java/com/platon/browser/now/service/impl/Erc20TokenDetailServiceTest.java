package com.platon.browser.now.service.impl;

import com.platon.browser.TestMockBase;
import com.platon.browser.dao.entity.Erc20TokenDetailWithBLOBs;
import com.platon.browser.dao.mapper.Erc20TokenDetailMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class Erc20TokenDetailServiceTest extends TestMockBase {

	@Mock
	private Erc20TokenDetailMapper erc20TokenDetailMapper;

	@Spy
	private Erc20TokenDetailServiceImpl target;

	@Before
	public void setup() {
		ReflectionTestUtils.setField(this.target, "erc20TokenDetailMapper", this.erc20TokenDetailMapper);

	}

	@Test
	public void test_save() {
		this.target.save(new Erc20TokenDetailWithBLOBs());
		this.target.batchSave(new ArrayList<>());
		assertTrue(true);
	}


}
