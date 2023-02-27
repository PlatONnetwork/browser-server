package com.platon.browser.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.platon.browser.bean.ErcToken;
import com.platon.browser.bean.TokenHolderCount;
import com.platon.browser.bean.http.CustomHttpClient;
import com.platon.browser.dao.custommapper.*;
import com.platon.browser.dao.entity.Token;
import com.platon.browser.dao.entity.Token1155InventoryWithBLOBs;
import com.platon.browser.dao.entity.TokenInventoryWithBLOBs;
import com.platon.browser.dao.mapper.Token1155InventoryMapper;
import com.platon.browser.dao.mapper.TokenInventoryMapper;
import com.platon.browser.dao.mapper.TokenMapper;
import com.platon.browser.enums.ErcTypeEnum;
import com.platon.browser.service.erc.ErcServiceImpl;
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
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * token定时任务
 * todo: lvxiaoyi, 2023/02/24
 * 关于合约销毁
 * 合约上可能有主币，如果合约支持主币回转（一般都是回转到销毁者）
 * SCAN暂时没有处理这种case，因为目前，scan显示余额的地方，都是直接调用链上接口查询的（而不是调用scan接口查询的）
 *
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
    private TokenInventoryMapper token721InventoryMapper;

    @Resource
    private Token1155InventoryMapper token1155InventoryMapper;

    @Resource
    private CustomTokenInventoryMapper customToken721InventoryMapper;

    @Resource
    private CustomToken1155InventoryMapper customToken1155InventoryMapper;


    @Resource
    private CustomTokenHolderMapper customTokenHolderMapper;

    @Resource
    private CustomToken1155HolderMapper customToken1155HolderMapper;

    @Resource
    private CustomTokenMapper customTokenMapper;

    @Resource
    private TokenMapper tokenMapper;

    @Resource
    private CustomAddressMapper customAddressMapper;

    @Resource
    private ErcServiceImpl ercServiceImpl;


    private static final int TOKEN_BATCH_SIZE = 10;

    private static final ExecutorService TOKEN_UPDATE_POOL = Executors.newFixedThreadPool(TOKEN_BATCH_SIZE);
    /**
     * 全量更新token的总供应量
     * 每5分钟更新
     *
     * 根据：表token中所有记录，
     * 通过：rpc查询每个token的totalSupply，
     * 更新：表token中记录。
     *
     * @return void
     * @date 2021/1/18
     */
    @XxlJob("totalUpdateTokenTotalSupplyJobHandler")
    public void totalUpdateTokenTotalSupply() {
        try {
            updateTokenTotalSupply();
        } catch (Exception e) {
            log.warn("全量更新token的总供应量异常", e);
            throw e;
        }
    }


    /**
     * 全量更新token库存信息
     * 每天凌晨1点执行一次
     *
     * 参考：
     * ERC721（Token721_Inventory表）, ERC1155（Token1155_Inventory表）中的保存NFT的信息
     * 有些字段需要需要补齐，补齐的条件是：
     * token_url is not null and image is null and retry_num < 3
     * 发现有这样的记录，就尝试补齐
     *
     * @param
     * @return void
     * @date 2021/4/17
     */
    @XxlJob("totalUpdateTokenInventoryJobHandler")
    public void totalUpdateTokenInventory() {
        try {
            updateToken721Inventory();
            updateToken1155Inventory();
        } catch (Exception e) {
            log.error("更新token库存信息", e);
        }
    }


    /**
     * 更新ERC20和Erc721Enumeration token的总供应量===》全量更新
     * todo: lvxiaoyi, 2023/02/24，此功能可以考虑scan不再支持非标准的合约
     * 有些合约可能不是标准的合约，没有发射必要的事件，比如铸币，增发。
     * 这样就可能造成链上查询token总量变化了，但是scan没有发现。
     * 于是这个任务遍历所有token，去链上查询totalSupply.
     *
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
        //
        //
        // 遍历全网token
        //
        for (ErcToken token : tokens) {
            if (token.isDirty()) {
                //todo:这个dirty并没有赋值（db表中没有对应字段），所以，这个if(){...}是无效的
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
     * 更新token库存信息
     *
     * @param :
     * @return: void
     * @date: 2022/9/21
     */
    private void updateToken721Inventory() {
        // 只有程序正常运行才执行任务
        if (!AppStatusUtil.isRunning()) {
            return;
        }

        // 分页更新token库存相关信息
        int batchSize = Convert.toInt(XxlJobHelper.getJobParam(), 100);
        int pageNo = 1;

        List<TokenInventoryWithBLOBs> nftList = null;
        do {
            int offset = (pageNo-1) * batchSize;
            //查询需要更新的NFT资产列表，（合约是有效的，没有被销毁）
            nftList = token721InventoryMapper.listValidTokenInventoryToRefresh(offset, batchSize);
            pageNo++;

            if (CollUtil.isNotEmpty(nftList)) {
                nftList.forEach(inventory -> {
                    // 重试次数+1
                    inventory.setRetryNum(inventory.getRetryNum() + 1);
                    if (inventory.getTokenUrl().startsWith("ipfs://")){
                        String tokenUrl = inventory.getTokenUrl().replace("ipfs://", "https://ipfs.io/ipfs/");
                        inventory.setTokenUrl(tokenUrl);
                    }
                    Request request = new Request.Builder().url(inventory.getTokenUrl()).build();
                    try (Response response = CustomHttpClient.getOkHttpClient().newCall(request).execute()) {
                        if (response.code() == 200 && response.body() !=null) {
                            String resp = response.body().string();
                            TokenInventoryWithBLOBs newTi = JSONUtil.toBean(resp, TokenInventoryWithBLOBs.class);
                            inventory.setImage(newTi.getImage());
                            inventory.setDescription(newTi.getDescription());
                            inventory.setName(newTi.getName());

                            log.info("NFT(ERC_721)有属性更新,token[{}]", JSONUtil.toJsonStr(inventory));
                        } else {
                            log.warn("NFT(ERC_721)属性查询http响应状态码异常:{},http消息:{},token:{}", response.code(), response.message(), JSONUtil.toJsonStr(inventory));
                        }
                    } catch (Exception e) {
                        log.warn(StrUtil.format("NFT(ERC_721)属性查询异常,token:{}", JSONUtil.toJsonStr(inventory)), e);
                    }
                });

                customToken721InventoryMapper.batchUpdateTokenInfo(nftList);
                XxlJobHelper.log("NFT(ERC_721)属性更新：{}", JSONUtil.toJsonStr(nftList));
            }
        } while (CollUtil.isNotEmpty(nftList) && nftList.size() < batchSize);
    }

    /**
     * 更新token库存信息
     *
     * @param :
     * @return: void
     * @date: 2022/9/21
     */
    private void updateToken1155Inventory() {
        // 只有程序正常运行才执行任务
        if (!AppStatusUtil.isRunning()) {
            return;
        }
        // 分页更新token库存相关信息
        int batchSize = Convert.toInt(XxlJobHelper.getJobParam(), 100);
        int pageNo = 1;

        List<Token1155InventoryWithBLOBs> nftList = null;
        do {
            int offset = (pageNo-1) * batchSize;
            //查询需要更新的NFT资产列表，（合约是有效的，没有被销毁）
            nftList = token1155InventoryMapper.listValidTokenInventoryToRefresh(offset, batchSize);
            pageNo++;
            if (CollUtil.isNotEmpty(nftList)) {
                nftList.forEach(inventory -> {
                    // 重试次数+1
                    inventory.setRetryNum(inventory.getRetryNum() + 1);
                    if (inventory.getTokenUrl().startsWith("ipfs://")){
                        String tokenUrl = inventory.getTokenUrl().replace("ipfs://", "https://ipfs.io/ipfs/");
                        inventory.setTokenUrl(tokenUrl);
                    }
                    Request request = new Request.Builder().url(inventory.getTokenUrl()).build();
                    try (Response response = CustomHttpClient.getOkHttpClient().newCall(request).execute()) {
                        if (response.code() == 200 && response.body() !=null) {
                            String resp = response.body().string();
                            Token1155InventoryWithBLOBs newTi = JSONUtil.toBean(resp, Token1155InventoryWithBLOBs.class);
                            inventory.setImage(newTi.getImage());
                            inventory.setDescription(newTi.getDescription());
                            inventory.setName(newTi.getName());
                            inventory.setDecimal(newTi.getDecimal());

                            log.info("NFT(ERC_1155)有属性更新,token[{}]", JSONUtil.toJsonStr(inventory));
                        } else {
                            log.warn("NFT(ERC_1155)属性查询http响应状态码异常:{},http消息:{},token:{}", response.code(), response.message(), JSONUtil.toJsonStr(inventory));
                        }
                    } catch (Exception e) {
                        log.warn(StrUtil.format("NFT(ERC_1155)属性查询异常,token:{}", JSONUtil.toJsonStr(inventory)), e);
                    }
                });

                customToken1155InventoryMapper.batchInsertOrUpdateSelective(nftList);
                XxlJobHelper.log("NFT(ERC_1155)属性更新：{}", JSONUtil.toJsonStr(nftList));
            }
        } while (CollUtil.isNotEmpty(nftList) && nftList.size() < batchSize);
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
        List<TokenHolderCount> token1155List = customToken1155HolderMapper.findToken1155Holder();
        if (CollUtil.isNotEmpty(token1155List) && CollUtil.isNotEmpty(tokenList)) {
            token1155List.forEach(tokenHolderCount -> {
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
}
