package com.platon.browser.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.platon.browser.dao.entity.Token;
import com.platon.browser.dao.entity.TokenHolder;
import com.platon.browser.dao.entity.TokenInventory;
import com.platon.browser.dao.entity.TokenInventoryExample;
import com.platon.browser.dao.mapper.CustomTokenMapper;
import com.platon.browser.dao.mapper.SyncTokenInfoMapper;
import com.platon.browser.dao.mapper.TokenInventoryMapper;
import com.platon.browser.service.erc.ErcServiceImpl;
import com.platon.browser.task.bean.TokenHolderType;
import com.platon.browser.utils.AppStatusUtil;
import com.platon.browser.v0152.analyzer.ErcCache;
import com.platon.browser.v0152.bean.ErcToken;
import com.platon.browser.v0152.enums.ErcTypeEnum;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
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
    private ErcCache ercCache;

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
        if (!AppStatusUtil.isRunning()) return;
        Set<ErcToken> updateParams = new ConcurrentHashSet<>();

        List<List<ErcToken>> batchList = new ArrayList<>();
        // 从缓存中获取Token更新参数，并构造批次列表
        List<ErcToken> batch = new ArrayList<>();
        batchList.add(batch);
        for (ErcToken token : ercCache.getTokenCache()) {
            if(token.isDirty()) updateParams.add(token);
            if(token.getTypeEnum() != ErcTypeEnum.ERC20) continue;

            if(batch.size()==TOKEN_BATCH_SIZE){
                // 本批次达到大小限制，则新建批次，并加入批次列表
                batch = new ArrayList<>();
                batchList.add(batch);
            }
            // 加入批次中
            batch.add(token);
        }

        // 分批并发查询Token totalSupply
        batchList.forEach(b->{
            CountDownLatch latch = new CountDownLatch(b.size());
            for (ErcToken token : b) {
                TOKEN_UPDATE_POOL.submit(() -> {
                    try {
                        // 查询总供应量
                        BigInteger totalSupply = ercServiceImpl.getTotalSupply(token.getAddress());
                        totalSupply = totalSupply == null ? BigInteger.ZERO : totalSupply;
                        if(token.getTotalSupply().compareTo(new BigDecimal(totalSupply))!=0){
                            // 有变动添加到更新列表中
                            token.setTotalSupply(new BigDecimal(totalSupply));
                            token.setUpdateTime(new Date());
                            updateParams.add(token);
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage());
                        log.error("",e);
                    } finally {
                        latch.countDown();
                    }
                });
            }
            try {
                latch.await();
            } catch (InterruptedException e) {
                log.error("",e);
            }
        });

        if(!updateParams.isEmpty()) {
            // 批量更新总供应量有变动的记录
            customTokenMapper.batchInsertOrUpdateSelective(new ArrayList<>(updateParams),Token.Column.values());
            updateParams.forEach(token->token.setDirty(false));
        }
        customTokenMapper.updateTokenHolderCount();
    }

    /**
     * 更新token持有者余额
     */
    @Scheduled(cron = "0/30  * * * * ?")
    public void updateTokenHolderBalance(){
        try {
            List<TokenHolderType> tokenTypeList = syncTokenInfoMapper.findTokenHolderType();
            if (CollUtil.isNotEmpty(tokenTypeList)) {
                int size = tokenTypeList.size();
                List<TokenHolder> tokenHolderList = Collections.synchronizedList(new ArrayList<>(size));
                CountDownLatch latch = new CountDownLatch(size);
                tokenTypeList.forEach(d -> {
                    HOLDER_UPDATE_POOL.submit(() -> {
                        try {
                            // 查询余额并回填
                            BigInteger balance = ercServiceImpl.getBalance(d.getTokenAddress(), d.getType(), d.getAddress());
                            d.setBalance(new BigDecimal(balance));
                            d.setUpdateTime(DateUtil.date());
                            tokenHolderList.add(d);
                        } catch (Exception e) {
                            log.error("{}",e.getMessage());
                        } finally {
                            latch.countDown();
                        }
                    });
                });
                latch.await();
                if (CollUtil.isNotEmpty(tokenHolderList)) {
                    this.syncTokenInfoMapper.updateAddressBalance(tokenHolderList);
                }
            }
        } catch (Exception e) {
            log.error("更新地址代币余额异常",e);
        }
    }

    /**
     * 更新token库存信息
     */
    @Scheduled(cron = "0/30  * * * * ?")
    public void updateTokenInventory(){
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
                CountDownLatch latch = new CountDownLatch(size);
                tokenInventoryList.forEach(token -> {
                    INVENTORY_UPDATE_POOL.submit(() -> {
                        try {
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
                                    log.error("请求TokenURI异常，token_address：{},token_id:{}", token.getTokenAddress(), token.getTokenId());
                                    log.error("", e);
                                }
                            } else {
                                log.error("请求TokenURI为空，token_address：{},token_id:{}", token.getTokenAddress(), token.getTokenId());
                            }
                        } catch (Exception e) {
                            log.error("{}",e.getMessage());
                        } finally {
                            latch.countDown();
                        }
                    });
                });
                latch.await();
                if (CollUtil.isNotEmpty(params)) {
                    syncTokenInfoMapper.updateTokenInventory(params);
                }
            }
        } catch (Exception e) {
            log.error("定期更新token_inventory异常",e);
        }
    }
}
