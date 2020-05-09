package com.platon.browser.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NetworkParms {

	private static Long chainId;

	public static Long getChainId() {
		return chainId;
	}

	/**
	 * 默认主网链id100
	 * @param chainId
	 */
	@Value("${platon.chainId:100}")
	public void setChainId(Long chainId) {
		NetworkParms.chainId = chainId;
	}
	
	
}
