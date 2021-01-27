package com.platon.browser.task;

import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.*;
import com.platon.browser.service.erc.ErcServiceImpl;
import com.platon.browser.utils.AppStatusUtil;
import com.platon.browser.v0152.analyzer.ErcCache;
import com.platon.browser.v0152.bean.ErcToken;
import com.platon.browser.v0152.enums.ErcTypeEnum;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * token定时器
 *
 * @author huangyongpeng@matrixelements.com
 * @date 2021/1/22
 */
@Slf4j
@Component
public class ErcTokenUpdateTask {

    @Resource
    private CustomTokenMapper customTokenMapper;

    @Resource
    private TokenHolderMapper tokenHolderMapper;

    @Resource
    private CustomTokenHolderMapper customTokenHolderMapper;

    @Resource
    private TokenInventoryMapper tokenInventoryMapper;

    @Resource
    private CustomTokenInventoryMapper customTokenInventoryMapper;

    @Resource
    private ErcCache ercCache;

    @Resource
    private ErcServiceImpl ercServiceImpl;

    /**
     * 线程名前缀
     */
    private final static String ThreadFactoryName = "token-task-pool-";

    private static final int TOKEN_BATCH_SIZE = 10;

    private static final ExecutorService TOKEN_UPDATE_POOL = Executors.newFixedThreadPool(TOKEN_BATCH_SIZE);

    private static final int HOLDER_BATCH_SIZE = 10;

    private static final ExecutorService HOLDER_UPDATE_POOL = Executors.newFixedThreadPool(HOLDER_BATCH_SIZE);

    private static final int INVENTORY_BATCH_SIZE = 10;

    private static final ExecutorService INVENTORY_UPDATE_POOL = Executors.newFixedThreadPool(INVENTORY_BATCH_SIZE);

    private final static OkHttpClient client = new OkHttpClient.Builder()
            .connectionPool(new ConnectionPool(50, 5, TimeUnit.MINUTES))
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build();

    /**
     * 更新ERC 20 token的总供应量
     *
     * @return void
     * @author huangyongpeng@matrixelements.com
     * @date 2021/1/18
     */
    @Scheduled(cron = "0/30  * * * * ?")
    public void updateTokenTotalSupply() {
        // 只有程序正常运行才执行任务
        if (!AppStatusUtil.isRunning()) {
            return;
        }
        Set<ErcToken> updateParams = new ConcurrentHashSet<>();

        List<List<ErcToken>> batchList = new ArrayList<>();
        // 从缓存中获取Token更新参数，并构造批次列表
        List<ErcToken> batch = new ArrayList<>();
        batchList.add(batch);
        for (ErcToken token : ercCache.getTokenCache().values()) {
            if (token.isDirty()) {
                updateParams.add(token);
            }
            if (token.getTypeEnum() != ErcTypeEnum.ERC20) {
                continue;
            }

            if (batch.size() == TOKEN_BATCH_SIZE) {
                // 本批次达到大小限制，则新建批次，并加入批次列表
                batch = new ArrayList<>();
                batchList.add(batch);
            }
            // 加入批次中
            batch.add(token);
        }

        // 分批并发查询Token totalSupply
        batchList.forEach(b -> {
            CountDownLatch latch = new CountDownLatch(b.size());
            for (ErcToken token : b) {
                TOKEN_UPDATE_POOL.submit(() -> {
                    try {
                        // 查询总供应量
                        BigInteger totalSupply = ercServiceImpl.getTotalSupply(token.getAddress());
                        totalSupply = totalSupply == null ? BigInteger.ZERO : totalSupply;
                        if (token.getTotalSupply().compareTo(new BigDecimal(totalSupply)) != 0) {
                            // 有变动添加到更新列表中
                            token.setTotalSupply(new BigDecimal(totalSupply));
                            token.setUpdateTime(new Date());
                            updateParams.add(token);
                        }
                    } catch (Exception e) {
                        log.error("异常更新ERC 20 token的总供应量", e);
                    } finally {
                        latch.countDown();
                    }
                });
            }
            try {
                latch.await();
            } catch (InterruptedException e) {
                log.error("", e);
            }
        });

        if (!updateParams.isEmpty()) {
            // 批量更新总供应量有变动的记录
            customTokenMapper.batchInsertOrUpdateSelective(new ArrayList<>(updateParams), Token.Column.values());
            updateParams.forEach(token -> token.setDirty(false));
        }
        customTokenMapper.updateTokenHolderCount();
    }

