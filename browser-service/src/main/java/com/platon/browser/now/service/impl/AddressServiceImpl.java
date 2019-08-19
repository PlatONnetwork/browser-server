package com.platon.browser.now.service.impl;

import com.platon.browser.dao.entity.Address;
import com.platon.browser.dao.mapper.AddressMapper;
import com.platon.browser.now.service.AddressService;
import com.platon.browser.req.address.QueryDetailRequest;
import com.platon.browser.res.BaseResp;
import com.platon.browser.res.address.QueryDetailResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressMapper addressMapper;

    @Override
    public BaseResp<QueryDetailResp> getDetails(QueryDetailRequest req) {
        Address item = addressMapper.selectByPrimaryKey(req.getAddress());
        QueryDetailResp resp = new QueryDetailResp();
        if (item != null) {
            resp.setType(item.getType());
            resp.setBalance(item.getBalance());
            resp.setRestrictingBalance(item.getRestrictingBalance());
            resp.setStakingValue(item.getStakingValue());
            resp.setDelegateValue(item.getDelegateValue());
            resp.setRedeemedValue(item.getRedeemedValue());
            resp.setTxQty(item.getTxQty());
            resp.setTransferQty(item.getTransferQty());
            resp.setDelegateQty(item.getDelegateQty());
            resp.setStakingQty(item.getStakingQty());
            resp.setProposalQty(item.getProposalQty());
            resp.setCandidateCount(item.getCandidateCount());
            resp.setDelegateHes(item.getDelegateHes());
            resp.setDelegateLocked(item.getDelegateLocked());
            resp.setDelegateUnlock(item.getDelegateUnlock());
            resp.setDelegateReduction(item.getDelegateReduction());
            resp.setContractName(item.getContractName());
            resp.setContractCreate(item.getContractCreate());
            resp.setContractCreateHash(item.getContractCreatehash());
        }
        return BaseResp.build(0, "", resp);
    }
}
