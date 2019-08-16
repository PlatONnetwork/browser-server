package com.platon.browser.service;

import com.platon.browser.dto.BlockBean;
import com.platon.browser.engine.BlockChainResult;
import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: dongqile
 * Date: 2019/8/14
 * Time: 21:46
 */
@Component
public class StatisticsService {

    public void addressInfoUpdate( List <BlockBean> basicData, BlockChainResult bizData){
        bizData.getAddressExecuteResult().getAddAddress();
        bizData.getAddressExecuteResult().getUpdateAddress();
    }

    public void statisticsUpdate(List<BlockBean> basicData, BlockChainResult bizData){}
}