package com.platon.browser.task;

import com.platon.browser.common.BrowserConst;
import com.platon.browser.utils.AppStatusUtil;
import com.platon.browser.dao.mapper.SyncTokenInfoMapper;
import com.platon.browser.dao.entity.Erc20Token;
import com.platon.browser.dao.entity.Erc20TokenAddressRel;
import com.platon.browser.dao.mapper.CustomErc20TokenAddressRelMapper;
import com.platon.browser.dao.mapper.Erc20TokenMapper;
import com.platon.browser.erc.ErcService;
import com.platon.browser.service.erc20.Erc20ServiceImpl;
import com.platon.browser.param.sync.TotalSupplyUpdateParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
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
public class ErcTokenUpdateTask {

    @Resource
    protected RedisTemplate<String, String> redisTemplate;
    @Resource
    private CustomErc20TokenAddressRelMapper customErc20TokenAddressRelMapper;
    @Resource
    private Erc20TokenMapper erc20TokenMapper;
    @Resource
    private SyncTokenInfoMapper syncTokenInfoMapper;
    @Resource
    protected ErcService ercService;
    @Resource
    private Erc20ServiceImpl erc20ServiceImpl;

    @Value("${task.erc20-batch-size:4}")
    private int batchSize;
    private ExecutorService EXECUTOR = Executors.newFixedThreadPool(30);

    @Scheduled(cron = "0/5  * * * * ?")
    public void cron() {
        // 只有程序正常运行才执行任务
        if (!AppStatusUtil.isRunning()) return;
        // 1、更新erc20_token_address_rel表的balance字段
        try {
            Set<String> data = this.redisTemplate.opsForSet().members(BrowserConst.ERC_BALANCE_KEY);
            if (data != null && !data.isEmpty()) {
                int size = data.size();
                List<Erc20TokenAddressRel> erc20TokenAddressRelList = Collections.synchronizedList(new ArrayList<>(size));
                CountDownLatch countDownLatch = new CountDownLatch(size);
                data.forEach(d->{
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
                });
                countDownLatch.await();

                //更新成功则删除对应的数据
                this.customErc20TokenAddressRelMapper.updateAddressBalance(erc20TokenAddressRelList);
                this.redisTemplate.opsForSet().remove(BrowserConst.ERC_BALANCE_KEY, data.toArray());
            }
        } catch (Exception e) {
            log.error("on ErcTokenUpdateTask error", e);
        }

        // 2、更新erc20_token表的totalSupply
        try {
            List<Erc20Token> tokens = erc20TokenMapper.selectByExample(null);
            if (tokens != null && !tokens.isEmpty()) {
                int size = tokens.size();
                List<TotalSupplyUpdateParam> params = Collections.synchronizedList(new ArrayList<>(size));
                CountDownLatch countDownLatch = new CountDownLatch(size);
                tokens.forEach(token->{
                    EXECUTOR.submit(() -> {
                        //查询总供应量
                        BigInteger totalSupply = erc20ServiceImpl.getTotalSupply(token.getAddress());
                        totalSupply = totalSupply==null?BigInteger.ZERO:totalSupply;
                        TotalSupplyUpdateParam tsp = new TotalSupplyUpdateParam();
                        tsp.setAddress(token.getAddress());
                        tsp.setTotalSupply(new BigDecimal(totalSupply));
                        params.add(tsp);
                        countDownLatch.countDown();
                    });
                });
                countDownLatch.await();
                syncTokenInfoMapper.updateTotalSupply(params);
            }
        } catch (Exception e) {
            log.error("on ErcTokenUpdateTask error", e);
        }
    }
}