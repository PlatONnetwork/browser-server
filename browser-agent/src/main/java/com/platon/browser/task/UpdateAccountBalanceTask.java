package com.platon.browser.task;

import com.platon.browser.dao.entity.Address;
import com.platon.browser.engine.AddressExecute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * User: dongqile
 * Date: 2019/8/14
 * Time: 20:23
 */
@Component
public class UpdateAccountBalanceTask {

    @Autowired
    private AddressExecute addressExecute;

    @Scheduled(cron="0/10 * * * * ?")
    protected void updateBalance () {
        //1.AddressExectue获取到需要新增和更新的地址列表
        //2.调用底层提供接口，查询列表中的所有
        Set <Address> updateAddressSet =  addressExecute.exportResult().getUpdateAddress();
        Set<Address> addAddressSet = addressExecute.exportResult().getAddAddress();

    }
}