package com.platon.browser.service.elasticsearch.bean;

import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ESSortDtoTest {
    @Test
    public void test(){
        ESSortDto esSortDto = new ESSortDto("", SortOrder.ASC);
        esSortDto = new ESSortDto();
        assertTrue(true);
    }
}
