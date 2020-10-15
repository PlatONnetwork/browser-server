package com.platon.browser.task;

import com.platon.browser.common.BrowserConst;
import com.platon.browser.common.utils.AppStatusUtil;
import com.platon.browser.dao.entity.Erc20TokenAddressRel;
import com.platon.browser.dao.mapper.CustomErc20TokenAddressRelMapper;
import com.platon.browser.erc.ErcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * erc20余额补充定时器
 */
@Component
@Slf4j
public class ErcBalanceUpdateTask {

    @Autowired
    protected RedisTemplate<String, String> redisTemplate;
    @Autowired
    private CustomErc20TokenAddressRelMapper customErc20TokenAddressRelMapper;
    @Autowired
    protected ErcService ercService;
    @Value("${task.erc20-batch-size:4}")
    private int batchSize;

    @Scheduled(cron = "0/5  * * * * ?")
    public void cron() {
        // 只有程序正常运行才执行任务
        if (!AppStatusUtil.isRunning()) return;
        try {
            Set<String> data = this.redisTemplate.opsForSet().members(BrowserConst.ERC_BALANCE_KEY);
            if (data == null || data.isEmpty()) {
                return;
            }
            int size = data.size();
            List<Erc20TokenAddressRel> erc20TokenAddressRelList = new ArrayList<>(data.size());
            int threadNum = this.batchSize;
            if (this.batchSize > data.size()) {
                threadNum = size;
            }
            ExecutorService EXECUTOR = Executors.newFixedThreadPool(threadNum);
            for (int i = 0; i < size; ) {
                if (size - i < this.batchSize) {
                    threadNum = size - i;
                }
                CountDownLatch countDownLatch = new CountDownLatch(threadNum);
                data.stream().forEach(d -> {
                            String[] ds = d.split(BrowserConst.ERC_SPILT);
                            EXECUTOR.submit(() -> {
                                //查询余额并回填
                                BigInteger balance = this.ercService.getBalance(ds[0], ds[1]);
                                //更新成功则删除对应的数据
                                Erc20TokenAddressRel erc20TokenAddressRel = Erc20TokenAddressRel.builder().contract(ds[0])
                                        .address(ds[1]).balance(new BigDecimal(balance)).build();
                                erc20TokenAddressRelList.add(erc20TokenAddressRel);
                                countDownLatch.countDown();
                            });
                        }
                );
                countDownLatch.await();
                i = i + threadNum;
            }

            //更新成功则删除对应的数据
            this.customErc20TokenAddressRelMapper.updateAddressBalance(erc20TokenAddressRelList);
            this.redisTemplate.opsForSet().remove(BrowserConst.ERC_BALANCE_KEY, data.toArray());
        } catch (Exception e) {
            log.error("on ErcBalanceUpdateTask error", e);
        }
    }


}