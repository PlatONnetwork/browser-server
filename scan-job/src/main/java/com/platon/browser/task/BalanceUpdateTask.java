package com.platon.browser.task;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.platon.browser.bean.RestrictingBalance;
import com.platon.browser.client.JobPlatOnClient;
import com.platon.browser.client.SpecialApi;
import com.platon.browser.config.TaskConfig;
import com.platon.browser.dao.entity.InternalAddress;
import com.platon.browser.dao.entity.InternalAddressExample;
import com.platon.browser.dao.mapper.InternalAddressMapper;
import com.platon.browser.enums.InternalAddressType;
import com.platon.protocol.Web3j;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Component
@Slf4j
public class BalanceUpdateTask {

    @Resource
    private InternalAddressMapper internalAddressMapper;

    @Resource
    private SpecialApi specialApi;

    @Resource(name = "jobPlatOnClient")
    private JobPlatOnClient platOnClient;

    @Resource
    private TaskConfig config;

    /**
     * 更新基金会账户余额
     * 每6分钟执行一次
     */
    @XxlJob("balanceUpdateJobHandler")
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public void updateFundAccount() {
        try {
            updateBalance(InternalAddressType.FUND_ACCOUNT);
            XxlJobHelper.handleSuccess("更新基金会账户余额完成");
        } catch (Exception e) {
            log.error("更新基金会账户余额异常", e);
            throw e;
        }
    }

    /**
     * 更新内置合约账户余额
     * 每10秒执行一次
     */
    @XxlJob("updateContractAccountJobHandler")
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public void updateContractAccount() {
        try {
            updateBalance(InternalAddressType.OTHER);
            XxlJobHelper.handleSuccess("更新内置合约账户余额完成");
        } catch (Exception e) {
            log.error("更新内置合约账户余额异常", e);
            throw e;
        }
    }

    private void updateBalance(InternalAddressType type) {
        InternalAddressExample example = new InternalAddressExample();
        switch (type) {
            case FUND_ACCOUNT:
                example.createCriteria().andTypeEqualTo(type.getCode());
                break;
            case OTHER:
                example.createCriteria().andTypeNotEqualTo(InternalAddressType.FUND_ACCOUNT.getCode());
                break;
        }
        example.setOrderByClause(" address LIMIT " + config.getMaxAddressCount());
        Instant start = Instant.now();
        List<InternalAddress> addressList = internalAddressMapper.selectByExample(example);
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        log.debug("查询地址耗时：{} ms", duration.toMillis());
        if (!addressList.isEmpty()) {
            Instant start1 = Instant.now();
            updateBalance(addressList);
            Instant end1 = Instant.now();
            Duration duration1 = Duration.between(start1, end1);
            log.debug("总更新地址耗时：{} ms", duration1.toMillis());
        } else {
            log.info("地址数为0,不做操作！");
        }
    }

    private void updateBalance(List<InternalAddress> addressList) {
        List<Map<String, InternalAddress>> batchList = new ArrayList<>();
        Map<String, InternalAddress> batch = new HashMap<>();
        batchList.add(batch);
        for (InternalAddress address : addressList) {
            if (batch.size() >= config.getMaxBatchSize()) {
                // 如果当前批次大小达到批次大小，则新建一个批次
                batch = new HashMap<>();
                batchList.add(batch);
            }
            // <地址-内部地址> 映射
            batch.put(address.getAddress(), address);
        }
        log.info("地址总数{},分成{}批,每批最多{}个地址", addressList.size(), batchList.size(), config.getMaxBatchSize());

        // 按批次查询并更新余额
        batchList.forEach(addressMap -> {
            try {
                Web3j web3j = platOnClient.getWeb3jWrapper().getWeb3j();
                Set<String> addressSet = addressMap.keySet();
                String addresses = String.join(";", addressSet);
                log.debug("锁仓余额查询参数：{}", addresses);

                Instant start = Instant.now();
                List<RestrictingBalance> balanceList = specialApi.getRestrictingBalance(web3j, addresses);
                Instant end = Instant.now();
                Duration duration = Duration.between(start, end);
                log.debug("本批次查询地址锁仓余额耗时：{} ms", duration.toMillis());

                log.debug("锁仓余额查询结果：{}", JSON.toJSONString(balanceList));
                // 设置余额
                balanceList.forEach(balance -> {
                    InternalAddress address = addressMap.get(balance.getAccount());
                    address.setBalance(new BigDecimal(balance.getFreeBalance()));
                    address.setRestrictingBalance(new BigDecimal(balance.getLockBalance().subtract(balance.getPledgeBalance())));
                });

                // 同步更新，防止表锁争用导致的死锁
                synchronized (BalanceUpdateTask.class) {
                    // 批量更新余额
                    Instant start1 = Instant.now();
                    if (CollUtil.isNotEmpty(addressMap)) {
                        for (Map.Entry<String, InternalAddress> entry : addressMap.entrySet()) {
                            internalAddressMapper.updateByPrimaryKey(entry.getValue());
                        }
                    }
                    Instant end1 = Instant.now();
                    Duration duration1 = Duration.between(start1, end1);
                    log.debug("本批次更新地址余额耗时：{} ms", duration1.toMillis());
                    log.info("地址余额批量更新成功！");
                }
            } catch (Exception e) {
                log.error("地址余额批量更新失败！", e);
            }
        });
    }

}