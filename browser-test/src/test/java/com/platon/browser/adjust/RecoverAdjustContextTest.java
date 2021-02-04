package com.platon.browser.adjust;

import com.alibaba.fastjson.JSON;
import com.platon.browser.dao.entity.Delegation;
import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.v0150.bean.AdjustParam;
import com.platon.browser.v0150.context.DelegateAdjustContext;
import com.platon.browser.v0150.context.StakingAdjustContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
// 根据调账日志恢复调账上下文
@Slf4j
public class RecoverAdjustContextTest {
    public static void main(String[] args) throws IOException {

        String content = FileUtils.readFileToString(new File("E:\\Java\\browser-server\\browser-test\\src\\test\\resources\\staking-delegate-adjust.log"),"utf-8");
        String[] jsonArray = content.split("oooo");

        List<DelegateAdjustContext> delegateAdjustContextList = new ArrayList<>();
        List<StakingAdjustContext> stakingAdjustContextList = new ArrayList<>();
        for (String arr: jsonArray){
            List<Map> context = JSON.parseArray(arr, Map.class);

            if(context.size()==3){
                // 构造委托调账上下文
                StakingAdjustContext sac = new StakingAdjustContext();
                Map map = context.get(0);
                AdjustParam adjustParam = JSON.parseObject(JSON.toJSONString(map),AdjustParam.class);
                sac.setAdjustParam(adjustParam);
                map = context.get(1);
                Node node = JSON.parseObject(JSON.toJSONString(map),Node.class);
                sac.setNode(node);
                map = context.get(2);
                Staking staking = JSON.parseObject(JSON.toJSONString(map),Staking.class);
                sac.setStaking(staking);
                stakingAdjustContextList.add(sac);
            }

            if(context.size()==4){
                // 构造委托调账上下文
                DelegateAdjustContext dac = new DelegateAdjustContext();
                Map map = context.get(0);
                AdjustParam adjustParam = JSON.parseObject(JSON.toJSONString(map),AdjustParam.class);
                dac.setAdjustParam(adjustParam);
                map = context.get(1);
                Node node = JSON.parseObject(JSON.toJSONString(map),Node.class);
                dac.setNode(node);
                map = context.get(2);
                Staking staking = JSON.parseObject(JSON.toJSONString(map),Staking.class);
                dac.setStaking(staking);
                map = context.get(3);
                Delegation delegation = JSON.parseObject(JSON.toJSONString(map),Delegation.class);
                dac.setDelegation(delegation);
                delegateAdjustContextList.add(dac);
            }
        }
        log.error("");
    }
}
