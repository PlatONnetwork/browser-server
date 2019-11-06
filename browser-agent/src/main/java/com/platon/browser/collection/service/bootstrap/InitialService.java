package com.platon.browser.collection.service.bootstrap;

import com.platon.browser.collection.exception.InitialException;
import com.platon.browser.collection.service.epoch.EpochRetryService;
import com.platon.browser.common.collection.dto.CollectionNetworkStat;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.dao.mapper.NetworkStatMapper;
import com.platon.browser.persistence.dao.mapper.StakeBusinessMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.util.List;

/**
 * @description: 启动初始化服务
 * @author: chendongming@juzix.net
 * @create: 2019-11-06 10:10:30
 **/
@Slf4j
public class InitialService {
    private static final InitialResult initialResult = new InitialResult();

    @Autowired
    private NetworkStatMapper networkStatMapper;
    @Autowired
    private StakeBusinessMapper stakeBusinessMapper;
    @Autowired
    private EpochRetryService epochRetryService;

    public InitialResult init() throws Exception {
        // 检查数据库network_stat表,如果没有记录则添加一条,并从链上查询最新内置验证人节点入库至staking表和node表

        List<NetworkStat> networkStatList = networkStatMapper.selectByExample(null);
        if(networkStatList.isEmpty()){
            epochRetryService.issueChange(BigInteger.ZERO);
            // 创建新的统计记录
            NetworkStat networkStat = new CollectionNetworkStat();
            networkStat.setCurNumber(BigInteger.ONE.longValue());


        }

        if(networkStatList.size()>1) throw new InitialException("启动自检出错:network_stat表存在多条网络统计状态数据!");
        NetworkStat networkStat = networkStatList.get(0);
        initialResult.setCollectedBlockNumber(networkStat.getCurNumber());
        return initialResult;
    }
}
