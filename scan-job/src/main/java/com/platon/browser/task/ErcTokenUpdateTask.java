package com.platon.browser.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.platon.browser.bean.http.CustomHttpClient;
import com.platon.browser.dao.custommapper.CustomToken1155InventoryMapper;
import com.platon.browser.dao.custommapper.CustomTokenInventoryMapper;
import com.platon.browser.dao.custommapper.CustomTokenMapper;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.dao.entity.Token;
import com.platon.browser.dao.entity.Token1155InventoryWithBLOBs;
import com.platon.browser.dao.entity.TokenInventoryWithBLOBs;
import com.platon.browser.dao.mapper.NetworkStatMapper;
import com.platon.browser.dao.mapper.Token1155InventoryMapper;
import com.platon.browser.dao.mapper.TokenInventoryMapper;
import com.platon.browser.enums.ErcTypeEnum;
import com.platon.browser.service.erc.ErcServiceImpl;
import com.platon.browser.utils.AppStatusUtil;
import com.platon.browser.utils.TaskUtil;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
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
    private CustomTokenMapper customTokenMapper;

    @Resource
    private NetworkStatMapper networkStatMapper;

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
            log.debug(">>>>开始执行:全量更新token的总供应量");
            StopWatch watch = new StopWatch("全量更新token的总供应量");
            watch.start("全量更新token的总供应量");

            updateTokenTotalSupply();

            watch.stop();
            log.debug("结束执行:全量更新token的总供应量，耗时统计:{}ms", watch.getLastTaskTimeMillis());
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
        log.debug(">>>>开始执行:全量更新token库存信息");
        StopWatch watch = new StopWatch("全量更新token库存信息");
        watch.start("更新ERC721库存信息");
        updateToken721Inventory();
        watch.stop();

        watch.start("更新ERC1155库存信息");
        updateToken1155Inventory();
        watch.stop();
        log.debug("结束执行:全量更新token库存信息，耗时统计:{}", watch.prettyPrint());
    }

    /**
     * lvxiaoyi:2023/03/29
     *
     * 销毁的合约，需要更新一次最后的余额，以便scan还能从scan的db中查询到token的有关信息。
     * 这个任务不再需要，而是有com.platon.browser.v0152.analyzer.ErcTokenHolderAnalyzer#analyze(java.util.List)来实时维护token的每个账户余额
     * 但是由此带来的一个缺陷是：不再支持创建合约时就铸币，但是没有发射transfer事件的非标准合约。现象就是db中，会出现balanace<0的情况，对此现象的解释：
     * 创建合约时就铸币，因为没有transfer事件，所以scan没有记录到minter和余额；然后minter转账token到其他人，因为minter需要减持，所以造成负数
     *
     */
    //@XxlJob("contractDestroyUpdateBalanceJobHandler")
    public void contractDestroyUpdateBalance() {
        if (!AppStatusUtil.isRunning()) {
            return;
        }
    }

    /**
     * 更新ERC20和Erc721Enumeration token的总供应量===》全量更新
     * lvxiaoyi, 2023/03/29，
     * 更新token的totalSupply，理论上应该通过监控合约的增发事件来获取（或者通过合约交易如mint等方法来获取），但是有些合约可能不是标准合约，修改totalSupply是通过自定义方法进行的，也没有发射标准的事件如：事件铸币、增发等事件。
     *
     * 因此，只能通过全量取链上查询totalSupply。
     *
     * 1. 如果合约已经销毁，则不再需要更新totalSupply，但是需要跟新销毁前一个区块上销毁人的余额
     * 2. todo: 后续可以考虑不再支持标准合约的totalSupply更新
     *
     *
     * @return void
     * @date 2021/1/18
     */
    private void updateTokenTotalSupply() {
        // 只有程序正常运行才执行任务
        if (!AppStatusUtil.isRunning()) {
            log.debug("全量更新token的总供应量任务执行失败，因为应用程序不在运行状态");
            return;
        }
        log.debug("开始执行:全量更新token的总供应量任务");

        StopWatch watch = new StopWatch();
        watch.start("全量更新token的总供应量");

        NetworkStat networkStat = networkStatMapper.selectByPrimaryKey(1);
        BigInteger currentBlockNumber = BigInteger.ZERO;
        if (networkStat!=null){
            currentBlockNumber = BigInteger.valueOf(networkStat.getCurNumber());
        }
        int pageNo = 1;
        int pageSize = 100;
        int offset = 0;
        int countReturned = 0;
        do {
            offset = (pageNo - 1) * pageSize;

            /**
             * 注意：scan-agent可能落后于特殊节点，即在db中，合约还没有销毁，但是特殊节点已经是销毁状态了。
             * 所以，获取此结果后，如果需要到特殊节点查询此合约的某些信息，就会返回0x
             */
            List<Token> tokenList = customTokenMapper.listNotDestroyedToUpdateTotalSupply(offset, pageSize);
            countReturned = tokenList.size();

            if (countReturned == 0) {
                break;
            }

            List<Token> totalSupplyUpdated = new ArrayList<>();
            CountDownLatch latch = new CountDownLatch(tokenList.size());
            for (Token token : tokenList) {
                BigInteger finalCurrentBlockNumber = currentBlockNumber;
                TOKEN_UPDATE_POOL.submit(() -> {
                    try {
                        // 查询总供应量
                        BigInteger totalSupply = ercServiceImpl.getTotalSupply(token.getAddress(), ErcTypeEnum.getErcTypeEnum(token.getType()), finalCurrentBlockNumber);
                        totalSupply = totalSupply == null ? BigInteger.ZERO : totalSupply;
                        if (ObjectUtil.isNull(token.getTotalSupply()) || !token.getTotalSupply().equalsIgnoreCase(totalSupply.toString())) {
                            TaskUtil.console("token[{}]的总供应量有变动需要更新旧值[{}]新值[{}]", token.getAddress(), token.getTotalSupply(), totalSupply);
                            // 有变动添加到更新列表中
                            token.setTotalSupply(totalSupply.toString());
                            totalSupplyUpdated.add(token);
                        }
                    } catch (Exception e) {
                        XxlJobHelper.log(StrUtil.format("该token[{}]查询总供应量异常", token.getAddress()));
                        log.error("查询总供应量异常", e);
                    } finally {
                        latch.countDown();
                    }
                });
            }
            if (totalSupplyUpdated.size() > 0) {
                customTokenMapper.batchUpdateTokenTotalSupply(totalSupplyUpdated);
            }
            pageNo++;
        }while(countReturned == pageSize);

        watch.stop();
        log.debug("结束执行:全量更新token的总供应量任务，耗时统计:{}ms", watch.getLastTaskTimeMillis());
        XxlJobHelper.log("全量更新token的总供应量成功");
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

                            log.debug("NFT(ERC_721)有属性更新,token[{}]", JSONUtil.toJsonStr(inventory));
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

                            log.debug("NFT(ERC_1155)有属性更新,token[{}]", JSONUtil.toJsonStr(inventory));
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
     * 更新token对应的持有人的数量
     * todo: 在涉及的表记录数量多时，这个统计效率并不高。最好的做法是在查看token的持有人数量时，再实时统计。就是说，这个数据没有必要持续统计并更新到表中。
     * @param
     * @return void
     * @date 2021/3/17
     */

    @XxlJob("updateTokenHolderCountJobHandler")
    public void updateTokenHolderCount() {
        log.debug("开始执行:统计token的持有人数量任务");

        StopWatch watch = new StopWatch();
        watch.start("统计token的持有人数量");

        customTokenMapper.updateErc20TokenHolderCount();
        customTokenMapper.updateErc1155TokenHolderCount();

        watch.stop();
        log.debug("结束执行:统计token的持有人数量任务，耗时统计:{}ms", watch.getLastTaskTimeMillis());
        XxlJobHelper.log("统计token的持有人数量完成");
    }
}
