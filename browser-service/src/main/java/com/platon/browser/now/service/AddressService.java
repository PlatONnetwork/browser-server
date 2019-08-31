package com.platon.browser.now.service;

import com.platon.browser.req.address.QueryDetailRequest;
import com.platon.browser.res.BaseResp;
import com.platon.browser.res.address.QueryDetailResp;

/**
 *  地址服务
 *  @file AddressService.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public interface AddressService {

	/**
	 * 根据钱包地址查询对应信息
	 * @method getDetails
	 */
	public BaseResp<QueryDetailResp> getDetails(QueryDetailRequest req);
}
