package com.platon.browser.task;

import com.platon.browser.bean.RestrictingBalance;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.SpecialApi;
import com.platon.browser.dao.custommapper.CustomInternalAddressMapper;
import com.platon.browser.dao.entity.InternalAddress;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.dao.mapper.NetworkStatMapper;
import com.platon.browser.enums.InternalAddressType;
import com.platon.browser.utils.AppStatusUtil;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class BalanceUpdateTask {
    @Resource
    private CustomInternalAddressMapper customInternalAddressMapper;
    @Resource
    private SpecialApi specialApi;

    @Resource
    private PlatOnClient platOnClient;

    @Resource
    private NetworkStatMapper networkStatMapper;
    /**
     * 更新基金会账户余额
     * 每6分钟执行一次
     */
    @XxlJob("balanceUpdateJobHandler")
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public void updateFundAccount() {

        // 只有程序正常运行才执行任务
        if (!AppStatusUtil.isRunning()) {
            return;
        }

        log.debug("开始执行:更新基金会账户余额任务");
        StopWatch watch = new StopWatch();
        watch.start("更新基金会账户余额任务");

        try {
            updateBalanceAndRestrictingBalance(InternalAddressType.FUND_ACCOUNT);
            XxlJobHelper.handleSuccess("更新基金会账户余额完成");
        } catch (Exception e) {
            log.error("更新基金会账户余额任务异常", e);
        }

        watch.stop();
        log.debug("结束执行:更新基金会账户余额任务, 耗时：{}ms", watch.getLastTaskTimeMillis());
    }



    /**
     * 更新内置合约账户余额
     * 每10秒执行一次
     */
    @XxlJob("updateContractAccountJobHandler")
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public void updateContractAccount() {

        // 只有程序正常运行才执行任务
        if (!AppStatusUtil.isRunning()) {
            return;
        }

        log.debug("开始执行:更新内置合约账户余额任务");

        StopWatch watch = new StopWatch();
        watch.start("更新内置合约账户余额任务");
        try {
            updateBalanceAndRestrictingBalance(InternalAddressType.OTHER);
            XxlJobHelper.handleSuccess("更新内置合约账户余额完成");
        } catch (Exception e) {
            log.error("更新内置合约账户余额任务异常", e);
        }
        watch.stop();
        log.debug("结束执行:更新内置合约账户余额任务, 耗时：{}ms", watch.getLastTaskTimeMillis());
    }

    private void updateBalanceAndRestrictingBalance(InternalAddressType type){

        NetworkStat networkStat = networkStatMapper.selectByPrimaryKey(1);
        Long currentBlockNumber = 0L;
        if (networkStat!=null){
            currentBlockNumber = networkStat.getCurNumber();
        }

        //Web3j web3j = platOnClient.getWeb3jWrapper().getWeb3j();

        int pageNo = 1;
        int pageSize = 100;
        int offset = 0;
        int countReturned = 0;
        do{
            offset = (pageNo-1) * pageSize;
            List<InternalAddress> internalAddressList = customInternalAddressMapper.listInternalAddress(type.getCode(), offset, pageSize);

            countReturned = internalAddressList.size();

            if (countReturned ==0){
                break;
            }

            Map<String, InternalAddress> internalAddressMap = internalAddressList.stream().collect(Collectors.toMap(InternalAddress::getAddress, Function.identity()));

            // Set<String> internalAddressSet = internalAddressList.stream().map(InternalAddress::getAddress).collect(Collectors.toSet());
            String addresses = String.join(";", internalAddressMap.keySet());

            try {
                List<RestrictingBalance> balanceList = specialApi.getRestrictingBalance(platOnClient.getWeb3jWrapper(), addresses, currentBlockNumber);
                balanceList.stream().forEach(balance -> {
                    InternalAddress internalAddress = internalAddressMap.get(balance.getAccount());
                    if (internalAddress != null) {
                        internalAddress.setBalance(new BigDecimal(balance.getFreeBalance()));
                        internalAddress.setRestrictingBalance(new BigDecimal(balance.getRestrictingPlanLockedAmount().subtract(balance.getRestrictingPlanPledgeAmount())));
                    }
                });
            }catch (Exception e){
                log.error("查询批量地址余额出错", e);
            }
            customInternalAddressMapper.updateBalanceAndRestrictingBalance(internalAddressMap.values());
            pageNo++;
        }while(countReturned == pageSize);
    }
}
