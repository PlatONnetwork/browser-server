package com.platon.browser.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NetworkParms {

	private static Long chainId;

	public static Long getChainId() {
		if(chainId == null) {
			return 100l;
		}
		return chainId;
	}

	/**
	 * 默认主网链id100
	 * @param chainId
	 */
	@Value("${platon.chainId:100}")
	public void setChainId(String chainId) {
		NetworkParms.chainId = Long.valueOf(chainId);
	}
	
	
}
