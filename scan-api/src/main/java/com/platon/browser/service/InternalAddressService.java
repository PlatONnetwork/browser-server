package com.platon.browser.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.platon.browser.bean.CountBalance;
import com.platon.browser.dao.entity.InternalAddress;
import com.platon.browser.dao.entity.InternalAddressExample;
import com.platon.browser.dao.custommapper.CustomInternalAddressMapper;
import com.platon.browser.request.PageReq;
import com.platon.browser.response.RespPage;
import com.platon.browser.response.address.InternalAddressResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class InternalAddressService {

    @Resource
    private CustomInternalAddressMapper customInternalAddressMapper;

    /**
     * 获取基金会账户列表
     *
     * @param req
     * @return com.platon.browser.response.RespPage<com.platon.browser.response.address.InternalAddressResp>
     * @date 2021/5/31
     */
    public RespPage<InternalAddressResp> getFoundationInfo(PageReq req) {
        RespPage<InternalAddressResp> respPage = new RespPage<>();
        InternalAddressResp internalAddressResp = new InternalAddressResp();
        List<CountBalance> countBalanceList = customInternalAddressMapper.countBalance();
        CountBalance foundationValue = countBalanceList.stream().filter(v -> v.getType() == 0).findFirst().orElseGet(CountBalance::new);
        internalAddressResp.setTotalBalance(foundationValue.getFree());
        internalAddressResp.setTotalRestrictingBalance(foundationValue.getLocked());
        List<InternalAddressResp> lists = new ArrayList<>();
        lists.add(internalAddressResp);
        InternalAddressExample internalAddressExample = new InternalAddressExample();
        internalAddressExample.createCriteria().andTypeEqualTo(0);
        internalAddressExample.setOrderByClause(" balance,restricting_balance desc");
        PageHelper.startPage(req.getPageNo(), req.getPageSize());
        Page<InternalAddress> InternalAddressList = customInternalAddressMapper.selectListByExample(internalAddressExample);
        internalAddressResp.setInternalAddressBaseResp(InternalAddressList);
        respPage.init(InternalAddressList, lists);
        return respPage;
    }

}
