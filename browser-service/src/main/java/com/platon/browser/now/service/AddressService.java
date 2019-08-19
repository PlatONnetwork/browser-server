package com.platon.browser.now.service;

import com.platon.browser.req.address.QueryDetailRequest;
import com.platon.browser.res.BaseResp;
import com.platon.browser.res.address.QueryDetailResp;

public interface AddressService {
    BaseResp<QueryDetailResp> getDetails(QueryDetailRequest req);
}