    /**
     * 更新token持有者余额
     */
    @Scheduled(cron = "0/30  * * * * ?")
    public void updateTokenHolderBalance() {
        // 只有程序正常运行才执行任务
        if (!AppStatusUtil.isRunning()) {
            return;
        }
        try {
            // 分页更新holder的balance
            List<TokenHolder> batch;
            int page = 0;
            do {
                TokenHolderExample condition = new TokenHolderExample();
                condition.setOrderByClause(" token_address asc, address asc limit " + page * HOLDER_BATCH_SIZE + "," + HOLDER_BATCH_SIZE);
                batch = tokenHolderMapper.selectByExample(condition);

                List<TokenHolder> updateParams = new ArrayList<>();
                if (!batch.isEmpty()) {
                    CountDownLatch latch = new CountDownLatch(batch.size());
                    batch.forEach(holder -> {
                        HOLDER_UPDATE_POOL.submit(() -> {
                            try {
                                // 查询余额并回填
                                ErcToken token = ercCache.getTokenCache().get(holder.getTokenAddress());
                                if (token != null) {
                                    BigInteger balance = ercServiceImpl.getBalance(holder.getTokenAddress(), token.getTypeEnum(), holder.getAddress());
                                    if (holder.getBalance().compareTo(new BigDecimal(balance)) != 0) {
                                        // 余额有变动才加入更新列表，避免频繁访问表
                                        holder.setBalance(new BigDecimal(balance));
                                        holder.setUpdateTime(DateUtil.date());
                                        updateParams.add(holder);
                                    }
                                }
                            } catch (Exception e) {
                                log.error(StrFormatter.format("查询地址【{}】的令牌【{}】余额失败", holder.getAddress(), holder.getTokenAddress()), e);
                            } finally {
                                latch.countDown();
                            }
                        });
                    });
                    try {
                        latch.await();
                    } catch (InterruptedException e) {
                        log.error("", e);
                    }
                }
                if (!updateParams.isEmpty()) {
                    customTokenHolderMapper.batchInsertOrUpdateSelective(updateParams, TokenHolder.Column.values());
                }
                page++;
            } while (!batch.isEmpty());
        } catch (Exception e) {
            log.error("更新地址代币余额异常", e);
        }
    }

    /**
     * 更新token库存信息
     */
    @Scheduled(cron = "0/30  * * * * ?")
    public void updateTokenInventory() {
        // 只有程序正常运行才执行任务
        if (!AppStatusUtil.isRunning()) {
            return;
        }
        try {
            // 分页更新token库存相关信息
            List<TokenInventory> batch;
            int page = 0;
            do {
                TokenInventoryExample condition = new TokenInventoryExample();
                condition.setOrderByClause(" token_address asc limit " + page * INVENTORY_BATCH_SIZE + "," + INVENTORY_BATCH_SIZE);
                batch = tokenInventoryMapper.selectByExample(condition);

                List<TokenInventory> updateParams = new ArrayList<>();
                if (!batch.isEmpty()) {
                    CountDownLatch latch = new CountDownLatch(batch.size());
                    batch.forEach(inventory -> {
                        INVENTORY_UPDATE_POOL.submit(() -> {
                            try {
                                String tokenURI = ercServiceImpl.getTokenURI(inventory.getTokenAddress(), inventory.getTokenId());
                                if (StrUtil.isNotBlank(tokenURI)) {
                                    Request request = new Request.Builder().url(tokenURI).build();
                                    try (Response response = client.newCall(request).execute()) {
                                        if (response.code() == 200) {
                                            String resp = response.body().string();
                                            TokenInventory newTi = JSON.parseObject(resp, TokenInventory.class);
                                            newTi.setUpdateTime(DateUtil.date());
                                            newTi.setTokenId(inventory.getTokenId());
                                            newTi.setTokenAddress(inventory.getTokenAddress());
                                            boolean changed = false;
                                            // 只要有一个属性变动就添加到更新列表中
                                            if (StringUtils.isNotBlank(newTi.getImage()) && !newTi.getImage().equals(inventory.getImage())) {
                                                inventory.setImage(newTi.getImage());
                                                changed = true;
                                            }
                                            if (StringUtils.isNotBlank(newTi.getDescription()) && !newTi.getDescription().equals(inventory.getDescription())) {
                                                inventory.setDescription(newTi.getDescription());
                                                changed = true;
                                            }
                                            if (StringUtils.isNotBlank(newTi.getName()) && !newTi.getName().equals(inventory.getName())) {
                                                inventory.setName(newTi.getName());
                                                changed = true;
                                            }
                                            if (changed) {
                                                inventory.setUpdateTime(new Date());
                                                updateParams.add(inventory);
                                            }
                                        }
                                        if (response.code() == 404) {
                                            log.error("token[{}] resource [{}] does not exist", inventory.getTokenAddress(), tokenURI);
                                        }
                                    } catch (Exception e) {
                                        log.error(StrFormatter.format("请求TokenURI异常，token_address：{},token_id:{}", inventory.getTokenAddress(), inventory.getTokenId()), e);
                                    }
                                } else {
                                    log.error("请求TokenURI为空，token_address：{},token_id:{}", inventory.getTokenAddress(), inventory.getTokenId());
                                }
                            } catch (Exception e) {
                                log.error("更新token库存信息异常", e);
                            } finally {
                                latch.countDown();
                            }

                        });
                    });
                    try {
                        latch.await();
                    } catch (InterruptedException e) {
                        log.error("", e);
                    }
                }
                if (!updateParams.isEmpty()) {
                    customTokenInventoryMapper.batchInsertOrUpdateSelective(updateParams, TokenInventory.Column.values());
                }
                page++;
            } while (!batch.isEmpty());
        } catch (Exception e) {
            log.error("更新token库存信息", e);
        }
    }

}
