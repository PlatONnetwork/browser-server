package com.platon.browser.persistence.dao.mapper;

import com.platon.browser.AgentApplication;
import com.platon.browser.TestBase;
import com.platon.browser.complement.dao.mapper.StatisticBusinessMapper;
import com.platon.browser.complement.dao.param.statistic.AddressStatChange;
import com.platon.browser.complement.dao.param.statistic.NetworkStatChange;
import com.platon.browser.dao.entity.NetworkStat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Auther: dongqile
 * @Date: 2019/10/31
 * @Description: 统计数据测试类
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgentApplication.class, value = "spring.profiles.active=test")
@SpringBootApplication
public class StatisticBusinessTest extends TestBase {


    @Autowired
    private StatisticBusinessMapper statisticBusinessMapper;


    /**
     * 地址数据统计
     */
    @Test
    public void addressChangeMapper(){
        AddressStatChange addressStatChange = addressStatChangeParam();
        statisticBusinessMapper.addressChange(addressStatChange);
    }


     /**
     *  其他数据统计
     */
//    @Test
//    public void netWorkChangeMapper(){
//        NetworkStat networkStatChange = networkStatChangeParam();
//        statisticBusinessMapper.networkChange(networkStatChange);
//    }


}