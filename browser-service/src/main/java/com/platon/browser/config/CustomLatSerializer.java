package com.platon.browser.config;

import java.io.IOException;
import java.math.RoundingMode;

import org.web3j.utils.Convert;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.platon.browser.util.EnergonUtil;


public class CustomLatSerializer  extends JsonSerializer<String>{

	@Override
	public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		String transEner = EnergonUtil.format(Convert.fromVon(value, Convert.Unit.LAT).setScale(18,RoundingMode.DOWN), 18);
		gen.writeString(transEner);
	}

}
