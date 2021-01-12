package com.platon.browser.service;

import com.platon.browser.ApiTestMockBase;
import com.platon.browser.dao.entity.Erc20TokenDetailWithBLOBs;
import com.platon.browser.dao.mapper.Erc20TokenDetailMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class Erc20TokenDetailServiceApiTest extends ApiTestMockBase {

	@Mock
	private Erc20TokenDetailMapper erc20TokenDetailMapper;
	@InjectMocks
	@Spy
	private Erc20TokenDetailService target;

	@Before
	public void setup() {

	}

	@Test
	public void test_save() {
		this.target.save(new Erc20TokenDetailWithBLOBs());
		this.target.batchSave(new ArrayList<>());
		assertTrue(true);
	}


}
