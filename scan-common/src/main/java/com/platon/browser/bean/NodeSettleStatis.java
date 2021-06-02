package com.platon.browser.bean;

import cn.hutool.core.collection.BoundedPriorityQueue;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.stream.Collectors;

/**
 * 节点结算周期的出块统计
 *
 * @date 2021/5/25
 */
@Data
@Slf4j
public class NodeSettleStatis {

    /**
     * 节点id
     */
    private String nodeId;

    /**
     * 已统计的块高
     */
    private Long blockNum = new Long("0");

    /**
     * 创建有界优先队列，存放节点8个结算周期(最近7个结算周期(不一定是连续的)+当前的结算周期)的出块统计信息
     */
    BoundedPriorityQueue<NodeSettleStatisBase> nodeSettleStatisQueue = new BoundedPriorityQueue<NodeSettleStatisBase>(CommonConstant.BLOCK_RATE_SETTLE_EPOCH_NUM + 1, (v1, v2) -> {
        return v2.getSettleEpochRound().compareTo(v1.getSettleEpochRound());
    });

    /**
     * json转bean
     *
     * @param json json字符串
     * @return com.platon.browser.bean.NodeSettleStatis
     * @date 2021/6/1
     */
    public static NodeSettleStatis jsonToBean(String json) {
        NodeSettleStatis nodeSettleStatis = new NodeSettleStatis();
        try {
            if (StrUtil.isNotEmpty(json) && JSONUtil.isJson(json)) {
                JSONObject jsonObject = JSONUtil.parseObj(json);
                String nodeId = jsonObject.getStr("nodeId");
                Long blockNum = jsonObject.getLong("blockNum", 0L);
                JSONArray jsonArray = jsonObject.getJSONArray("nodeSettleStatisQueue");
                List<NodeSettleStatisBase> list = jsonArray.toList(NodeSettleStatisBase.class);
                nodeSettleStatis.setNodeId(nodeId);
                nodeSettleStatis.setBlockNum(blockNum);
                nodeSettleStatis.getNodeSettleStatisQueue().addAll(list);
                return nodeSettleStatis;
            } else {
                return nodeSettleStatis;
            }
        } catch (Exception e) {
            log.error("节点结算周期的出块统计json初始化异常", e);
            return nodeSettleStatis;
        }
    }

    /**
     * 计算24小时出块率
     * 出块率=最近7个结算周期该节点出过的所有的块数量/该节点应当出块的数量
     * 该节点应当出块的数量=该节点过去7个结算周期内（不包含当前结算周期）当选出块节点的次数*10
     *
     * @param curSettleEpochRound 当前结算周期轮数
     * @return java.math.BigDecimal
     * @date 2021/6/1
     */
    public String computeGenBlocksRate(BigInteger curSettleEpochRound) {
        try {
            // 8个结算周期不一定是连续的
            if (this.getNodeSettleStatisQueue().size() > 0) {
                // 已按结算周期轮数排序，index[0]是最大的
                List<NodeSettleStatisBase> list = this.getNodeSettleStatisQueue().toList();
                // 过滤数据，与当前结算周期轮数的差值大于0且小于等于7的，即是最近7个结算周期
                List<NodeSettleStatisBase> last = list.stream().filter(v -> {
                    BigInteger difference = curSettleEpochRound.subtract(v.getSettleEpochRound());
                    return difference.compareTo(BigInteger.ZERO) > 0 && difference.compareTo(BigInteger.valueOf(CommonConstant.BLOCK_RATE_SETTLE_EPOCH_NUM)) <= 0;
                }).collect(Collectors.toList());
                if (CollUtil.isNotEmpty(last)) {
                    LongSummaryStatistics blockNumGrandTotal = last.stream().collect(Collectors.summarizingLong(v -> v.getBlockNumGrandTotal().longValue()));
                    LongSummaryStatistics blockNumElected = last.stream().collect(Collectors.summarizingLong(v -> v.getBlockNumElected().longValue()));
                    BigDecimal molecular = BigDecimal.valueOf(blockNumGrandTotal.getSum());
                    BigDecimal denominator = BigDecimal.valueOf(blockNumElected.getSum()).multiply(BigDecimal.TEN);
                    // 分母为0，直接返回
                    if (denominator.compareTo(BigDecimal.valueOf(0)) == 0) {
                        return "0%";
                    }
                    BigDecimal percent = molecular
                            .divide(denominator, 6, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(100))
                            .setScale(6, RoundingMode.HALF_UP)
                            .stripTrailingZeros();
                    log.info("节点[{}]在[{}]结算周期计算24小时出块率为[{}%]=[{}]/([{}]*10)", this.getNodeId(), curSettleEpochRound, percent.toPlainString(), molecular, blockNumElected.getSum());
                    return percent.toPlainString() + "%";
                } else {
                    return "0%";
                }
            } else {
                return "0%";
            }
        } catch (Exception e) {
            log.error(StrUtil.format("节点[{}]计算24小时出块率异常", this.getNodeId()), e);
            return "0%";
        }
    }


}
