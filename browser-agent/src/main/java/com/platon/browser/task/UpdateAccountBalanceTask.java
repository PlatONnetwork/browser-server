package com.platon.browser.task;

import com.platon.browser.client.PlatonClient;
import com.platon.browser.client.RestrictingBalance;
import com.platon.browser.engine.BlockChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.platon.BaseResponse;

import java.util.List;
import java.util.Set;

/**
 * User: dongqile
 * Date: 2019/8/14
 * Time: 20:23
 */
@Component
public class UpdateAccountBalanceTask {

    @Autowired
    private PlatonClient client;

    //@Scheduled(cron = "0/10 * * * * ?")
    protected void updateBalance () {
        String resAdd = "";
        Set <String> addresSet = BlockChain.ADDRESS_CACHE.getAll().keySet();
        if (addresSet.size() > 0) {
            for (String s : addresSet) {
                if (!resAdd.equals("")) {
                    resAdd = resAdd + ";" + s;
                } else {
                    resAdd = s + ";";
                }
            }
            try {
                BaseResponse <List <RestrictingBalance>> res = client.getRestrictingBalance(resAdd);
                List <RestrictingBalance> list = res.data;
                BlockChain.ADDRESS_CACHE.getAll().values().forEach(address->{
                    list.forEach(restrictingBalance -> {
                        if(restrictingBalance.getAccount().equals(address.getAddress())){
                            address.setRestrictingBalance(restrictingBalance.getLockBalance().toString());
                            address.setBalance(restrictingBalance.getFreeBalance().toString());
                        }
                    });
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
}
