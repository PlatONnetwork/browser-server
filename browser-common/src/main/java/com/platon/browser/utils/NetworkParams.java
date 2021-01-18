package com.platon.browser.utils;

import com.platon.parameters.NetworkParameters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NetworkParams {

	private static Long chainId;

	public static Long getChainId() {
		if(chainId == null) {
			return NetworkParameters.getChainId();
		}
		return chainId;
	}

	/**
	 * 默认主网链id100
	 * @param chainId
	 */
	@Value("${platon.chainId:100}")
	public void setChainId(String chainId) {
		NetworkParams.chainId = Long.valueOf(chainId);
	}
	
	
}
