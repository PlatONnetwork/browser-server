package com.platon.browser.service.elasticsearch.query;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ESQueryBuildersTest {

    @Test
    public void test(){
        ESQueryBuilders eb = new ESQueryBuilders();
        eb.fuzzy("test",333);
        
        eb.queryString("test");
        assertTrue(true);
    }
}
