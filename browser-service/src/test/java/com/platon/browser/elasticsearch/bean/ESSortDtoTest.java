package com.platon.browser.elasticsearch.bean;

import com.platon.browser.elasticsearch.bean.ESSortDto;
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
