package com.platon.browser.task;

import com.platon.browser.client.PlatonClient;
import com.platon.browser.client.RestrictingBalance;
import com.platon.browser.engine.AddressExecute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.platon.BaseResponse;

import java.util.List;

/**
 * User: dongqile
 * Date: 2019/8/14
 * Time: 20:23
 */
@Component
public class UpdateAccountBalanceTask {

    @Autowired
    private PlatonClient client;
    @Autowired
    private AddressExecute addressExecute;

    @Scheduled(cron="0/10 * * * * ?")
    protected void updateBalance () {
        //1.AddressExectue获取到需要新增和更新的地址列表
        //2.调用底层提供接口，查询列表中的所有
        /*Map <String,Address> addMaps = addressExecute.exportResult().getAddressMap();
        try{
            BaseResponse <List <RestrictingBalance>> updateAddBalanceList = client.getRestrictingBalance(new ArrayList <>());
        }catch (Exception e){

        }
*/
        String addreslist = "";
        try {
            BaseResponse <List<RestrictingBalance>> res = client.getRestrictingBalance(addreslist);
            List<RestrictingBalance> list = res.data;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}