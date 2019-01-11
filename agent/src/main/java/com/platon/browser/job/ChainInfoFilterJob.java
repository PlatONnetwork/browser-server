package com.platon.browser.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.github.pagehelper.PageHelper;
import com.platon.browser.client.Web3jClient;
import com.platon.browser.common.base.AppException;
import com.platon.browser.common.enums.ErrorCodeEnum;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.BlockExample;
import com.platon.browser.dao.mapper.BlockMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StopWatch;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthBlockNumber;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * User: dongqile
 * Date: 2018/10/24
 * Time: 17:28
 */
public class ChainInfoFilterJob extends AbstractTaskJob {

    /**
     *
     */

    private static Logger log = LoggerFactory.getLogger(ChainInfoFilterJob.class);


    @Autowired
    private BlockMapper blockMapper;

    private static Long maxNubmer = 0L;

    @Value("${chain.id}")
    private String chainId;

    private static boolean isFirstRun = true;

    @PostConstruct
    public void init () {
        BlockExample condition = new BlockExample();
        condition.createCriteria().andChainIdEqualTo(chainId);
        condition.setOrderByClause("timestamp desc");
        PageHelper.startPage(1, 1);
        List <Block> blocks = blockMapper.selectByExample(condition);
        // 1、首先从数据库查询当前链的最高块号，作为采集起始块号
        // 2、如果查询不到则从0开始
        if (blocks.size() == 0) {
            maxNubmer = 0L;
        } else {
            maxNubmer = blocks.get(0).getNumber();
        }



    }

    @Override
    protected void doJob ( ShardingContext shardingContext ) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            if (isFirstRun) {
                TimeUnit.SECONDS.sleep(30l);
                isFirstRun = false;
            }
            EthBlockNumber ethBlockNumber = null;
            Web3j web3j = Web3jClient.getWeb3jClient();
            try {
                ethBlockNumber = web3j.ethBlockNumber().send();
            } catch (Exception e) {
                log.error("Get blockNumber exception...", e);
                throw new AppException(ErrorCodeEnum.BLOCKCHAIN_ERROR);
            }
            String blockNumber = ethBlockNumber.getBlockNumber().toString();

            for (int i = maxNubmer.intValue() + 1; i <= Integer.parseInt(blockNumber); i++) {
                //select blockinfo from PlatON
                DefaultBlockParameter defaultBlockParameter = new DefaultBlockParameterNumber(new BigInteger(String.valueOf(i)));
                EthBlock ethBlock = web3j.ethGetBlockByNumber(defaultBlockParameter, true).send();

                try {
                } catch (Exception e) {
                    //BlockFilter(ethBlock,);
                    log.error("Block Filter exception", e);
                    throw new AppException(ErrorCodeEnum.BLOCKCHAIN_ERROR);
                }

                try {
                } catch (Exception e) {
                    log.error("Transaction Filter exception", e);
                    throw new AppException(ErrorCodeEnum.TX_ERROR);
                }

                try {
                } catch (Exception e) {
                    log.error("Node Filter exception", e);
                    throw new AppException(ErrorCodeEnum.NODE_ERROR);
                }

                try {
                } catch (Exception e) {
                    log.error("PendingTx Filter exception", e);
                    throw new AppException(ErrorCodeEnum.PENDINGTX_ERROR);
                }

                try {
                } catch (Exception e) {
                    log.error("Stomp Filter exception", e);
                    throw new AppException(ErrorCodeEnum.STOMP_ERROR);
                }

            }
            maxNubmer = Long.valueOf(blockNumber);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            stopWatch.stop();
            log.debug("BlockSynchronizeJob-->{}", stopWatch.shortSummary());
        }

    }

}