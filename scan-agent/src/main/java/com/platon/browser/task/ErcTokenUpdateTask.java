package com.platon.browser.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.Page;
import com.platon.browser.bean.CustomTokenHolder;
import com.platon.browser.bean.Erc721ContractDestroyBalanceVO;
import com.platon.browser.bean.TokenHolderCount;
import com.platon.browser.bean.http.CustomHttpClient;
import com.platon.browser.cache.DestroyContractCache;
import com.platon.browser.dao.custommapper.CustomAddressMapper;
import com.platon.browser.dao.custommapper.CustomTokenHolderMapper;
import com.platon.browser.dao.custommapper.CustomTokenInventoryMapper;
import com.platon.browser.dao.custommapper.CustomTokenMapper;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.TokenHolderMapper;
import com.platon.browser.dao.mapper.TokenInventoryMapper;
import com.platon.browser.dao.mapper.TokenMapper;
import com.platon.browser.elasticsearch.dto.ErcTx;
import com.platon.browser.enums.AddressTypeEnum;
import com.platon.browser.service.elasticsearch.AbstractEsRepository;
import com.platon.browser.service.elasticsearch.EsErc20TxRepository;
import com.platon.browser.service.elasticsearch.EsErc721TxRepository;
import com.platon.browser.service.elasticsearch.bean.ESResult;
import com.platon.browser.service.elasticsearch.query.ESQueryBuilderConstructor;
import com.platon.browser.service.elasticsearch.query.ESQueryBuilders;
import com.platon.browser.service.erc.ErcServiceImpl;
import com.platon.browser.utils.AddressUtil;
import com.platon.browser.utils.AppStatusUtil;
import com.platon.browser.utils.CommonUtil;
import com.platon.browser.v0152.analyzer.ErcCache;
import com.platon.browser.v0152.bean.ErcToken;
import com.platon.browser.v0152.enums.ErcTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import okhttp3.Response;
import org.elasticsearch.index.query.QueryBuilders;
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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

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

    private static final int INVENTORY_BATCH_SIZE = 100;

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
     * 更新ERC20和Erc721Enumeration token的总供应量===》全量更新
     * 每5分钟更新
     *
     * @return void
     * @date 2021/1/18
     */
    @Scheduled(cron = "0 */5 * * * ?")
    public void totalUpdateTokenTotalSupply() {
        lock.lock();
        try {
            this.updateTokenTotalSupply();
        } catch (Exception e) {
            log.warn("全量更新token的总供应量异常", e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 更新ERC20和Erc721Enumeration token的总供应量===》全量更新
     *
     * @return void
     * @date 2021/1/18
     */
    private void updateTokenTotalSupply() {
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
            if (!(token.getTypeEnum() == ErcTypeEnum.ERC20 || token.getIsSupportErc721Enumeration())) {
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
            // 过滤销毁的合约
            List<ErcToken> res = tokenSubtractToList(b, destroyContractCache.getDestroyContracts());
            if (CollUtil.isNotEmpty(res)) {
                CountDownLatch latch = new CountDownLatch(res.size());
                for (ErcToken token : res) {
                    TOKEN_UPDATE_POOL.submit(() -> {
                        try {
                            // 查询总供应量
                            BigInteger totalSupply = ercServiceImpl.getTotalSupply(token.getAddress());
                            totalSupply = totalSupply == null ? BigInteger.ZERO : totalSupply;
                            if (ObjectUtil.isNull(token.getTotalSupply()) || !token.getTotalSupply().equalsIgnoreCase(totalSupply.toString())) {
                                log.info("token[{}]的总供应量有变动需要更新旧值[{}]新值[{}]", token.getAddress(), token.getTotalSupply(), totalSupply.toString());
                                // 有变动添加到更新列表中
                                token.setTotalSupply(totalSupply.toString());
                                token.setUpdateTime(new Date());
                                updateParams.add(token);
                            }
                        } catch (Exception e) {
                            log.error("异常更新ERC token的总供应量", e);
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
            }
        });
        if (!updateParams.isEmpty()) {
            // 批量更新总供应量有变动的记录
            customTokenMapper.batchInsertOrUpdateSelective(new ArrayList<>(updateParams), Token.Column.values());
            updateParams.forEach(token -> token.setDirty(false));
        }
        updateTokenHolderCount();
    }

    /**
     * 过滤销毁的合约
     *
     * @param ercTokens:
     * @param destroyContracts:
     * @return: java.util.List<com.platon.browser.v0152.bean.ErcToken>
     * @date: 2021/10/14
     */
    private List<ErcToken> tokenSubtractToList(List<ErcToken> ercTokens, Set<String> destroyContracts) {
        List<ErcToken> res = CollUtil.newArrayList();
        for (ErcToken ercToken : ercTokens) {
            if (!destroyContracts.contains(ercToken.getAddress())) {
                res.add(ercToken);
            }
        }
        return res;
    }

    /**
     * 更新token对应的持有人的数量
     *
     * @param
     * @return void
     * @date 2021/3/17
     */
    private void updateTokenHolderCount() {
        List<Token> updateTokenList = new ArrayList<>();
        List<TokenHolderCount> list = customTokenHolderMapper.findTokenHolderCount();
        List<Token> tokenList = tokenMapper.selectByExample(null);
        if (CollUtil.isNotEmpty(list) && CollUtil.isNotEmpty(tokenList)) {
            list.forEach(tokenHolderCount -> {
                tokenList.forEach(token -> {
                    if (token.getAddress().equalsIgnoreCase(tokenHolderCount.getTokenAddress()) && !token.getHolder().equals(tokenHolderCount.getTokenHolderCount())) {
                        token.setHolder(tokenHolderCount.getTokenHolderCount());
                        updateTokenList.add(token);
                    }
                });
            });
        }
        if (CollUtil.isNotEmpty(updateTokenList)) {
            customTokenMapper.batchInsertOrUpdateSelective(updateTokenList, Token.Column.values());
            log.info("更新token对应的持有人的数量{}", JSONUtil.toJsonStr(updateTokenList));
        }
    }

    /**
     * 更新token持有者余额===》增量更新
     * 每1分钟运行一次
     *
     * @param
     * @return void
     * @date 2021/2/1
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void incrementUpdateTokenHolderBalance() {
        if (tokenHolderLock.tryLock()) {
            try {
                incrementUpdateTokenHolderBalance(esErc20TxRepository, ErcTypeEnum.ERC20, this.getErc20TxSeq());
                incrementUpdateTokenHolderBalance(esErc721TxRepository, ErcTypeEnum.ERC721, this.getErc721TxSeq());
            } catch (Exception e) {
                log.error("增量更新token持有者余额异常", e);
            } finally {
                tokenHolderLock.unlock();
            }
        } else {
            log.error("本次增量更新token持有者余额抢不到锁,erc20TxSeq:{},erc721TxSeq:{}", erc20TxSeq, erc721TxSeq);
        }
    }

    private void incrementUpdateTokenHolderBalance(AbstractEsRepository abstractEsRepository, ErcTypeEnum typeEnum, Long txSeq) {
        // 只有程序正常运行才执行任务
        if (!AppStatusUtil.isRunning()) {
            return;
        }
        try {
            List<TokenHolder> updateParams = new ArrayList<>();
            ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
            ESResult<ErcTx> queryResultFromES = new ESResult<>();
            constructor.setAsc("seq");
            constructor.setResult(new String[]{"seq", "from", "contract", "to"});
            ESQueryBuilders esQueryBuilders = new ESQueryBuilders();
            esQueryBuilders.listBuilders().add(QueryBuilders.rangeQuery("seq").gt(txSeq));
            constructor.must(esQueryBuilders);
            constructor.setUnmappedType("long");
            queryResultFromES = abstractEsRepository.search(constructor, ErcTx.class, 1, 5000);
            List<ErcTx> list = queryResultFromES.getRsData();
            if (CollUtil.isEmpty(list)) {
                return;
            }
            HashMap<String, HashSet<String>> map = new HashMap();
            list.sort(Comparator.comparing(ErcTx::getSeq));
            if (typeEnum == ErcTypeEnum.ERC20) {
                this.setErc20TxSeq(list.get(list.size() - 1).getSeq());
            } else if (typeEnum == ErcTypeEnum.ERC721) {
                this.setErc721TxSeq(list.get(list.size() - 1).getSeq());
            }
            list.forEach(v -> {
                if (map.containsKey(v.getContract())) {
                    // 判断是否是0地址
                    if (!AddressUtil.isAddrZero(v.getTo())) {
                        map.get(v.getContract()).add(v.getTo());
                    }
                    if (!AddressUtil.isAddrZero(v.getFrom())) {
                        map.get(v.getContract()).add(v.getFrom());
                    }
                } else {
                    HashSet<String> addressSet = new HashSet<String>();
                    // 判断是否是0地址
                    if (!AddressUtil.isAddrZero(v.getTo())) {
                        addressSet.add(v.getTo());
                    }
                    if (!AddressUtil.isAddrZero(v.getFrom())) {
                        addressSet.add(v.getFrom());
                    }
                    map.put(v.getContract(), addressSet);
                }
            });

            // 过滤销毁的合约
            HashMap<String, HashSet<String>> res = subtractToMap(map, destroyContractCache.getDestroyContracts());
            if (MapUtil.isNotEmpty(res)) {
                // 串行查余额
                res.forEach((contract, addressSet) -> {
                    addressSet.forEach(address -> {
                        try {
                            BigInteger balance = ercServiceImpl.getBalance(contract, typeEnum, address);
                            TokenHolder holder = new TokenHolder();
                            holder.setTokenAddress(contract);
                            holder.setAddress(address);
                            holder.setBalance(balance.toString());
                            holder.setUpdateTime(DateUtil.date());
                            updateParams.add(holder);
                            log.info("token[{}]address[{}]查询到余额[{}]", contract, address, balance.toString());
                            try {
                                TimeUnit.MILLISECONDS.sleep(100);
                            } catch (InterruptedException interruptedException) {
                                interruptedException.printStackTrace();
                            }
                        } catch (Exception e) {
                            log.warn(StrFormatter.format("查询地址余额失败,contract:{},address:{}", contract, address), e);
                        }
                    });
                });
            }
            if (!updateParams.isEmpty()) {
                customTokenHolderMapper.batchUpdate(updateParams);
            }
        } catch (Exception e) {
            log.error("更新token持有者余额异常", e);
        }
    }

    private HashMap<String, HashSet<String>> subtractToMap(HashMap<String, HashSet<String>> map, Set<String> destroyContracts) {
        HashMap<String, HashSet<String>> res = CollUtil.newHashMap();
        if (CollUtil.isNotEmpty(map)) {
            for (Map.Entry<String, HashSet<String>> entry : map.entrySet()) {
                if (!destroyContracts.contains(entry.getKey())) {
                    res.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return res;
    }

    /**
     * 更新token持有者余额===》全量更新
     * 每天00:00:00运行一次
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void totalUpdateTokenHolderBalance() {
        // 只有程序正常运行才执行任务
        if (!AppStatusUtil.isRunning()) {
            return;
        }
        tokenHolderLock.lock();
        try {
            // 分页更新holder的balance
            List<TokenHolder> batch;
            int page = 0;
            do {
                TokenHolderExample condition = new TokenHolderExample();
                condition.setOrderByClause(" token_address asc, address asc limit " + page * HOLDER_BATCH_SIZE + "," + HOLDER_BATCH_SIZE);
                batch = tokenHolderMapper.selectByExample(condition);
                // 过滤销毁的合约
                List<TokenHolder> res = subtractToList(batch, destroyContractCache.getDestroyContracts());
                List<TokenHolder> updateParams = new ArrayList<>();
                if (CollUtil.isNotEmpty(res)) {
                    CountDownLatch latch = new CountDownLatch(res.size());
                    res.forEach(holder -> {
                        HOLDER_UPDATE_POOL.submit(() -> {
                            try {
                                // 查询余额并回填
                                ErcToken token = ercCache.getTokenCache().get(holder.getTokenAddress());
                                if (token != null) {
                                    BigInteger balance = ercServiceImpl.getBalance(holder.getTokenAddress(), token.getTypeEnum(), holder.getAddress());
                                    if (ObjectUtil.isNull(holder.getBalance()) || new BigDecimal(holder.getBalance()).compareTo(new BigDecimal(balance)) != 0) {
                                        log.info("token[{}]address[{}]余额有变动需要更新,旧值[{}]新值[{}]", holder.getTokenAddress(), holder.getAddress(), holder.getBalance(), balance.toString());
                                        // 余额有变动才加入更新列表，避免频繁访问表
                                        holder.setBalance(balance.toString());
                                        holder.setUpdateTime(DateUtil.date());
                                        updateParams.add(holder);
                                    }
                                }
                            } catch (Exception e) {
                                log.error(StrFormatter.format("查询余额失败,地址[{}],合约地址[{}]", holder.getAddress(), holder.getTokenAddress()), e);
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
                    customTokenHolderMapper.batchUpdate(updateParams);
                }
                page++;
            } while (!batch.isEmpty());
        } catch (Exception e) {
            log.error("更新地址代币余额异常", e);
        } finally {
            tokenHolderLock.unlock();
        }
    }

    /**
     * 过滤销毁的合约
     *
     * @param list:
     * @param destroyContracts:
     * @return: java.util.List<com.platon.browser.dao.entity.TokenHolder>
     * @date: 2021/10/14
     */
    private List<TokenHolder> subtractToList(List<TokenHolder> list, Set<String> destroyContracts) {
        List<TokenHolder> res = CollUtil.newArrayList();
        if (CollUtil.isNotEmpty(list)) {
            for (TokenHolder tokenHolder : list) {
                if (!destroyContracts.contains(tokenHolder.getTokenAddress())) {
                    res.add(tokenHolder);
                }
            }
        }
        return res;
    }

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
        // 只有程序正常运行才执行任务
        if (!AppStatusUtil.isRunning()) {
            return;
        }
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
                condition.setOrderByClause(" id asc limit " + page * INVENTORY_BATCH_SIZE + "," + INVENTORY_BATCH_SIZE);
                List<TokenInventory> batch = tokenInventoryMapper.selectByExample(condition);
                // 过滤销毁的合约
                res = tokenInventorySubtractToList(batch, destroyContractCache.getDestroyContracts());
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
                                        log.info("token[{}]库存有属性变动需要更新,tokenURL[{}],tokenName[{}],tokenDesc[{}],tokenImage[{}]",
                                                 inventory.getTokenAddress(),
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
     * 过滤销毁的合约
     *
     * @param list:
     * @param destroyContracts:
     * @return: java.util.List<com.platon.browser.dao.entity.TokenInventory>
     * @date: 2021/10/15
     */
    private List<TokenInventory> tokenInventorySubtractToList(List<TokenInventory> list, Set<String> destroyContracts) {
        List<TokenInventory> res = CollUtil.newArrayList();
        if (CollUtil.isNotEmpty(list)) {
            for (TokenInventory tokenInventory : list) {
                if (!destroyContracts.contains(tokenInventory.getTokenAddress())) {
                    res.add(tokenInventory);
                }
            }
        }
        return res;
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
            log.error("该次token库存增量更新抢不到锁，增量更新的标记为{}", tokenInventoryPage);
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
        // 只有程序正常运行才执行任务
        if (!AppStatusUtil.isRunning()) {
            return;
        }
        // 当次更新的条数
        int updateNum = 0;
        // 当前查询到的条数
        int batchNum = 0;
        // 当前失败的条数
        AtomicInteger errorNum = new AtomicInteger(0);
        try {
            TokenInventoryExample condition = new TokenInventoryExample();
            condition.setOrderByClause(" id asc limit " + pageNum * INVENTORY_BATCH_SIZE + "," + INVENTORY_BATCH_SIZE);
            // 分页更新token库存相关信息
            List<TokenInventory> batch = tokenInventoryMapper.selectByExample(condition);
            List<TokenInventory> res = tokenInventorySubtractToList(batch, destroyContractCache.getDestroyContracts());
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
                                    log.info("token[{}]库存有属性变动需要更新,tokenURL[{}],tokenName[{}],tokenDesc[{}],tokenImage[{}]",
                                             inventory.getTokenAddress(),
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
            if (batchNum == INVENTORY_BATCH_SIZE) {
                tokenInventoryPage.incrementAndGet();
            }
        }
    }

    /**
     * 销毁的721合约更新余额
     *
     * @param :
     * @return: void
     * @date: 2021/9/27
     */
    @Scheduled(cron = "0 */10 * * * ?")
    public void contractDestroyUpdateBalance() {
        if (!AppStatusUtil.isRunning()) {
            return;
        }
        contractErc721DestroyUpdateBalance();
    }

    /**
     * 销毁的erc721更新余额
     *
     * @param :
     * @return: void
     * @date: 2021/9/27
     */
    private void contractErc721DestroyUpdateBalance() {
        try {
            List<String> contractErc721Destroys = customAddressMapper.findContractDestroy(AddressTypeEnum.ERC721_EVM_CONTRACT.getCode());
            if (CollUtil.isNotEmpty(contractErc721Destroys)) {
                for (String tokenAddress : contractErc721Destroys) {
                    List<Erc721ContractDestroyBalanceVO> list = customTokenInventoryMapper.findErc721ContractDestroyBalance(tokenAddress);
                    Page<CustomTokenHolder> ids = customTokenHolderMapper.selectERC721Holder(tokenAddress);
                    List<TokenHolder> updateParams = new ArrayList<>();
                    StringBuilder res = new StringBuilder();
                    for (CustomTokenHolder tokenHolder : ids) {
                        List<Erc721ContractDestroyBalanceVO> filterList = list.stream().filter(v -> v.getOwner().equalsIgnoreCase(tokenHolder.getAddress())).collect(Collectors.toList());
                        int balance = 0;
                        if (CollUtil.isNotEmpty(filterList)) {
                            balance = filterList.get(0).getNum();
                        }
                        if (!tokenHolder.getBalance().equalsIgnoreCase(cn.hutool.core.convert.Convert.toStr(balance))) {
                            TokenHolder updateTokenHolder = new TokenHolder();
                            updateTokenHolder.setTokenAddress(tokenHolder.getTokenAddress());
                            updateTokenHolder.setAddress(tokenHolder.getAddress());
                            updateTokenHolder.setBalance(cn.hutool.core.convert.Convert.toStr(balance));
                            updateParams.add(updateTokenHolder);
                            res.append(StrUtil.format("[合约{}，余额{}->{}] ", tokenHolder.getAddress(), tokenHolder.getBalance(), cn.hutool.core.convert.Convert.toStr(balance)));
                        }
                    }
                    if (CollUtil.isNotEmpty(updateParams)) {
                        customTokenHolderMapper.batchUpdate(updateParams);
                        log.info("销毁的erc721[{}]更新余额成功，结果为{}", tokenAddress, res.toString());
                    }
                }
            }
        } catch (Exception e) {
            log.error("销毁的erc721更新余额异常", e);
        }
    }

}
