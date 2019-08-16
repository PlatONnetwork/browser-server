package com.platon.browser.service;

import com.platon.browser.dao.entity.Address;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dto.BlockBean;
import com.platon.browser.engine.BlockChainResult;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * User: dongqile
 * Date: 2019/8/14
 * Time: 21:46
 */
@Component
public class StatisticsService {

    public void addressInfoUpdate( List <BlockBean> basicData, BlockChainResult bizData){

        //质押新增列表插入map
        Map <String,Staking> addStakingMap = new HashMap <>();
        Set<Staking> addStakingSet = bizData.getStakingExecuteResult().getAddStakings();
        addStakingSet.forEach(staking -> {
            addStakingMap.put(staking.getStakingAddr(),staking);
        });

        //质押更新列表插入map
        Set<Staking> updateStakingSet = bizData.getStakingExecuteResult().getUpdateStakings();
        Map<String,Staking> updateStakingMap = new HashMap <>();
        updateStakingSet.forEach(staking -> {
            updateStakingMap.put(staking.getStakingAddr(),staking);
        });

        Set <Address> addressSet = bizData.getAddressExecuteResult().getAddAddress();


        Set <Address> updateAddressSet = bizData.getAddressExecuteResult().getUpdateAddress();

    }
    public void statisticsUpdate(List<BlockBean> basicData, BlockChainResult bizData){}
}