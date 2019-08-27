package com.platon.browser.now.service.impl;

import com.platon.browser.dao.entity.Address;
import com.platon.browser.dao.mapper.AddressMapper;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.now.service.AddressService;
import com.platon.browser.req.address.QueryDetailRequest;

import com.platon.browser.res.BaseResp;
import com.platon.browser.res.address.QueryDetailResp;
import com.platon.browser.util.I18nUtil;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private I18nUtil i18n;

    @Override
    public BaseResp<QueryDetailResp> getDetails(QueryDetailRequest req) {
        Address item = addressMapper.selectByPrimaryKey(req.getAddress());
        QueryDetailResp resp = new QueryDetailResp();
        if (item != null) {
        	BeanUtils.copyProperties(item, resp);
        }
        return BaseResp.build(RetEnum.RET_SUCCESS.getCode(), i18n.i(I18nEnum.SUCCESS), resp);
    }
}
