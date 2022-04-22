package com.platon.browser.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.platon.browser.bean.http.CustomHttpClient;
import com.platon.browser.cache.DestroyContractCache;
import com.platon.browser.dao.custommapper.CustomAddressMapper;
import com.platon.browser.dao.custommapper.CustomTokenHolderMapper;
import com.platon.browser.dao.custommapper.CustomTokenInventoryMapper;
import com.platon.browser.dao.custommapper.CustomTokenMapper;
import com.platon.browser.dao.entity.TokenInventory;
import com.platon.browser.dao.entity.TokenInventoryExample;
import com.platon.browser.dao.mapper.TokenHolderMapper;
import com.platon.browser.dao.mapper.TokenInventoryMapper;
import com.platon.browser.dao.mapper.TokenMapper;
import com.platon.browser.service.elasticsearch.EsErc20TxRepository;
import com.platon.browser.service.elasticsearch.EsErc721TxRepository;
import com.platon.browser.service.erc.ErcServiceImpl;
import com.platon.browser.utils.CommonUtil;
import com.platon.browser.v0152.analyzer.ErcCache;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * token定时器
 * 全量更新的时间需要错开
 *
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
    private CustomAddressMapper customAddressMapper;

    @Resource
    private CustomTokenInventoryMapper customTokenInventoryMapper;

    @Resource
    private TokenMapper tokenMapper;

    @Resource
    private ErcCache ercCache;

    @Resource
    private ErcServiceImpl ercServiceImpl;

    @Resource
    private EsErc20TxRepository esErc20TxRepository;

    @Resource
    private EsErc721TxRepository esErc721TxRepository;

    @Resource
    private DestroyContractCache destroyContractCache;

    private static final int TOKEN_BATCH_SIZE = 10;

    private static final ExecutorService TOKEN_UPDATE_POOL = Executors.newFixedThreadPool(TOKEN_BATCH_SIZE);

    private static final int HOLDER_BATCH_SIZE = 10;

    private static final ExecutorService HOLDER_UPDATE_POOL = Executors.newFixedThreadPool(HOLDER_BATCH_SIZE);

    private static final int INVENTORY_BATCH_SIZE = 10;

    /**
     * token更新标识位
     */
    @Getter
    @Setter
    private volatile long tokenCreateTime = 0L;

    /**
     * tokenInventory更新标识位
     */
    @Getter
    @Setter
    private AtomicLong tokenInventoryPage = new AtomicLong(0);

    /**
     * TokenHolderERC20更新标识
     */
    @Getter
    @Setter
    private volatile Long erc20TxSeq = 0L;

    /**
     * TokenHolderERC721更新标识
     */
    @Getter
    @Setter
    private volatile Long erc721TxSeq = 0L;

    private final Lock lock = new ReentrantLock();

    private final Lock tokenInventoryLock = new ReentrantLock();

    private final Lock tokenHolderLock = new ReentrantLock();


    /**
     * 更新token库存信息=>全量更新
     * 每天凌晨1点更新
     *
     * @param
     * @return void
     * @date 2021/4/17
     */
    @Scheduled(cron = "0 0 1 */1 * ?")
    public void totalUpdateTokenInventory() {
        tokenInventoryLock.lock();
        try {
            updateTokenInventory(0);
        } catch (Exception e) {
            log.error("更新token库存信息", e);
        } finally {
            tokenInventoryLock.unlock();
        }
    }

    /**
     * 更新token库存信息
     *
     * @param pageNum 当前页码
     * @return void
     * @date 2021/2/2
     */
    private void updateTokenInventory(int pageNum) {
        // 当前页码
        int page = pageNum;
        // 分页更新token库存相关信息
        List<TokenInventory> res = null;
        do {
            // 当次更新的条数
            int updateNum = 0;
            // 当前查询到的条数
            int batchNum = 0;
            // 当前失败的条数
            AtomicInteger errorNum = new AtomicInteger(0);
            try {
                TokenInventoryExample condition = new TokenInventoryExample();
                List<String> list = new ArrayList<>();
                list.add("lat12v6d2mguvnh4wm2d65k9sf5t2t8z9urer55u08");
                condition.createCriteria().andTokenAddressNotIn(list).andImageIsNull();
                condition.setOrderByClause(" id desc limit " + page * INVENTORY_BATCH_SIZE + "," + INVENTORY_BATCH_SIZE);
                List<TokenInventory> batch = tokenInventoryMapper.selectByExample(condition);
                // 过滤销毁的合约
                res = batch;
                List<TokenInventory> updateParams = new ArrayList<>();
                if (!res.isEmpty()) {
                    batchNum = res.size();
                    int finalPage = page;
                    res.forEach(inventory -> {
                        String tokenURI = "";
                        try {
                            tokenURI = ercServiceImpl.getTokenURI(inventory.getTokenAddress(), new BigInteger(inventory.getTokenId()));
                            if (StrUtil.isNotBlank(tokenURI)) {
                                Request request = new Request.Builder().url(tokenURI).build();
                                String resp = "";
                                Response response = CustomHttpClient.client.newCall(request).execute();
                                if (response.code() == 200) {
                                    resp = response.body().string();
                                    TokenInventory newTi = JSONUtil.toBean(resp, TokenInventory.class);
                                    newTi.setTokenId(inventory.getTokenId());
                                    newTi.setTokenAddress(inventory.getTokenAddress());
                                    boolean changed = false;
                                    // 只要有一个属性变动就添加到更新列表中
                                    if (ObjectUtil.isNull(inventory.getImage()) || !newTi.getImage().equals(inventory.getImage())) {
                                        inventory.setImage(newTi.getImage());
                                        changed = true;
                                    }
                                    if (ObjectUtil.isNull(inventory.getDescription()) || !newTi.getDescription().equals(inventory.getDescription())) {
                                        inventory.setDescription(newTi.getDescription());
                                        changed = true;
                                    }
                                    if (ObjectUtil.isNull(inventory.getName()) || !newTi.getName().equals(inventory.getName())) {
                                        inventory.setName(newTi.getName());
                                        changed = true;
                                    }
                                    if (changed) {
                                        log.info("token[{}]tokenId[{}]库存有属性变动需要更新,tokenURL[{}],tokenName[{}],tokenDesc[{}],tokenImage[{}]",
                                                 inventory.getTokenAddress(),
                                                 inventory.getTokenId(),
                                                 tokenURI,
                                                 inventory.getName(),
                                                 inventory.getDescription(),
                                                 inventory.getImage());
                                        updateParams.add(inventory);
                                    }
                                } else {
                                    errorNum.getAndIncrement();
                                    log.warn("http请求异常：http状态码:{},http消息:{},当前标识为:{},token_address:{}, token_id:{}, tokenURI:{}",
                                             response.code(),
                                             response.message(),
                                             pageNum,
                                             inventory.getTokenAddress(),
                                             inventory.getTokenId(),
                                             tokenURI);
                                }
                            } else {
                                errorNum.getAndIncrement();
                                log.warn("请求TokenURI为空,当前标识为:{},token_address：{},token_id:{}", finalPage, inventory.getTokenAddress(), inventory.getTokenId());
                            }
                        } catch (Exception e) {
                            errorNum.getAndIncrement();
                            log.warn(StrUtil.format("全量更新token库存信息异常,当前标识为:{},token_address：{},token_id:{},tokenURI:{}", finalPage, inventory.getTokenAddress(), inventory.getTokenId(), tokenURI), e);
                        }
                    });
                }
                if (CollUtil.isNotEmpty(updateParams)) {
                    updateNum = updateParams.size();
                    customTokenInventoryMapper.batchInsertOrUpdateSelective(updateParams, TokenInventory.Column.values());
                }
                log.info("全量更新token库存信息:当前标识为:{},查询到的条数为{},已更新的条数为:{},失败的条数为:{}", page, batchNum, updateNum, errorNum.get());
            } catch (Exception e) {
                log.error(StrUtil.format("全量更新token库存信息异常,当前标识为:{}", page), e);
            } finally {
                page++;
            }
        } while (CollUtil.isNotEmpty(res));
    }

    /**
     * 更新token库存信息=>增量更新
     * 每1分钟更新
     *
     * @param
     * @return void
     * @date 2021/2/1
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void incrementUpdateTokenInventory() {
        if (tokenInventoryLock.tryLock()) {
            try {
                cronIncrementUpdateTokenInventory(tokenInventoryPage.intValue());
            } catch (Exception e) {
                log.warn("增量更新token库存信息异常", e);
            } finally {
                tokenInventoryLock.unlock();
            }
        } else {
            log.warn("该次token库存增量更新抢不到锁，增量更新的标记为{}", tokenInventoryPage);
        }
    }

    /**
     * 更新token库存信息=>增量更新
     *
     * @param pageNum 当前页码
     * @return void
     * @date 2021/4/26
     */
    private void cronIncrementUpdateTokenInventory(int pageNum) {
        // 当次更新的条数
        int updateNum = 0;
        // 当前查询到的条数
        int batchNum = 0;
        // 当前失败的条数
        AtomicInteger errorNum = new AtomicInteger(0);
        try {
            TokenInventoryExample condition = new TokenInventoryExample();
            List<String> list = new ArrayList<>();
            list.add("lat12v6d2mguvnh4wm2d65k9sf5t2t8z9urer55u08");
            condition.createCriteria().andTokenAddressNotIn(list).andImageIsNull();
            condition.setOrderByClause(" id desc limit " + pageNum * INVENTORY_BATCH_SIZE + "," + INVENTORY_BATCH_SIZE);
            // 分页更新token库存相关信息
            List<TokenInventory> batch = tokenInventoryMapper.selectByExample(condition);
            List<TokenInventory> res = batch;
            batchNum = CommonUtil.ofNullable(() -> res.size()).orElse(0);
            List<TokenInventory> updateParams = new ArrayList<>();
            if (CollUtil.isNotEmpty(res)) {
                res.forEach(inventory -> {
                    String tokenURI = "";
                    try {
                        tokenURI = ercServiceImpl.getTokenURI(inventory.getTokenAddress(), new BigInteger(inventory.getTokenId()));
                        if (StrUtil.isNotBlank(tokenURI)) {
                            Request request = new Request.Builder().url(tokenURI).build();
                            String resp = "";
                            Response response = CustomHttpClient.client.newCall(request).execute();
                            if (response.code() == 200) {
                                resp = response.body().string();
                                TokenInventory newTi = JSONUtil.toBean(resp, TokenInventory.class);
                                newTi.setTokenId(inventory.getTokenId());
                                newTi.setTokenAddress(inventory.getTokenAddress());
                                boolean changed = false;
                                // 只要有一个属性变动就添加到更新列表中
                                if (ObjectUtil.isNull(inventory.getImage()) || !newTi.getImage().equals(inventory.getImage())) {
                                    inventory.setImage(newTi.getImage());
                                    changed = true;
                                }
                                if (ObjectUtil.isNull(inventory.getDescription()) || !newTi.getDescription().equals(inventory.getDescription())) {
                                    inventory.setDescription(newTi.getDescription());
                                    changed = true;
                                }
                                if (ObjectUtil.isNull(inventory.getName()) || !newTi.getName().equals(inventory.getName())) {
                                    inventory.setName(newTi.getName());
                                    changed = true;
                                }
                                if (changed) {
                                    log.info("token[{}]tokenId[{}]库存有属性变动需要更新,tokenURL[{}],tokenName[{}],tokenDesc[{}],tokenImage[{}]",
                                             inventory.getTokenAddress(),
                                             inventory.getTokenId(),
                                             tokenURI,
                                             inventory.getName(),
                                             inventory.getDescription(),
                                             inventory.getImage());
                                    updateParams.add(inventory);
                                }
                            } else {
                                errorNum.getAndIncrement();
                                log.warn("http请求异常：http状态码:{},http消息:{},当前标识为:{},token_address:{}, token_id:{}, tokenURI:{}",
                                         response.code(),
                                         response.message(),
                                         pageNum,
                                         inventory.getTokenAddress(),
                                         inventory.getTokenId(),
                                         tokenURI);
                            }
                        } else {
                            errorNum.getAndIncrement();
                            log.warn("请求TokenURI为空,当前标识为:{},token_address：{},token_id:{}", pageNum, inventory.getTokenAddress(), inventory.getTokenId());
                        }
                    } catch (Exception e) {
                        errorNum.getAndIncrement();
                        log.warn(StrUtil.format("增量更新token库存信息异常,当前标识为:{},token_address：{},token_id:{},tokenURI:{}", pageNum, inventory.getTokenAddress(), inventory.getTokenId(), tokenURI), e);
                    }
                });
            }
            if (CollUtil.isNotEmpty(updateParams)) {
                updateNum = updateParams.size();
                customTokenInventoryMapper.batchInsertOrUpdateSelective(updateParams, TokenInventory.Column.values());
            }
            log.info("增量更新token库存信息:当前标识为:{},查询到的条数为:{},已更新的条数为:{},失败的条数为:{}", pageNum, batchNum, updateNum, errorNum.get());
        } catch (Exception e) {
            log.error(StrUtil.format("增量更新token库存信息异常,当前标识为:{}", pageNum), e);
        } finally {
            if (errorNum.get() == INVENTORY_BATCH_SIZE) {
                tokenInventoryPage.incrementAndGet();
            } else {
                tokenInventoryPage.set(0L);
            }
        }
    }

}
