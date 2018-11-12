package com.platon.browser.message;

import com.alibaba.fastjson.JSON;
import com.maxmind.geoip.Location;
import com.platon.browser.common.dto.agent.BlockDto;
import com.platon.browser.common.dto.agent.NodeDto;
import com.platon.browser.common.dto.agent.TransactionDto;
import com.platon.browser.common.dto.mq.Message;
import com.platon.browser.common.enums.MqMessageTypeEnum;
import com.platon.browser.config.ChainsConfig;
import com.platon.browser.dto.IndexInfo;
import com.platon.browser.dto.StatisticInfo;
import com.platon.browser.dto.StatisticItem;
import com.platon.browser.dto.block.BlockInfo;
import com.platon.browser.dto.node.NodeInfo;
import com.platon.browser.dto.transaction.TransactionInfo;
import com.platon.browser.service.CacheService;
import com.platon.browser.util.GeoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SubscribePushService {

    private final Logger logger = LoggerFactory.getLogger(SubscribePushService.class);

    @Autowired
    private CacheService cacheService;

    @Autowired
    private ChainsConfig chainsConfig;

    @RabbitListener(queues = "#{platonQueue.name}")
    public void receive(String msg) {
        Message message = JSON.parseObject(msg,Message.class);
        String chainId = message.getChainId();
        if(!chainsConfig.isValid(chainId)){
            logger.error("链ID={}在本系统中未作配置！",message.getChainId());
            return;
        }

        switch (MqMessageTypeEnum.valueOf(message.getType().toUpperCase())){
            case NODE:
                logger.debug("更新增量节点缓存: {}",msg);
                NodeDto nodeDto = JSON.parseObject(message.getStruct(), NodeDto.class);
                NodeInfo nodeInfo = new NodeInfo();
                BeanUtils.copyProperties(nodeDto,nodeInfo);
                Location location = GeoUtil.getLocation(nodeDto.getIp());
                nodeInfo.setLatitude(location.latitude);
                nodeInfo.setLongitude(location.longitude);
                List<NodeInfo> nodeInfoList = new ArrayList<>();
                nodeInfoList.add(nodeInfo);
                cacheService.updateNodeCache(nodeInfoList,false,chainId);
                break;
            case BLOCK:
                logger.debug("更新增量区块缓存: {}",msg);
                BlockDto blockDto = JSON.parseObject(message.getStruct(),BlockDto.class);
                BlockInfo blockInfo = new BlockInfo();
                BeanUtils.copyProperties(blockDto,blockInfo);
                blockInfo.setServerTime(System.currentTimeMillis());
                blockInfo.setNode(blockDto.getMiner());
                blockInfo.setTimestamp(blockDto.getTimestamp());
                blockInfo.setHeight(blockDto.getNumber());
                blockInfo.setBlockReward(blockDto.getBlockReward());
                blockInfo.setTransaction(blockDto.getTransaction().size());
                List<BlockInfo> blockInfoList = new ArrayList<>();
                blockInfoList.add(blockInfo);
                cacheService.updateBlockCache(blockInfoList,chainId);

                logger.debug("整体更新指标缓存: {}",msg);
                IndexInfo indexInfo = new IndexInfo();
                indexInfo.setCurrentHeight(blockInfo.getHeight());
                indexInfo.setCurrentTransaction(blockDto.getTransaction().size());
                cacheService.updateIndexCache(indexInfo,false, chainId);

                logger.debug("更新交易缓存: {}",msg);
                List<TransactionDto> transactionDtos = blockDto.getTransaction();
                List<TransactionInfo> transactionInfos = new ArrayList<>();
                if(transactionDtos.size()>0){
                    transactionDtos.forEach(transactionDto -> {
                        TransactionInfo bean = new TransactionInfo();
                        BeanUtils.copyProperties(transactionDto,bean);
                        bean.setTxHash(transactionDto.getHash());
                        bean.setTimestamp(transactionDto.getTimestamp());
                        bean.setBlockHeight(transactionDto.getBlockNumber().longValue());
                        bean.setFrom(transactionDto.getFrom());
                        bean.setTo(transactionDto.getTo());
                        bean.setTransactionIndex(transactionDto.getTransactionIndex().intValue());
                        bean.setValue(transactionDto.getValue());
                        transactionInfos.add(bean);
                    });
                    cacheService.updateTransactionCache(transactionInfos,chainId);
                }

                logger.debug("整体更新统计缓存: {}",msg);
                StatisticInfo statisticInfo = new StatisticInfo();
                statisticInfo.setHighestBlockNumber(blockInfo.getHeight());
                statisticInfo.setBlockCount(1l);
                statisticInfo.setDayTransaction(Long.valueOf(transactionInfos.size()));
                List<StatisticItem> statisticItems = new ArrayList<>();
                StatisticItem statisticItem = new StatisticItem();
                statisticItem.setHeight(blockInfo.getHeight());
                statisticItem.setTime(blockInfo.getTimestamp());
                statisticItem.setTransaction(Long.valueOf(transactionInfos.size()));
                statisticItems.add(statisticItem);
                statisticInfo.setBlockStatisticList(statisticItems);
                cacheService.updateStatisticCache(statisticInfo,false,chainId);
                break;
        }
    }
}
