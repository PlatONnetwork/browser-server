package com.platon.browser.task;

import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.RestrictingBalance;
import com.platon.browser.client.SpecialContractApi;
import com.platon.browser.dao.entity.Address;
import com.platon.browser.dao.mapper.CustomAddressMapper;
import com.platon.browser.dto.CustomAddress;
import com.platon.browser.engine.cache.CacheHolder;
import com.platon.browser.engine.stage.AddressStage;
import com.platon.browser.exception.ContractInvokeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: dongqile
 * @Date: 2019/8/17 20:09
 * @Description: 地址更新任务
 */
@Component
public class AddressUpdateTask {
    private static Logger logger = LoggerFactory.getLogger(AddressUpdateTask.class);
    @Autowired
    private PlatOnClient client;
    @Autowired
    private SpecialContractApi sca;
    @Autowired
    private CacheHolder cacheHolder;
    @Autowired
    private CustomAddressMapper customAddressMapper;

    private AddressStage addressStage = new AddressStage();

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
                    // 查看缓存中地址的属性值是否和查询回来的值有出入，有变动才需要更新, 防止大批量数据更新
                    String restrictingBalance=(rb.getLockBalance()!=null && rb.getPledgeBalance()!=null)?rb.getLockBalance().subtract(rb.getPledgeBalance()).toString():"0";
                    if (!restrictingBalance.equals(address.getRestrictingBalance())){
                        address.setRestrictingBalance(restrictingBalance);
                        addressStage.updateAddress(address);
                    }
                    String balance = (rb.getFreeBalance()!=null)?rb.getFreeBalance().toString():"0";
                    if(!balance.equals(address.getBalance())){
                        address.setBalance(balance);
                        addressStage.updateAddress(address);
                    }
                }
            });
            if(!addressStage.exportAddress().isEmpty()){
                customAddressMapper.batchInsertOrUpdateSelective(addressStage.exportAddress(), Address.Column.values());
                addressStage.clear();
            }
        } catch (Exception e) {
            logger.error("锁仓合约查询余额出错:{}",e.getMessage());
        }
    }

    /**
     * 取缓存全量地址
     * @return
     */
    public Collection<CustomAddress> getAllAddress(){
        return cacheHolder.getAddressCache().getAllAddress();
    }

    /**
     * 取锁仓余额信息
     * @param params
     * @return
     * @throws Exception
     */
    public List<RestrictingBalance> getRestrictingBalance(String params) throws ContractInvokeException {
        return sca.getRestrictingBalance(client.getWeb3j(),params);
    }
}
