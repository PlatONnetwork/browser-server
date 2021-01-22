package com.platon.browser.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.alibaba.fastjson.JSON;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.platon.browser.dao.entity.Token;
import com.platon.browser.dao.entity.TokenHolder;
import com.platon.browser.dao.entity.TokenInventory;
import com.platon.browser.dao.entity.TokenInventoryExample;
import com.platon.browser.dao.mapper.SyncTokenInfoMapper;
import com.platon.browser.dao.mapper.TokenInventoryMapper;
import com.platon.browser.dao.mapper.TokenMapper;
import com.platon.browser.param.sync.TotalSupplyUpdateParam;
import com.platon.browser.service.erc.ErcServiceImpl;
import com.platon.browser.task.bean.TokenHolderNum;
import com.platon.browser.task.bean.TokenHolderType;
import com.platon.browser.utils.AppStatusUtil;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

/**
 * token定时器
 *
 * @author huangyongpeng@matrixelements.com
 * @date 2021/1/22
 */
@Component
public class ErcTokenUpdateTask {

    private static final Log log = LogFactory.get();

    @Resource
    private TokenMapper tokenMapper;

    @Resource
    private SyncTokenInfoMapper syncTokenInfoMapper;

    @Resource
    private TokenInventoryMapper tokenInventoryMapper;

    @Resource
    private ErcServiceImpl ercServiceImpl;

    /**
     * 线程名前缀
     */
    private final static String ThreadFactoryName = "token-task-pool-";

    private final ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
            .setNameFormat(ThreadFactoryName + "%d").build();

    private final ExecutorService pool = new ThreadPoolExecutor(30, 30,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

    private final static OkHttpClient client = new OkHttpClient.Builder()
            .connectionPool(new ConnectionPool(50, 5, TimeUnit.MINUTES))
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build();

    /**
     * 余额补充定时器
     *
     * @return void
     * @author huangyongpeng@matrixelements.com
     * @date 2021/1/18
     */
    @Scheduled(cron = "0/30  * * * * ?")
    public void cron() {
        // 只有程序正常运行才执行任务
        if (!AppStatusUtil.isRunning()) {
            return;
        }
        // 更新token_holder表的balance字段
        try {
            List<TokenHolderType> tokenTypeList = syncTokenInfoMapper.findTokenHolderType();
            if (CollUtil.isNotEmpty(tokenTypeList)) {
                int size = tokenTypeList.size();
                List<TokenHolder> tokenHolderList = Collections.synchronizedList(new ArrayList<>(size));
                CountDownLatch countDownLatch = new CountDownLatch(size);
                tokenTypeList.forEach(d -> {
                    pool.submit(() -> {
                        // 查询余额并回填
                        BigInteger balance = ercServiceImpl.getBalance(d.getTokenAddress(), d.getType(), d.getAddress());
                        d.setBalance(new BigDecimal(balance));
                        d.setUpdateTime(DateUtil.date());
                        tokenHolderList.add(d);
                        countDownLatch.countDown();
                    });
                });
                countDownLatch.await();
                if (CollUtil.isNotEmpty(tokenHolderList)) {
                    this.syncTokenInfoMapper.updateAddressBalance(tokenHolderList);
                }
            }
        } catch (Exception e) {
            log.error(e, "更新地址代币余额异常");
        }

        // 更新token表的total_supply字段
        try {
            List<Token> tokens = tokenMapper.selectByExample(null);
            if (CollUtil.isNotEmpty(tokens)) {
                int size = tokens.size();
                List<TotalSupplyUpdateParam> params = Collections.synchronizedList(new ArrayList<>(size));
                CountDownLatch countDownLatch = new CountDownLatch(size);
                tokens.forEach(token -> {
                    pool.submit(() -> {
                        // 查询总供应量
                        BigInteger totalSupply = ercServiceImpl.getTotalSupply(token.getAddress());
                        totalSupply = totalSupply == null ? BigInteger.ZERO : totalSupply;
                        TotalSupplyUpdateParam tsp = new TotalSupplyUpdateParam();
                        tsp.setAddress(token.getAddress());
                        tsp.setTotalSupply(new BigDecimal(totalSupply));
                        tsp.setUpdateTime(DateUtil.date());
                        params.add(tsp);
                        countDownLatch.countDown();
                    });
                });
                countDownLatch.await();
                if (CollUtil.isNotEmpty(params)) {
                    syncTokenInfoMapper.updateTokenTotalSupply(params);
                }
            }
        } catch (Exception e) {
            log.error(e, "定期更新Erc供应总量(包含erc20和erc721)异常");
        }

        //定期更新token_inventory
        try {
            TokenInventoryExample example = new TokenInventoryExample();
            example.createCriteria().andNameIsNull()
                    .andDescriptionIsNull()
                    .andImageIsNull();
            List<TokenInventory> tokenInventoryList = tokenInventoryMapper.selectByExample(example);
            if (CollUtil.isNotEmpty(tokenInventoryList)) {
                int size = tokenInventoryList.size();
                List<TokenInventory> params = Collections.synchronizedList(new ArrayList<>(size));
                CountDownLatch countDownLatch = new CountDownLatch(size);
                tokenInventoryList.forEach(token -> {
                    pool.submit(() -> {
                        String tokenURI = ercServiceImpl.getTokenURI(token.getTokenAddress(), Convert.toBigInteger(token.getTokenId()));
                        if (StrUtil.isNotBlank(tokenURI)) {
                            Request request = new Request.Builder().url(tokenURI).build();
                            try (Response response = client.newCall(request).execute()) {
                                if (response.code() == 200) {
                                    String resp = response.body().string();
                                    TokenInventory tokenInventory = JSON.parseObject(resp, TokenInventory.class);
                                    tokenInventory.setUpdateTime(DateUtil.date());
                                    tokenInventory.setTokenId(token.getTokenId());
                                    tokenInventory.setTokenAddress(token.getTokenAddress());
                                    params.add(tokenInventory);
                                }
                                if (response.code() == 404) {
                                    log.error("token[{}] resource [{}] does not exist", token.getTokenAddress(), tokenURI);
                                }
                            } catch (Exception e) {
                                log.error(e, "请求TokenURI异常，token_address：{},token_id:{}", token.getTokenAddress(), token.getTokenId());
                            }
                        } else {
                            log.error("请求TokenURI为空，token_address：{},token_id:{}", token.getTokenAddress(), token.getTokenId());
                        }
                        countDownLatch.countDown();
                    });
                });
                countDownLatch.await();
                if (CollUtil.isNotEmpty(params)) {
                    syncTokenInfoMapper.updateTokenInventory(params);
                }
            }
        } catch (Exception e) {
            log.error(e, "定期更新token_inventory异常");
        }
    }

    /**
     * 定期更新token对应的持有人的数量
     *
     * @param
     * @return void
     * @author huangyongpeng@matrixelements.com
     * @date 2021/1/19
     */
    @Scheduled(cron = "0/15  * * * * ?")
    public void cronUpdateTokenHolder() {
        // 只有程序正常运行才执行任务
        if (!AppStatusUtil.isRunning()) {
            return;
        }
        try {
            List<TokenHolderNum> list = syncTokenInfoMapper.findTokenHolder();
            if (CollUtil.isNotEmpty(list)) {
                list.forEach(value -> value.setUpdateTime(DateUtil.date()));
                syncTokenInfoMapper.updateTokenHolder(list);
            }
        } catch (Exception e) {
            log.error(e, "定期更新token对应的持有人的数量异常");
        }
    }

}
