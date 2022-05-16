package com.platon.browser.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.Page;
import com.platon.browser.bean.*;
import com.platon.browser.bean.http.CustomHttpClient;
import com.platon.browser.dao.custommapper.CustomAddressMapper;
import com.platon.browser.dao.custommapper.CustomTokenHolderMapper;
import com.platon.browser.dao.custommapper.CustomTokenInventoryMapper;
import com.platon.browser.dao.custommapper.CustomTokenMapper;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.*;
import com.platon.browser.elasticsearch.dto.ErcTx;
import com.platon.browser.enums.AddressTypeEnum;
import com.platon.browser.enums.ErcTypeEnum;
import com.platon.browser.service.erc.ErcServiceImpl;
import com.platon.browser.utils.AddressUtil;
import com.platon.browser.utils.AppStatusUtil;
import com.platon.browser.utils.TaskUtil;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * token定时任务
 *
 * @date: 2021/11/30
 */
@Slf4j
@Component
public class ErcTokenUpdateTask {

    /**
     * token_inventory表重试次数
     */
    @Value("${platon.token-retry-num:3}")
    private int tokenRetryNum;

    @Resource
    private TokenInventoryMapper tokenInventoryMapper;

    @Resource
    private CustomTokenInventoryMapper customTokenInventoryMapper;

    @Resource
    private TokenHolderMapper tokenHolderMapper;

    @Resource
    private CustomTokenHolderMapper customTokenHolderMapper;

    @Resource
    private CustomTokenMapper customTokenMapper;

    @Resource
    private TokenMapper tokenMapper;

    @Resource
    private CustomAddressMapper customAddressMapper;

    @Resource
    private ErcServiceImpl ercServiceImpl;

    @Resource
    private PointLogMapper pointLogMapper;

    @Resource
    private TxErc20BakMapper txErc20BakMapper;

    @Resource
    private TxErc721BakMapper txErc721BakMapper;

    private static final int TOKEN_BATCH_SIZE = 10;

    private static final ExecutorService TOKEN_UPDATE_POOL = Executors.newFixedThreadPool(TOKEN_BATCH_SIZE);

    private static final int HOLDER_BATCH_SIZE = 10;

    private static final ExecutorService HOLDER_UPDATE_POOL = Executors.newFixedThreadPool(HOLDER_BATCH_SIZE);

    private final Lock lock = new ReentrantLock();

    private final Lock tokenInventoryLock = new ReentrantLock();

    private final Lock tokenHolderLock = new ReentrantLock();

