package com.platon.browser.service.elasticsearch.query;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ESSimpleExpressionTest {

    @Test
    public void test(){

        ESSimpleExpression ee = new ESSimpleExpression("test", ESCriterion.Operator.FUZZY);
        ReflectionTestUtils.setField(ee,"fieldName","test");
        ee.toBuilder();

        ee = new ESSimpleExpression("test", ESCriterion.Operator.QUERY_STRING);
        ReflectionTestUtils.setField(ee,"fieldName","test");
        ee.toBuilder();
        assertTrue(true);
    }
}
