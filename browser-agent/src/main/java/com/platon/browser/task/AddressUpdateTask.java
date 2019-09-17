package com.platon.browser.task;

import com.platon.browser.client.PlatonClient;
import com.platon.browser.client.RestrictingBalance;
import com.platon.browser.client.SpecialContractApi;
import com.platon.browser.dto.CustomAddress;
import com.platon.browser.engine.BlockChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.platon.browser.engine.util.CacheTool.ADDRESS_CACHE;
import static com.platon.browser.engine.util.CacheTool.STAGE_DATA;

/**
 * @Auther: dongqile
 * @Date: 2019/8/17 20:09
 * @Description: 地址更新任务
 */
@Component
public class AddressUpdateTask {
    private static Logger logger = LoggerFactory.getLogger(AddressUpdateTask.class);
    @Autowired
    private PlatonClient client;
    @Autowired
    private SpecialContractApi sca;

    @Scheduled(cron = "0/10 * * * * ?")
    private void cron () {start();}

    public void start(){
        StringBuilder sb = new StringBuilder();
        Collection<CustomAddress> addresses = getAllAddress();
        if(addresses.isEmpty()) return;
        addresses.forEach(address -> sb.append(address.getAddress()).append(";"));
        String params = sb.toString().substring(0,sb.lastIndexOf(";"));
        try {
            List<RestrictingBalance> data = getRestrictingBalance(params);
            Map<String,RestrictingBalance> map = new HashMap<>();
            data.forEach(rb->map.put(rb.getAccount(),rb));
            addresses.forEach(address->{
                RestrictingBalance rb = map.get(address.getAddress());
                if(rb!=null){
                    address.setRestrictingBalance(rb.getLockBalance()!=null && rb.getPledgeBalance()!=null?rb.getLockBalance().subtract(rb.getPledgeBalance()).toString():"0");
                    address.setBalance(rb.getFreeBalance()!=null?rb.getFreeBalance().toString():"0");
                    // 把改动后的内容暂存至待更新列表
                    STAGE_DATA.getAddressStage().updateAddress(address);
                }
            });
        } catch (Exception e) {
            logger.error("锁仓合约查询余额出错:{}",e.getMessage());
        }
    }

    /**
     * 取缓存全量地址
     * @return
     */
    public Collection<CustomAddress> getAllAddress(){
        return ADDRESS_CACHE.getAllAddress();
    }

    /**
     * 取锁仓余额信息
     * @param params
     * @return
     * @throws Exception
     */
    public List<RestrictingBalance> getRestrictingBalance(String params) throws Exception {
        return sca.getRestrictingBalance(client.getWeb3j(),params);
    }
}