    /**
     * 全量更新token的总供应量
     * 每5分钟更新
     *
     * @return void
     * @date 2021/1/18
     */
    @XxlJob("totalUpdateTokenTotalSupplyJobHandler")
    public void totalUpdateTokenTotalSupply() {
        lock.lock();
        try {
            updateTokenTotalSupply();
        } catch (Exception e) {
            log.warn("全量更新token的总供应量异常", e);
            throw e;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 增量更新token持有者余额
     * 每1分钟运行一次
     *
     * @param
     * @return void
     * @date 2021/2/1
     */
    @XxlJob("incrementUpdateTokenHolderBalanceJobHandler")
    public void incrementUpdateTokenHolderBalance() {
        if (tokenHolderLock.tryLock()) {
            try {
                incrementUpdateErc20TokenHolderBalance();
                incrementUpdateErc721TokenHolderBalance();
            } catch (Exception e) {
                log.error("增量更新token持有者余额异常", e);
            } finally {
                tokenHolderLock.unlock();
            }
        }
    }

    /**
     * 全量更新token持有者余额
     * 每天00:00:00执行一次
     */
    @XxlJob("totalUpdateTokenHolderBalanceJobHandler")
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
                List<ErcToken> ercTokens = getErcTokens();
                // 过滤销毁的合约
                List<TokenHolder> res = subtractToList(batch, getDestroyContracts());
                List<TokenHolder> updateParams = new ArrayList<>();
                if (CollUtil.isNotEmpty(res)) {
                    CountDownLatch latch = new CountDownLatch(res.size());
                    res.forEach(holder -> {
                        HOLDER_UPDATE_POOL.submit(() -> {
                            try {
                                // 查询余额并回填
                                ErcToken token = CollUtil.findOne(ercTokens, ercToken -> ercToken.getAddress().equalsIgnoreCase(holder.getTokenAddress()));
                                if (token != null) {
                                    BigInteger balance = ercServiceImpl.getBalance(holder.getTokenAddress(), token.getTypeEnum(), holder.getAddress());
                                    if (ObjectUtil.isNull(holder.getBalance()) || new BigDecimal(holder.getBalance()).compareTo(new BigDecimal(balance)) != 0) {
                                        log.info("token[{}]address[{}]余额有变动需要更新,旧值[{}]新值[{}]", holder.getTokenAddress(), holder.getAddress(), holder.getBalance(), balance.toString());
                                        // 余额有变动才加入更新列表，避免频繁访问表
                                        holder.setBalance(balance.toString());
                                        updateParams.add(holder);
                                    }
                                } else {
                                    String msg = StrUtil.format("找不到对应的token[{}]", holder.getTokenAddress());
                                    XxlJobHelper.log(msg);
                                    log.error(msg);
                                }
                            } catch (Exception e) {
                                XxlJobHelper.log(StrUtil.format("查询token holder的余额失败，合约[{}]地址[{}]", holder.getTokenAddress(), holder.getAddress()));
                                log.warn(StrFormatter.format("查询余额失败,地址[{}],合约地址[{}]", holder.getAddress(), holder.getTokenAddress()), e);
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
                if (CollUtil.isNotEmpty(updateParams)) {
                    customTokenHolderMapper.batchUpdate(updateParams);
                    TaskUtil.console("更新token持有者余额{}", JSONUtil.toJsonStr(updateParams));
                }
                page++;
            } while (!batch.isEmpty());
            XxlJobHelper.log("全量更新token持有者余额成功");
        } catch (Exception e) {
            log.error("更新地址代币余额异常", e);
        } finally {
            tokenHolderLock.unlock();
        }
    }

    /**
     * 全量更新token库存信息
     * 每天凌晨1点执行一次
     *
     * @param
     * @return void
     * @date 2021/4/17
     */
    @XxlJob("totalUpdateTokenInventoryJobHandler")
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
     * 增量更新token库存信息
     * 每1分钟执行一次
     *
     * @param
     * @return void
     * @date 2021/2/1
     */
    @XxlJob("incrementUpdateTokenInventoryJobHandler")
    public void incrementUpdateTokenInventory() {
        if (tokenInventoryLock.tryLock()) {
            try {
                cronIncrementUpdateTokenInventory();
            } catch (Exception e) {
                log.warn("增量更新token库存信息异常", e);
            } finally {
                tokenInventoryLock.unlock();
            }
        } else {
            log.warn("该次token库存增量更新抢不到锁");
        }
    }

    /**
     * 销毁的721合约更新余额
     * 每10分钟执行一次
     *
     * @param :
     * @return: void
     * @date: 2021/9/27
     */
    @XxlJob("contractDestroyUpdateBalanceJobHandler")
    public void contractDestroyUpdateBalance() {
        if (!AppStatusUtil.isRunning()) {
            return;
        }
        contractErc20DestroyUpdateBalance();
        contractErc721DestroyUpdateBalance();
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
        List<ErcToken> batch = new ArrayList<>();
        batchList.add(batch);
        List<ErcToken> tokens = getErcTokens();
        for (ErcToken token : tokens) {
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
            List<ErcToken> res = tokenSubtractToList(b, getDestroyContracts());
            if (CollUtil.isNotEmpty(res)) {
                CountDownLatch latch = new CountDownLatch(res.size());
                for (ErcToken token : res) {
                    TOKEN_UPDATE_POOL.submit(() -> {
                        try {
                            // 查询总供应量
                            BigInteger totalSupply = ercServiceImpl.getTotalSupply(token.getAddress());
                            totalSupply = totalSupply == null ? BigInteger.ZERO : totalSupply;
                            if (ObjectUtil.isNull(token.getTotalSupply()) || !token.getTotalSupply().equalsIgnoreCase(totalSupply.toString())) {
                                TaskUtil.console("token[{}]的总供应量有变动需要更新旧值[{}]新值[{}]", token.getAddress(), token.getTotalSupply(), totalSupply);
                                // 有变动添加到更新列表中
                                token.setTotalSupply(totalSupply.toString());
                                updateParams.add(token);
                            }
                        } catch (Exception e) {
                            XxlJobHelper.log(StrUtil.format("该token[{}]查询总供应量异常", token.getAddress()));
                            log.error("查询总供应量异常", e);
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
            customTokenMapper.batchUpdateTokenTotalSupply(new ArrayList<>(updateParams));
            XxlJobHelper.handleSuccess("全量更新token的总供应量成功");
            updateParams.forEach(token -> token.setDirty(false));
        }
        XxlJobHelper.log("全量更新token的总供应量成功");
        updateTokenHolderCount();
    }

    /**
     * 更新erc20的token holder的余额
     *
     * @param :
     * @return: void
     * @date: 2021/12/17
     */
    private void incrementUpdateErc20TokenHolderBalance() throws Exception {
        // 只有程序正常运行才执行任务
        if (!AppStatusUtil.isRunning()) {
            return;
        }
        try {
            int pageSize = Convert.toInt(XxlJobHelper.getJobParam(), 500);
            PointLog pointLog = pointLogMapper.selectByPrimaryKey(5);
            long oldPosition = Convert.toLong(pointLog.getPosition());
            TxErc20BakExample example = new TxErc20BakExample();
            example.setOrderByClause("id asc limit " + pageSize);
            example.createCriteria().andIdGreaterThan(oldPosition);
            List<TxErc20Bak> list = txErc20BakMapper.selectByExample(example);
            List<TokenHolder> updateParams = new ArrayList<>();
            TaskUtil.console("[erc20]当前页数为[{}]，断点为[{}]", pageSize, oldPosition);
            if (CollUtil.isEmpty(list)) {
                TaskUtil.console("[erc20]该断点[{}]未找到交易", oldPosition);
                return;
            }
            HashMap<String, HashSet<String>> map = new HashMap();
            list.sort(Comparator.comparing(ErcTx::getSeq));
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
            HashMap<String, HashSet<String>> res = subtractToMap(map, getDestroyContracts());
            if (MapUtil.isNotEmpty(res)) {
                // 串行查余额
                res.forEach((contract, addressSet) -> {
                    addressSet.forEach(address -> {
                        try {
                            BigInteger balance = ercServiceImpl.getBalance(contract, ErcTypeEnum.ERC20, address);
                            TokenHolder holder = new TokenHolder();
                            holder.setTokenAddress(contract);
                            holder.setAddress(address);
                            holder.setBalance(balance.toString());
                            updateParams.add(holder);
                            log.info("[erc20] token holder查询到余额[{}]，合约[{}]地址[{}]", balance, contract, address);
                            try {
                                TimeUnit.MILLISECONDS.sleep(100);
                            } catch (InterruptedException interruptedException) {
                                interruptedException.printStackTrace();
                            }
                        } catch (Exception e) {
                            String msg = StrFormatter.format("查询[erc20] token holder的余额失败,contract:{},address:{}", contract, address);
                            XxlJobHelper.log(msg);
                            log.warn(msg, e);
                        }
                    });
                });
            }
            if (CollUtil.isNotEmpty(updateParams)) {
                customTokenHolderMapper.batchUpdate(updateParams);
                TaskUtil.console("更新[erc20] token holder的余额{}", JSONUtil.toJsonStr(updateParams));
                XxlJobHelper.handleSuccess("更新[erc20] token holder的余额成功");
            }
            String newPosition = CollUtil.getLast(list).getId().toString();
            pointLog.setPosition(newPosition);
            pointLogMapper.updateByPrimaryKeySelective(pointLog);
            XxlJobHelper.log("更新[erc20] token holder的余额成功，断点为[{}]->[{}]", oldPosition, newPosition);
        } catch (Exception e) {
            log.error("更新token持有者余额异常", e);
            throw e;
        }
    }

    /**
     * 更新erc721的token holder的余额
     *
     * @param :
     * @return: void
     * @date: 2021/12/17
     */
    private void incrementUpdateErc721TokenHolderBalance() throws Exception {
        // 只有程序正常运行才执行任务
        if (!AppStatusUtil.isRunning()) {
            return;
        }
        try {
            int pageSize = Convert.toInt(XxlJobHelper.getJobParam(), 500);
            PointLog pointLog = pointLogMapper.selectByPrimaryKey(6);
            long oldPosition = Convert.toLong(pointLog.getPosition());
            TxErc721BakExample example = new TxErc721BakExample();
            example.setOrderByClause("id asc limit " + pageSize);
            example.createCriteria().andIdGreaterThan(oldPosition);
            List<TxErc721Bak> list = txErc721BakMapper.selectByExample(example);
            List<TokenHolder> updateParams = new ArrayList<>();
            TaskUtil.console("[erc721]当前页数为[{}]，断点为[{}]", pageSize, oldPosition);
            if (CollUtil.isEmpty(list)) {
                TaskUtil.console("[erc721]该断点[{}]未找到交易", oldPosition);
                return;
            }
            HashMap<String, HashSet<String>> map = new HashMap();
            list.sort(Comparator.comparing(ErcTx::getSeq));
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
            HashMap<String, HashSet<String>> res = subtractToMap(map, getDestroyContracts());
            if (MapUtil.isNotEmpty(res)) {
                // 串行查余额
                res.forEach((contract, addressSet) -> {
                    addressSet.forEach(address -> {
                        try {
                            BigInteger balance = ercServiceImpl.getBalance(contract, ErcTypeEnum.ERC721, address);
                            TokenHolder holder = new TokenHolder();
                            holder.setTokenAddress(contract);
                            holder.setAddress(address);
                            holder.setBalance(balance.toString());
                            updateParams.add(holder);
                            log.info("[erc721] token holder查询到余额[{}]，合约[{}]地址[{}]", balance, contract, address);
                            try {
                                TimeUnit.MILLISECONDS.sleep(100);
                            } catch (InterruptedException interruptedException) {
                                interruptedException.printStackTrace();
                            }
                        } catch (Exception e) {
                            String msg = StrFormatter.format("查询[erc721] token holder的余额失败,contract:{},address:{}", contract, address);
                            XxlJobHelper.log(msg);
                            log.warn(msg, e);
                        }
                    });
                });
            }
            if (CollUtil.isNotEmpty(updateParams)) {
                customTokenHolderMapper.batchUpdate(updateParams);
                TaskUtil.console("更新[erc721] token holder的余额{}", JSONUtil.toJsonStr(updateParams));
                XxlJobHelper.handleSuccess("更新[erc721] token holder的余额成功");
            }
            String newPosition = CollUtil.getLast(list).getId().toString();
            pointLog.setPosition(newPosition);
            pointLogMapper.updateByPrimaryKeySelective(pointLog);
            XxlJobHelper.log("更新[erc721] token holder的余额成功，断点为[{}]->[{}]", oldPosition, newPosition);
        } catch (Exception e) {
            log.error("更新token持有者余额异常", e);
            throw e;
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
        List<TokenInventoryWithBLOBs> batch = null;
        do {
            // 当前失败的条数
            AtomicInteger errorNum = new AtomicInteger(0);
            // 当次更新的条数
            AtomicInteger updateNum = new AtomicInteger(0);
            try {
                int batchSize = Convert.toInt(XxlJobHelper.getJobParam(), 100);
                TokenInventoryExample condition = new TokenInventoryExample();
                condition.setOrderByClause(" id desc limit " + page * batchSize + "," + batchSize);
                condition.createCriteria().andRetryNumLessThan(tokenRetryNum).andImageIsNull();
                batch = tokenInventoryMapper.selectByExampleWithBLOBs(condition);
                List<TokenInventoryWithBLOBs> updateParams = new ArrayList<>();
                if (CollUtil.isNotEmpty(batch)) {
                    int finalPage = page;
                    batch.forEach(inventory -> {
                        TokenInventoryWithBLOBs updateTokenInventory = new TokenInventoryWithBLOBs();
                        updateTokenInventory.setTokenId(inventory.getTokenId());
                        updateTokenInventory.setTokenAddress(inventory.getTokenAddress());
                        updateTokenInventory.setTokenUrl(inventory.getTokenUrl());
                        try {
                            if (StrUtil.isNotBlank(inventory.getTokenUrl())) {
                                Request request = new Request.Builder().url(inventory.getTokenUrl()).build();
                                Response response = CustomHttpClient.getOkHttpClient().newCall(request).execute();
                                if (response.code() == 200) {
                                    String resp = response.body().string();
                                    UpdateTokenInventory newTi = JSONUtil.toBean(resp, UpdateTokenInventory.class);
                                    newTi.setTokenId(inventory.getTokenId());
                                    newTi.setTokenAddress(inventory.getTokenAddress());
                                    boolean changed = false;
                                    // 只要有一个属性变动就添加到更新列表中
                                    if (ObjectUtil.isNull(inventory.getImage()) && ObjectUtil.isNotNull(newTi.getImageUrl())) {
                                        updateTokenInventory.setImage(newTi.getImageUrl());
                                        changed = true;
                                    } else if (ObjectUtil.isNotNull(inventory.getImage()) && ObjectUtil.isNotNull(newTi.getImageUrl()) && !inventory.getImage().equals(newTi.getImageUrl())) {
                                        updateTokenInventory.setImage(newTi.getImageUrl());
                                        changed = true;
                                    }
                                    if (ObjectUtil.isNull(inventory.getImage()) && ObjectUtil.isNotNull(newTi.getImage())) {
                                        updateTokenInventory.setImage(newTi.getImage());
                                        changed = true;
                                    } else if (ObjectUtil.isNotNull(inventory.getImage()) && ObjectUtil.isNotNull(newTi.getImage()) && !inventory.getImage().equals(newTi.getImage())) {
                                        updateTokenInventory.setImage(newTi.getImage());
                                        changed = true;
                                    }
                                    if (ObjectUtil.isNull(inventory.getDescription()) && ObjectUtil.isNotNull(newTi.getDescription())) {
                                        updateTokenInventory.setDescription(newTi.getDescription());
                                        changed = true;
                                    } else if (ObjectUtil.isNotNull(inventory.getDescription()) && ObjectUtil.isNotNull(newTi.getDescription()) && !inventory.getDescription()
                                                                                                                                                             .equals(newTi.getDescription())) {
                                        updateTokenInventory.setDescription(newTi.getDescription());
                                        changed = true;
                                    }
                                    if (ObjectUtil.isNull(inventory.getName()) && ObjectUtil.isNotNull(newTi.getName())) {
                                        updateTokenInventory.setName(newTi.getName());
                                        changed = true;
                                    } else if (ObjectUtil.isNotNull(inventory.getName()) && ObjectUtil.isNotNull(newTi.getName()) && !inventory.getName().equals(newTi.getName())) {
                                        updateTokenInventory.setName(newTi.getName());
                                        changed = true;
                                    }
                                    if (changed) {
                                        updateNum.getAndIncrement();
                                        updateTokenInventory.setRetryNum(0);
                                        updateParams.add(updateTokenInventory);
                                        log.info("库存有属性变动需要更新,token[{}]", JSONUtil.toJsonStr(updateTokenInventory));
                                    }
                                } else {
                                    errorNum.getAndIncrement();
                                    updateTokenInventory.setRetryNum(inventory.getRetryNum() + 1);
                                    updateParams.add(updateTokenInventory);
                                    log.warn("http请求异常：http状态码:{},http消息:{},当前标识为:{},token:{}", response.code(), response.message(), pageNum, JSONUtil.toJsonStr(updateTokenInventory));
                                }
                            } else {
                                errorNum.getAndIncrement();
                                updateTokenInventory.setRetryNum(inventory.getRetryNum() + 1);
                                updateParams.add(updateTokenInventory);
                                String msg = StrUtil.format("请求TokenURI为空,当前标识为:{},token:{}", finalPage, JSONUtil.toJsonStr(updateTokenInventory));
                                XxlJobHelper.log(msg);
                                log.warn(msg);
                            }
                        } catch (Exception e) {
                            errorNum.getAndIncrement();
                            updateTokenInventory.setRetryNum(inventory.getRetryNum() + 1);
                            updateParams.add(updateTokenInventory);
                            log.warn(StrUtil.format("全量更新token库存信息异常,当前标识为:{},token:{}", finalPage, JSONUtil.toJsonStr(updateTokenInventory)), e);
                        }
                    });
                }
                if (CollUtil.isNotEmpty(updateParams)) {
                    customTokenInventoryMapper.batchUpdateTokenInfo(updateParams);
                    XxlJobHelper.log("全量更新token库存信息{}", JSONUtil.toJsonStr(updateParams));
                }
                String msg = StrUtil.format("全量更新token库存信息:当前标识为:{},查询到的条数为{},已更新的条数为:{},失败的条数为:{}", page, batch.size(), updateNum.get(), errorNum.get());
                XxlJobHelper.log(msg);
                log.info(msg);
            } catch (Exception e) {
                log.error(StrUtil.format("全量更新token库存信息异常,当前标识为:{}", page), e);
            } finally {
                page++;
            }
        } while (CollUtil.isNotEmpty(batch));
    }

    /**
     * 更新token库存信息=>增量更新
     *
     * @return void
     * @date 2021/4/26
     */
    private void cronIncrementUpdateTokenInventory() {
        // 只有程序正常运行才执行任务
        if (!AppStatusUtil.isRunning()) {
            return;
        }
        // 当前失败的条数
        AtomicInteger errorNum = new AtomicInteger(0);
        // 当次更新的条数
        AtomicInteger updateNum = new AtomicInteger(0);
        PointLog pointLog = pointLogMapper.selectByPrimaryKey(7);
        Long oldPosition = Convert.toLong(pointLog.getPosition());
        int batchSize = Convert.toInt(XxlJobHelper.getJobParam(), 10);
        XxlJobHelper.log("当前页数为[{}]，断点为[{}]", batchSize, oldPosition);
        try {
            TokenInventoryExample condition = new TokenInventoryExample();
            condition.setOrderByClause(" id asc limit " + batchSize);
            condition.createCriteria().andIdGreaterThan(oldPosition).andRetryNumLessThan(tokenRetryNum).andImageIsNull();
            List<TokenInventoryWithBLOBs> batch = tokenInventoryMapper.selectByExampleWithBLOBs(condition);
            if (CollUtil.isNotEmpty(batch)) {
                List<TokenInventoryWithBLOBs> updateParams = new ArrayList<>();
                batch.forEach(inventory -> {
                    TokenInventoryWithBLOBs updateTokenInventory = new TokenInventoryWithBLOBs();
                    updateTokenInventory.setTokenId(inventory.getTokenId());
                    updateTokenInventory.setTokenAddress(inventory.getTokenAddress());
                    updateTokenInventory.setTokenUrl(inventory.getTokenUrl());
                    try {
                        if (StrUtil.isNotBlank(inventory.getTokenUrl())) {
                            Request request = new Request.Builder().url(inventory.getTokenUrl()).build();
                            Response response = CustomHttpClient.getOkHttpClient().newCall(request).execute();
                            if (response.code() == 200) {
                                String resp = response.body().string();
                                UpdateTokenInventory newTi = JSONUtil.toBean(resp, UpdateTokenInventory.class);
                                newTi.setTokenId(inventory.getTokenId());
                                newTi.setTokenAddress(inventory.getTokenAddress());
                                boolean changed = false;
                                // 只要有一个属性变动就添加到更新列表中
                                if (ObjectUtil.isNull(inventory.getImage()) && ObjectUtil.isNotNull(newTi.getImageUrl())) {
                                    updateTokenInventory.setImage(newTi.getImageUrl());
                                    changed = true;
                                } else if (ObjectUtil.isNotNull(inventory.getImage()) && ObjectUtil.isNotNull(newTi.getImageUrl()) && !inventory.getImage().equals(newTi.getImageUrl())) {
                                    updateTokenInventory.setImage(newTi.getImageUrl());
                                    changed = true;
                                }
                                if (ObjectUtil.isNull(inventory.getImage()) && ObjectUtil.isNotNull(newTi.getImage())) {
                                    updateTokenInventory.setImage(newTi.getImage());
                                    changed = true;
                                } else if (ObjectUtil.isNotNull(inventory.getImage()) && ObjectUtil.isNotNull(newTi.getImage()) && !inventory.getImage().equals(newTi.getImage())) {
                                    updateTokenInventory.setImage(newTi.getImage());
                                    changed = true;
                                }
                                if (ObjectUtil.isNull(inventory.getDescription()) && ObjectUtil.isNotNull(newTi.getDescription())) {
                                    updateTokenInventory.setDescription(newTi.getDescription());
                                    changed = true;
                                } else if (ObjectUtil.isNotNull(inventory.getDescription()) && ObjectUtil.isNotNull(newTi.getDescription()) && !inventory.getDescription()
                                                                                                                                                         .equals(newTi.getDescription())) {
                                    updateTokenInventory.setDescription(newTi.getDescription());
                                    changed = true;
                                }
                                if (ObjectUtil.isNull(inventory.getName()) && ObjectUtil.isNotNull(newTi.getName())) {
                                    updateTokenInventory.setName(newTi.getName());
                                    changed = true;
                                } else if (ObjectUtil.isNotNull(inventory.getName()) && ObjectUtil.isNotNull(newTi.getName()) && !inventory.getName().equals(newTi.getName())) {
                                    updateTokenInventory.setName(newTi.getName());
                                    changed = true;
                                }
                                if (changed) {
                                    updateNum.getAndIncrement();
                                    updateTokenInventory.setRetryNum(0);
                                    updateParams.add(updateTokenInventory);
                                    String msg = StrUtil.format("库存有属性变动需要更新,token[{}]", JSONUtil.toJsonStr(updateTokenInventory));
                                    XxlJobHelper.log(msg);
                                    log.info(msg);
                                }
                            } else {
                                errorNum.getAndIncrement();
                                updateTokenInventory.setRetryNum(inventory.getRetryNum() + 1);
                                updateParams.add(updateTokenInventory);
                                String msg = StrUtil.format("http请求异常：http状态码:{},http消息:{},断点:{},token:{}", response.code(), response.message(), oldPosition, JSONUtil.toJsonStr(updateTokenInventory));
                                XxlJobHelper.log(msg);
                                log.warn(msg);
                            }
                        } else {
                            errorNum.getAndIncrement();
                            updateTokenInventory.setRetryNum(inventory.getRetryNum() + 1);
                            updateParams.add(updateTokenInventory);
                            String msg = StrUtil.format("请求TokenURI为空,断点:{},token：{}", oldPosition, JSONUtil.toJsonStr(updateTokenInventory));
                            XxlJobHelper.log(msg);
                            log.warn(msg);
                        }
                    } catch (Exception e) {
                        errorNum.getAndIncrement();
                        updateTokenInventory.setRetryNum(inventory.getRetryNum() + 1);
                        updateParams.add(updateTokenInventory);
                        log.warn(StrUtil.format("增量更新token库存信息异常,断点:{},token：{}", oldPosition, JSONUtil.toJsonStr(updateTokenInventory)), e);
                    }
                });
                if (CollUtil.isNotEmpty(updateParams)) {
                    customTokenInventoryMapper.batchUpdateTokenInfo(updateParams);
                }
                TokenInventory lastTokenInventory = CollUtil.getLast(batch);
                String newPosition = Convert.toStr(lastTokenInventory.getId());
                pointLog.setPosition(newPosition);
                pointLogMapper.updateByPrimaryKeySelective(pointLog);
                String msg = StrUtil.format("增量更新token库存信息:断点为[{}]->[{}],查询到的条数为:{},已更新的条数为:{},失败的条数为:{}", oldPosition, newPosition, batch.size(), updateNum.get(), errorNum.get());
                XxlJobHelper.log(msg);
                log.info(msg);
                XxlJobHelper.handleSuccess(msg);
            } else {
                XxlJobHelper.log("增量更新token库存信息完成，未找到数据，断点为[{}]", oldPosition);
            }
        } catch (Exception e) {
            log.error(StrUtil.format("增量更新token库存信息异常,断点:{}", oldPosition), e);
        }
    }

    private void contractErc20DestroyUpdateBalance() {
        try {
            List<DestroyContract> tokenList = customTokenMapper.findDestroyContract(ErcTypeEnum.ERC20.getDesc());
            if (CollUtil.isNotEmpty(tokenList)) {
                List<TokenHolder> updateList = new ArrayList<>();
                for (DestroyContract destroyContract : tokenList) {
                    try {
                        BigInteger balance = ercServiceImpl.getErc20HistoryBalance(destroyContract.getTokenAddress(),
                                                                                   destroyContract.getAccount(),
                                                                                   BigInteger.valueOf(destroyContract.getContractDestroyBlock() - 1));
                        TokenHolder tokenHolder = new TokenHolder();
                        tokenHolder.setTokenAddress(destroyContract.getTokenAddress());
                        tokenHolder.setAddress(destroyContract.getAccount());
                        tokenHolder.setBalance(balance.toString());
                        updateList.add(tokenHolder);
                    } catch (Exception e) {
                        log.error(StrUtil.format("已销毁的erc20合约[{}]账号[{}]更新余额异常", destroyContract.getTokenAddress(), destroyContract.getAccount()), e);
                    }
                }
                if (CollUtil.isNotEmpty(updateList)) {
                    customTokenHolderMapper.batchUpdate(updateList);
                    Set<String> destroyContractSet = updateList.stream().map(TokenHolderKey::getTokenAddress).collect(Collectors.toSet());
                    for (String destroyContract : destroyContractSet) {
                        Token token = new Token();
                        token.setAddress(destroyContract);
                        token.setContractDestroyUpdate(true);
                        tokenMapper.updateByPrimaryKeySelective(token);
                    }
                }
            }
        } catch (Exception e) {
            log.error("更新已销毁的erc20合约余额异常", e);
        }
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

    /**
     * 获取ercToken
     *
     * @param :
     * @return: java.util.List<com.platon.browser.v0152.bean.ErcToken>
     * @date: 2021/11/30
     */
    private List<ErcToken> getErcTokens() {
        List<ErcToken> ercTokens = new ArrayList<>();
        List<Token> tokens = tokenMapper.selectByExample(null);
        tokens.forEach(token -> {
            ErcToken et = new ErcToken();
            BeanUtils.copyProperties(token, et);
            ErcTypeEnum typeEnum = ErcTypeEnum.valueOf(token.getType().toUpperCase());
            et.setTypeEnum(typeEnum);
            ercTokens.add(et);
        });
        return ercTokens;
    }

    /**
     * 获取销毁的合约
     *
     * @param :
     * @return: java.util.Set<java.lang.String>
     * @date: 2021/11/30
     */
    private Set<String> getDestroyContracts() {
        Set<String> destroyContracts = new HashSet<>();
        List<String> list = customAddressMapper.findContractDestroy(null);
        destroyContracts.addAll(list);
        return destroyContracts;
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
                        TaskUtil.console("更新token[{}]对应的持有人的数量[{}]", token.getAddress(), token.getHolder());
                    }
                });
            });
        }
        if (CollUtil.isNotEmpty(updateTokenList)) {
            customTokenMapper.batchUpdateTokenHolder(updateTokenList);
        }
        XxlJobHelper.log("更新token对应的持有人的数量完成");
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

}
