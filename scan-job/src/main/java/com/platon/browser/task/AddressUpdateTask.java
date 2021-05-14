package com.platon.browser.task;

import com.platon.browser.InternalAddressType;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.SpecialApi;
import com.platon.browser.client.Web3jWrapper;
import com.platon.browser.dao.entity.InternalAddress;
import com.platon.browser.dao.entity.InternalAddressExample;
import com.platon.browser.dao.mapper.InternalAddressMapper;
import com.platon.browser.enums.AddressTypeEnum;
import com.platon.browser.utils.AppStatusUtil;
import com.platon.protocol.Web3j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
@Slf4j
public class AddressUpdateTask {

    @Resource
    private InternalAddressMapper addressMapper;

    @Resource
    private SpecialApi specialApi;

    @Resource
    private PlatOnClient platOnClient;

    /**
     * 更新基金会账户余额
     */
    @Scheduled(cron = "0/5  * * * * ?")
    public void updateFundAccount() {
       updateBalance(InternalAddressType.FUND_ACCOUNT);
    }
    /**
     * 更新内置合约账户余额
     */
    @Scheduled(cron = "0/10  * * * * ?")
    public void updateContractAccount() {
        updateBalance(InternalAddressType.OTHER);
    }

    private void updateBalance(InternalAddressType type){
        InternalAddressExample example = new InternalAddressExample();
        switch (type){
            case FUND_ACCOUNT:
                example.createCriteria().andTypeEqualTo(type.getCode());
                break;
            case OTHER:
                example.createCriteria().andTypeNotEqualTo(InternalAddressType.FUND_ACCOUNT.getCode());
                break;
        }
        example.setOrderByClause(" LIMIT 5000 ");
        List<InternalAddress> addressList = addressMapper.selectByExample(example);
        updateBalance(addressList);
    }


    private void updateBalance(List<InternalAddress> addressList){
        Web3j web3j = platOnClient.getWeb3jWrapper().getWeb3j();
        //specialApi.getRestrictingBalance(web3j,)
    }
}