package com.platon.browser.utils;

import com.platon.browser.dao.entity.NodeRanking;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * User: dongqile
 * Date: 2019/1/22
 * Time: 15:48
 */
public class FilterTool {

    public static List<NodeRanking> currentBlockOwner ( List <NodeRanking> list, BigInteger publicKey ) throws Exception {
        for (NodeRanking nodeRanking : list) {
            if (publicKey.equals(new BigInteger(nodeRanking.getNodeId().replace("0x", ""), 16))) {
                long count = nodeRanking.getBlockCount();
                count = count + 1;
                nodeRanking.setBlockCount(count);
                nodeRanking.getRewardRatio();
            }
        }
        return list;
    }

    public static  List <NodeRanking> dateStatistics ( List <NodeRanking> list, BigInteger publicKey, String blockReward ) throws Exception {
        for (NodeRanking nodeRanking : list) {
            if (publicKey.equals(new BigInteger(nodeRanking.getNodeId().replace("0x", ""), 16))) {
                BigDecimal sum = new BigDecimal(nodeRanking.getBlockReward());
                BigDecimal reward = new BigDecimal(blockReward);
                sum = sum.add(reward);
                nodeRanking.setBlockReward(sum.toString());
                BigDecimal rate = new BigDecimal(String.valueOf(1 - nodeRanking.getRewardRatio()));
                nodeRanking.setRewardAmount(sum.multiply(rate).multiply(BigDecimal.valueOf(nodeRanking.getBlockCount())).toString());
                BigDecimal fee = new BigDecimal(String.valueOf(nodeRanking.getRewardRatio()));
                nodeRanking.setProfitAmount(sum.multiply(fee).multiply(BigDecimal.valueOf(nodeRanking.getBlockCount())).toString());
            }
        }
        return list;
    }

    public static  String getBlockReward ( String number ) {
        //ATP trasnfrom ADP
        BigDecimal rate = BigDecimal.valueOf(10L).pow(18);
        BigDecimal height = new BigDecimal(number);
        BigDecimal blockSumOnYear = BigDecimal.valueOf(24).multiply(BigDecimal.valueOf(3600)).multiply(BigDecimal.valueOf(365));
        BigDecimal definiteValue = new BigDecimal("1.025");
        BigDecimal base = BigDecimal.valueOf(100000000L);
        BigDecimal wheel = height.divide(blockSumOnYear, 0, BigDecimal.ROUND_HALF_DOWN);
        if (wheel.intValue() == 0) {
            //one period block
            BigDecimal result = BigDecimal.valueOf(25000000L).multiply(rate).divide(blockSumOnYear, 0, BigDecimal.ROUND_HALF_DOWN);
            return result.setScale(0).toString();
        }
        BigDecimal thisRound = base.multiply(rate).multiply(definiteValue.pow(wheel.intValue()));
        BigDecimal previousRound = base.multiply(rate).multiply(definiteValue.pow(wheel.subtract(BigDecimal.valueOf(1L)).intValue()));
        BigDecimal result = thisRound.subtract(previousRound).divide(blockSumOnYear);
        return result.setScale(0).toString();
    }

    public static String valueConversion(BigInteger value){
        BigDecimal valueDiec = new BigDecimal(value.toString());
        BigDecimal conversionCoin = valueDiec.divide(new BigDecimal("1000000000000000000"));
        return  conversionCoin.toString();
    }
}