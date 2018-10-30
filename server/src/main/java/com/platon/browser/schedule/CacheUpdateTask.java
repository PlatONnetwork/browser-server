package com.platon.browser.schedule;

import com.maxmind.geoip.Location;
import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.entity.NodeExample;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.dao.mapper.TransactionMapper;
import com.platon.browser.dto.node.NodeInfo;
import com.platon.browser.service.CacheService;
import com.platon.browser.util.GeoUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 从数据库定时加载最新数据更新缓存
 */
@Component
public class CacheUpdateTask {

    @Autowired
    private NodeMapper nodeMapper;
    @Autowired
    private BlockMapper blockMapper;
    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private CacheService cacheService;

    /**
     * 更新节点信息缓存
     */
    @Scheduled(fixedRate = 10000)
    @Transactional
    void updateNodeInfoList(){
        NodeExample example = new NodeExample();
        List<Node> nodeList = nodeMapper.selectByExample(example);
        List<NodeInfo> nodeInfoList = new ArrayList<>();
        nodeList.forEach(node -> {
            NodeInfo ni = new NodeInfo();
            nodeInfoList.add(ni);
            BeanUtils.copyProperties(node,ni);
            Location location = GeoUtil.getLocation(node.getIp());
            ni.setLongitude(String.valueOf(location.longitude));
            ni.setLatitude(String.valueOf(location.latitude));
        });
        cacheService.updateNodeInfoList(true, nodeInfoList);
    }

    /**
     * 更新指标信息缓存
     */
    @Scheduled(fixedRate = 10000)
    void updateIndexInfo(){

    }

    /**
     * 更新交易统计信息缓存
     */
    @Scheduled(fixedRate = 10000)
    void updateStatisticInfo(){

    }

    /**
     * 更新区块列表信息缓存
     */
    @Scheduled(fixedRate = 10000)
    void updateBlockInfoList(){

    }

    /**
     * 更新交易列表信息缓存
     */
    @Scheduled(fixedRate = 10000)
    void updateTransactionInfoList(){

    }
}
