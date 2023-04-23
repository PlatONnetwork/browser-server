package com.platon.browser.config.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.Silent.class)
public class CustomRateSerializerTest {

    @Mock
    private JsonGenerator generator;
    @Mock
    private SerializerProvider serializers;
    @Spy
    private CustomRateSerializer target;

    @Test
    public void test() throws IOException {
        target.serialize("878",generator,serializers);
        target.serialize(null,generator,serializers);
        verify(target, times(2)).serialize(any(),any(),any());
    }
}
