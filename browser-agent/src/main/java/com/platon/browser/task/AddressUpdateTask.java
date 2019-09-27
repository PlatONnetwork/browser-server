package com.platon.browser.task;

import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.RestrictingBalance;
import com.platon.browser.client.SpecialContractApi;
import com.platon.browser.dto.CustomAddress;
import com.platon.browser.engine.cache.CacheHolder;
import com.platon.browser.exception.GracefullyShutdownException;
import com.platon.browser.task.bean.TaskAddress;
import com.platon.browser.task.cache.AddressTaskCache;
import com.platon.browser.exception.ContractInvokeException;
import com.platon.browser.util.GracefullyUtil;
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
public class AddressUpdateTask extends BaseTask{
    private static Logger logger = LoggerFactory.getLogger(AddressUpdateTask.class);
    @Autowired
    private PlatOnClient client;
    @Autowired
    private SpecialContractApi sca;
    @Autowired
    private CacheHolder cacheHolder;

    @Autowired
    private AddressTaskCache taskCache;

    @Scheduled(cron = "0/10 * * * * ?")
    private void cron () {start();}

    private Map<String,RestrictingBalance> balanceMap = new HashMap<>();

    public void start(){

        try {
            // 监控应用状态
            GracefullyUtil.monitor(this);
        } catch (GracefullyShutdownException e) {
            Thread.currentThread().interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Collection<CustomAddress> addresses = getAllAddress();
        if(addresses.isEmpty()) return;
        try {
            batchQueryBalance(addresses);
            addresses.forEach(address->{
                RestrictingBalance rb = balanceMap.get(address.getAddress());
                if(rb!=null){
                    TaskAddress cache = new TaskAddress();
                    // 设置缓存主键
                    cache.setAddress(address.getAddress());
                    // 查看缓存中地址的属性值是否和查询回来的值有出入，有变动才需要更新, 防止大批量数据更新
                    String restrictingBalance=(rb.getLockBalance()!=null && rb.getPledgeBalance()!=null)?rb.getLockBalance().subtract(rb.getPledgeBalance()).toString():"0";
                    if (!restrictingBalance.equals(address.getRestrictingBalance())){
                        cache.setRestrictingBalance(restrictingBalance);
                        taskCache.update(cache);
                    }
                    String balance = (rb.getFreeBalance()!=null)?rb.getFreeBalance().toString():"0";
                    if(!balance.equals(address.getBalance())){
                        cache.setBalance(balance);
                        taskCache.update(cache);
                    }
                }
            });
        } catch (Exception e) {
            logger.error("锁仓合约查询余额出错:",e);
        }
        balanceMap.clear();
        // 清除已合并的任务缓存
        taskCache.sweep();
    }

    /**
     * 批量查询地址锁仓余额
     * @param addresses
     * @throws ContractInvokeException
     */
    public void batchQueryBalance(Collection<CustomAddress> addresses) throws ContractInvokeException, GracefullyShutdownException, InterruptedException {
        balanceMap.clear();
        // 为防止RPC调用请求体超出限制，需要对地址分批查询: 每200个地址查询一次
        StringBuilder sb = new StringBuilder();
        int addressCount=0; // 地址索引
        int batchEleCount = 0; // 批次内元素个数计数
        for (CustomAddress address:addresses){
            sb.append(address.getAddress());
            batchEleCount++;
            addressCount++;
            if(batchEleCount==200||(addressCount==addresses.size())){
                // 如果达到批次指定的数量或达到地址列表最后一个元素，则执行批量查询
                List<RestrictingBalance> balanceList = getRestrictingBalance(sb.toString());
                balanceList.forEach(rb->balanceMap.put(rb.getAccount(),rb));
                batchEleCount=0; // 重置计数器
                sb.setLength(0); // 重置参数
            }else{
                sb.append(";");
            }
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
