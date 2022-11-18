package com.platon.browser.bean;

import cn.hutool.core.collection.BoundedPriorityQueue;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;

@Data
@Slf4j
public class NodeApr {

    /**
     * 节点id
     */
    private String nodeId;

    /**
     * 创建有界优先队列，存放节点9个结算周期的委托年化收益率
     */
    BoundedPriorityQueue<NodeAprBase> nodeAprQueue = new BoundedPriorityQueue<>(CommonConstant.BLOCK_APR_EPOCH_NUM + 1,
                                                                                (v1, v2) -> v2.getEpochNum()
                                                                                              .compareTo(v1.getEpochNum()));

    /**
     * 构建
     *
     * @param settingEpoch:   结算周期
     * @param nodeId:         节点id
     * @param annualizedRate: 委托年化率
     * @param json:           json
     * @return:
     * @date: 2022/11/16
     */
    public static NodeApr build(Integer settingEpoch, String nodeId, BigDecimal annualizedRate, String json) {
        NodeApr nodeApr = new NodeApr();
        nodeApr.setNodeId(nodeId);
        NodeAprBase nodeAprBase = new NodeAprBase();
        nodeAprBase.setEpochNum(settingEpoch);
        nodeAprBase.setDeleAnnualizedRate(annualizedRate.toPlainString());
        if (StrUtil.isNotEmpty(json) && JSONUtil.isJson(json)) {
            JSONObject jsonObject = JSONUtil.parseObj(json);
            JSONArray jsonArray = jsonObject.getJSONArray("nodeAprQueue");
            List<NodeAprBase> list = jsonArray.toList(NodeAprBase.class);
            nodeApr.getNodeAprQueue().addAll(list);
        }
        nodeApr.getNodeAprQueue().offer(nodeAprBase);
        return nodeApr;
    }

    /**
     * 获取前一天的委托年化率
     *
     * @param :
     * @return:
     * @date: 2022/11/16
     */
    public static String getPreDeleAnnualizedRate(String json) {
        try {
            if (StrUtil.isNotEmpty(json) && JSONUtil.isJson(json)) {
                JSONObject jsonObject = JSONUtil.parseObj(json);
                JSONArray jsonArray = jsonObject.getJSONArray("nodeAprQueue");
                List<NodeAprBase> list = jsonArray.toList(NodeAprBase.class);
                return list.get(0).getDeleAnnualizedRate();
            } else {
                return "0";
            }
        } catch (Exception e) {
            log.error("获取前一天的委托年化率异常", e);
            return "0";
        }
    }

}
