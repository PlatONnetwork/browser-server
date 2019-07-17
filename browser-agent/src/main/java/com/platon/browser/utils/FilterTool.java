package com.platon.browser.utils;

import com.platon.browser.dao.entity.NodeRanking;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Hex;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
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
            BigDecimal result = BigDecimal.valueOf(25000000L).multiply(rate).divide(blockSumOnYear, 0, BigDecimal.ROUND_DOWN);
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

    public static String voteHashAnalysis(String exetraDate){
        if(StringUtils.isNotBlank(exetraDate)){
            byte[] exetraByte = Hex.decode(exetraDate.replace("0x",""));
            byte[] voteHashdByte = new byte[]{};
            voteHashdByte = Arrays.copyOfRange(exetraByte,97,exetraByte.length);
            StringBuilder stringBuilder = new StringBuilder("");
            if (voteHashdByte == null || voteHashdByte.length <= 0) {
                return " ";
            }
            for (int i = 0; i < voteHashdByte.length; i++) {
                int v = voteHashdByte[i] & 0xFF;
                String hv = Integer.toHexString(v);
                if (hv.length() < 2) {
                    stringBuilder.append(0);
                }
                stringBuilder.append(hv);
            }
            return "0x" + stringBuilder.toString();
        }
        return " ";
    }

    public static void main ( String[] args ) {
        BigDecimal height = new BigDecimal("3");
        BigDecimal wheel = height.divide(new BigDecimal("2"), 0, BigDecimal.ROUND_DOWN);
        System.out.println(wheel);
    }
}