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
import org.checkerframework.checker.units.qual.A;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

// 根据调账日志恢复调账上下文
@Slf4j
public class RecoverAdjustContextTest {
    public static void main(String[] args) throws IOException {
        String path = RecoverAdjustContextTest.class.getResource("/staking-delegate-adjust.log").getPath();
        String content = FileUtils.readFileToString(new File(path),"utf-8");
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

        // 按nodeId+"-"+stakingBlockNum对委托context分组，且保留顺序
        Map<String, List<DelegateAdjustContext>> groupMap = new HashMap<>();
        delegateAdjustContextList.forEach(ctx->{
            String key = ctx.getNode().getNodeId()+"-"+ctx.getNode().getStakingBlockNum();
            List<DelegateAdjustContext> group = groupMap.computeIfAbsent(key, k -> new ArrayList<>());
            group.add(ctx);
        });

        // 打印[ nodeId  stakingBlockNum ]
        List<String> lines = new ArrayList<>();
        lines.add("#节点调账信息");
        lines.add("|节点ID|质押块高|");
        lines.add("|:----|:----|");
        groupMap.keySet().forEach(key-> lines.add("|"+key.replace("-"," | ")+"|"));
        FileUtils.writeLines(
                new File("E:\\Java\\browser-server\\browser-test\\src\\test\\resources\\节点调账(节点-质押块高)信息.md"),
                lines
        );

        // 每组排除最后一条
        //groupMap.forEach((key,group)->group.remove(group.size()-1));

        // 委托调账金额汇总
        Map<String, AdjustSummary> delegateAdjustSummary = new HashMap<>();
        groupMap.forEach((key,group)->{
            AdjustSummary summary = new AdjustSummary();
            group.forEach(ctx->{
                summary.setType(ctx.getAdjustParam().getOptType());
                summary.setRewardSum(summary.getRewardSum().add(ctx.getAdjustParam().getReward()));
                summary.setLockSum(summary.getLockSum().add(ctx.getAdjustParam().getLock()));
                summary.setHesSum(summary.getHesSum().add(ctx.getAdjustParam().getHes()));
            });
            delegateAdjustSummary.put(key,summary);
        });


        log.error("委托调账汇总：{}",JSON.toJSONString(delegateAdjustSummary,true));
    }
}
