package com.platon.browser.mapper.ParamBuild;

import com.platon.browser.persistence.dao.param.CreateStakingParam;
import org.web3j.platon.bean.StakingParam;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * @Auther: dongqile
 * @Date: 2019/11/1
 * @Description:
 */
public class ParamBuildTest {

    public CreateStakingParam crateStakingBuild(){
        CreateStakingParam createStakingParam = new CreateStakingParam();
        createStakingParam.setNodeId("0x20a090d94bc5015c9339a46e9ca5d80057a5ef25cc14e71cef67b502ec32949253f046821e80dfb6ff666ef0e0badf58fdb719368c38393f7c40ebcf18d8ed18");
        createStakingParam.setStakingHes(new BigDecimal("5000"));
        createStakingParam.setNodeName("testNode01");
        createStakingParam.setExternalId("externalId01");
        createStakingParam.setBenefitAddr("0xff48d9712d8a55bf603dab28f4645b6985696a61");
        createStakingParam.setProgramVersion("1794");
        createStakingParam.setBigVersion("1700");
        createStakingParam.setWebSite("web_site01");
        createStakingParam.setDetails("details01");
        createStakingParam.setIsInit(1);
        createStakingParam.setStakingBlockNum(new BigInteger("200"));
        createStakingParam.setStakingTxIndex(0);
        createStakingParam.setStakingAddr("0xb58c7fd25437e2fcf038b948963ffb80276bd44d");
        createStakingParam.setJoinTime(new Date(System.currentTimeMillis()));
        createStakingParam.setTxHash("0xaa85c7e85542ac8e8d2428c618130d02723138437d105d06d405f9e735469be7");
        return createStakingParam;
    }
}