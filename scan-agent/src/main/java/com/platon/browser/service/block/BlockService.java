package com.platon.browser.service.block;

import com.platon.browser.analyzer.epoch.OnConsensusAnalyzer;
import com.platon.browser.analyzer.epoch.OnElectionAnalyzer;
import com.platon.browser.analyzer.epoch.OnNewBlockAnalyzer;
import com.platon.browser.analyzer.epoch.OnSettleAnalyzer;
import com.platon.browser.bean.CollectionEvent;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.protocol.core.methods.response.PlatonBlock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * @Auther: Chendongming
 * @Date: 2019/10/30 10:14
 * @Description: 区块服务
 */
@Slf4j
@Service
public class BlockService {

    @Resource
    private BlockRetryService retryService;

    @Resource
    private BlockChainConfig chainConfig;

    @Resource
    private OnNewBlockAnalyzer onNewBlockAnalyzer;

    @Resource
    private OnElectionAnalyzer onElectionAnalyzer;

    @Resource
    private OnConsensusAnalyzer onConsensusAnalyzer;

    @Resource
    private OnSettleAnalyzer onSettleAnalyzer;

    /**
     * 异步获取区块
     */
    public CompletableFuture<PlatonBlock> getBlockAsync(Long blockNumber) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return retryService.getBlock(blockNumber);
            } catch (Exception e) {
                log.error("重试采集区块({})异常:", blockNumber, e);
            }
            return null;
        });
    }

    public void checkBlockNumber(Long blockNumber) {
        try {
            retryService.checkBlockNumber(blockNumber);
        } catch (Exception e) {
            log.error("重试同步链出错:", e);
        }
    }


    /**
     * 解析区块, 构造业务入库参数信息
     *
     * @return
     */
    public List<NodeOpt> analyze(CollectionEvent event) throws NoSuchBeanException {
        long startTime = System.currentTimeMillis();

        List<NodeOpt> nodeOptList = new ArrayList<>();
        Block block = event.getBlock();

        if (block.getNum() == 0)
            return nodeOptList;

        // 新选举周期事件
        if ((block.getNum() + chainConfig.getElectionBackwardBlockCount().longValue()) % chainConfig.getConsensusPeriodBlockCount().longValue() == 0
                && event.getEpochMessage().getConsensusEpochRound().longValue() > 1) {
            // 共识轮数等于大于1的时候才进来
            log.info("在块高[{}]选举验证人", block.getNum());
            List<NodeOpt> nodeOpt = onElectionAnalyzer.analyze(event, block);
            nodeOptList.addAll(nodeOpt);
        }

        // 新共识周期事件
        if ((block.getNum() - 1) % chainConfig.getConsensusPeriodBlockCount().longValue() == 0) {
            log.info("在块高[{}]切换共识周期,当前所处共识周期轮数为[{}]", block.getNum(), event.getEpochMessage().getConsensusEpochRound());
            Optional<List<NodeOpt>> nodeOpt = onConsensusAnalyzer.analyze(event, block);
            nodeOpt.ifPresent(nodeOptList::addAll);
        }

        // 新结算周期事件
        if ((block.getNum() - 1) % chainConfig.getSettlePeriodBlockCount().longValue() == 0) {
            log.info("在块高[{}]切换结算周期,当前所处结算周期轮数为[{}]", block.getNum(), event.getEpochMessage().getSettleEpochRound());
            List<NodeOpt> nodeOpt = onSettleAnalyzer.analyze(event, block);
            nodeOptList.addAll(nodeOpt);
        }

        // 新区块事件
        onNewBlockAnalyzer.analyze(event, block);


        log.debug("处理耗时:{} ms", System.currentTimeMillis() - startTime);

        return nodeOptList;
    }

}
