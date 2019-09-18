package com.platon.browser.task;

import com.platon.browser.bean.CollectResult;
import com.platon.browser.dao.mapper.CustomBlockMapper;
import com.platon.browser.dto.CustomBlock;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.cache.CacheHolder;
import com.platon.browser.engine.cache.NodeCache;
import com.platon.browser.engine.stage.BlockChainStage;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.service.BlockService;
import com.platon.browser.service.CandidateService;
import com.platon.browser.service.DbService;
import com.platon.browser.service.TransactionService;
import com.platon.browser.utils.HexTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/10 09:14
 * @Description: 区块和交易同步任务
 */
@Component
public class BlockSyncTask {
    private static Logger logger = LoggerFactory.getLogger(BlockSyncTask.class);

    @Autowired
    private CustomBlockMapper customBlockMapper;
    @Autowired
    private DbService dbService;
    @Autowired
    private BlockChain blockChain;
    @Autowired
    private BlockService blockService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private CandidateService candidateService;
    @Autowired
    private CacheHolder cacheHolder;

    // 已采集入库的最高块
    private long commitBlockNumber = 0;

    // 每一批次采集区块的数量
    @Value("${platon.web3j.collect.batch-size}")
    private int collectBatchSize;

    /**
     * 初始化已有业务数据
     */
    public void init () throws Exception {
        NodeCache nodeCache = cacheHolder.getNodeCache();
        BlockChainStage stageData = cacheHolder.getStageData();
        Map<String,String> nodeNameMap = cacheHolder.getNodeNameMap();

        ExecutorService es = Executors.newFixedThreadPool(collectBatchSize);
        blockService.init(es);
        transactionService.init(es);
        // 从数据库查询最高块号，赋值给commitBlockNumber
        Long maxBlockNumber = customBlockMapper.selectMaxBlockNumber();
        // 更新当前所在周期的区块奖励和结算周期质押奖励, 初始化共识验证人列表
        Long initBlockNumber = 1L;
        if (maxBlockNumber != null && maxBlockNumber > 0) {
            commitBlockNumber = maxBlockNumber;
            blockChain.updateReward(maxBlockNumber);
            initBlockNumber = maxBlockNumber;
        }else{
            blockChain.updateReward(0L);
        }
        // 设定指定块号时的结算周期验证人
        CandidateService.CandidateResult cr = candidateService.getVerifiers(initBlockNumber);
        blockChain.getPreVerifier().clear();
        cr.getPre().stream().filter(Objects::nonNull).forEach(node -> blockChain.getPreVerifier().put(HexTool.prefix(node.getNodeId()), node));
        blockChain.getCurVerifier().clear();
        cr.getCur().stream().filter(Objects::nonNull).forEach(node -> blockChain.getCurVerifier().put(HexTool.prefix(node.getNodeId()), node));
        // 设定指定块号时的共识周期验证人
        cr = candidateService.getValidators(initBlockNumber);
        blockChain.getPreValidator().clear();
        cr.getPre().stream().filter(Objects::nonNull).forEach(node -> blockChain.getPreValidator().put(HexTool.prefix(node.getNodeId()), node));
        blockChain.getCurValidator().clear();
        cr.getCur().stream().filter(Objects::nonNull).forEach(node -> blockChain.getCurValidator().put(HexTool.prefix(node.getNodeId()), node));
        //更新当前共识周期期望出块数
        blockChain.updateCurConsensusExpectBlockCount(blockChain.getCurValidator().size());
        /*
         * 从第一块同步的时候，结算周期验证人和共识周期验证人是链上内置的
         * 查询内置共识周期验证人初始化blockChain的curValidator属性
         * 查询内置结算周期验证人初始化blockChain的curVerifier属性
          */
        if(maxBlockNumber==null){
            CandidateService.InitParam initParam = candidateService.getInitParam();
            // 更新节点名称映射缓存
            initParam.getStakings().forEach(staking -> nodeNameMap.put(staking.getNodeId(),staking.getStakingName()));
            // 把节点放入待入库暂存
            initParam.getNodes().forEach(node->stageData.getStakingStage().insertNode(node));
            // 把质押放入待入库暂存
            initParam.getStakings().forEach(staking->stageData.getStakingStage().insertStaking(staking));
            // 导出结果
            BlockChainStage bcr = blockChain.exportResult();
            // 批量入库结果
            dbService.batchSave(Collections.emptyList(),bcr);
            blockChain.commitResult();
            // 初始化节点缓存
            nodeCache.init(initParam.getNodes(),initParam.getStakings(),Collections.emptyList(),Collections.emptyList());
            nodeCache.sweep();
        }
    }

    /**
     * 启动收集循环
     * @throws Exception
     */
    public void start() throws Exception {
        while (true) if (!collect()) break;
    }

    /**
     * 收集区块
     * @return
     * @throws Exception
     */
    public boolean collect() throws Exception {
        // 从(已采最高区块号+1)开始构造连续的指定数量的待采区块号列表
        Set<BigInteger> blockNumbers = new HashSet<>();
        // 当前链上最新区块号
        BigInteger curChainBlockNumber=blockService.getLatestNumber();
        long blockNumber=commitBlockNumber+1;
        while (blockNumber<=(commitBlockNumber+collectBatchSize)) {
            // 如果块号>当前链上块号,则不再累加
            if(blockNumber>curChainBlockNumber.longValue()) break;
            blockNumbers.add(BigInteger.valueOf(blockNumber));
            blockNumber++;
        }
        if (!blockNumbers.isEmpty()) {
            // 并行采块 ξξξξξξξξξξξξξξξξξξξξξξξξξξξ
            // 采集前先重置结果容器
            CollectResult.reset();
            // 开始并行采集
            List<CustomBlock> blocks = blockService.collect(blockNumbers);
            if (!blocks.isEmpty()){
                // 并行分析 ξξξξξξξξξξξξξξξξξξξξξξξξξξξ
                transactionService.analyze(blocks);
                // 调用BlockChain实例, 串行分析每个区块，获取质押、提案相关业务数据
                for (CustomBlock block:blocks) blockChain.execute(block);
                BlockChainStage bizData = blockChain.exportResult();
                try {
                    // 入库失败，立即停止，防止采集后续更高的区块号，导致数据错乱
                    dbService.batchSave(blocks, bizData);
                } catch (BusinessException e) {
                    logger.error("数据入库失败:",e);
                    return false;
                }
                // 记录已采入库最高区块号
                commitBlockNumber = blocks.get(blocks.size()-1).getNumber();
                TimeUnit.SECONDS.sleep(1);
            }
        } else {
            logger.info("当前链最高块({}),等待下一批块...",curChainBlockNumber);
            TimeUnit.SECONDS.sleep(1);
        }
        return true;
    }
}
