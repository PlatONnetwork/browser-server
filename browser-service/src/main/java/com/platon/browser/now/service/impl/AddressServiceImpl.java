package com.platon.browser.now.service.impl;

import com.platon.browser.dao.entity.Address;
import com.platon.browser.dao.mapper.AddressMapper;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.now.service.AddressService;
import com.platon.browser.req.address.QueryDetailRequest;

import com.platon.browser.res.BaseResp;
import com.platon.browser.res.address.QueryDetailResp;
import com.platon.browser.util.EnergonUtil;
import com.platon.browser.util.I18nUtil;

import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.utils.Convert;

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
            resp.setType(item.getType());
            resp.setBalance(EnergonUtil.format(Convert.fromVon(item.getBalance(), Convert.Unit.LAT).setScale(18,RoundingMode.DOWN)));
            resp.setRestrictingBalance(EnergonUtil.format(Convert.fromVon(item.getRestrictingBalance(), Convert.Unit.LAT).setScale(18,RoundingMode.DOWN)));
            resp.setStakingValue(item.getStakingValue());
            resp.setDelegateValue(item.getDelegateValue());
            resp.setRedeemedValue(item.getRedeemedValue());
            resp.setTxQty(item.getTxQty());
            resp.setTransferQty(item.getTransferQty());
            resp.setDelegateQty(item.getDelegateQty());
            resp.setStakingQty(item.getStakingQty());
            resp.setProposalQty(item.getProposalQty());
            resp.setCandidateCount(item.getCandidateCount());
            resp.setDelegateHes(EnergonUtil.format(Convert.fromVon(item.getDelegateHes(), Convert.Unit.LAT).setScale(18,RoundingMode.DOWN)));
            resp.setDelegateLocked(EnergonUtil.format(Convert.fromVon(item.getDelegateLocked(), Convert.Unit.LAT).setScale(18,RoundingMode.DOWN)));
            resp.setDelegateUnlock(EnergonUtil.format(Convert.fromVon(item.getDelegateUnlock(), Convert.Unit.LAT).setScale(18,RoundingMode.DOWN)));
            resp.setDelegateReduction(EnergonUtil.format(Convert.fromVon(item.getDelegateReduction(), Convert.Unit.LAT).setScale(18,RoundingMode.DOWN)));
            resp.setContractName(item.getContractName());
            resp.setContractCreate(item.getContractCreate());
            resp.setContractCreateHash(item.getContractCreatehash());
        }
        return BaseResp.build(RetEnum.RET_SUCCESS.getCode(), i18n.i(I18nEnum.SUCCESS), resp);
    }
}
