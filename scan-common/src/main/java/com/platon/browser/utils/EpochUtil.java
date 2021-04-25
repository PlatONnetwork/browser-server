package com.platon.browser.utils;

import com.platon.browser.exception.BlockNumberException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/26 10:37
 * @Description: 周期计算工具
 */
public class EpochUtil {
    private EpochUtil(){}
    /**
     * 取周期，向上取整
     * @param blockNumber 当前区块号
     * @param blockCountPerEpoch 每个周期的区块数量
     * @return
     */
    public static BigInteger getEpoch(BigInteger blockNumber, BigInteger blockCountPerEpoch) throws BlockNumberException {
        if(BigInteger.ZERO.compareTo(blockCountPerEpoch)>=0) throw new BlockNumberException("周期区块数必须大于0");
        if(BigInteger.ZERO.compareTo(blockNumber)>0) return BigInteger.ZERO;
        BigDecimal epoch = new BigDecimal(blockNumber).divide(new BigDecimal(blockCountPerEpoch),0, RoundingMode.CEILING);
        return epoch.toBigInteger();
    }
    /**
     * 取上一周期最后一个区块号
     * @param blockNumber 当前区块号
     * @param blockCountPerEpoch 每个周期的区块数量
     * @return
     */
    public static BigInteger getPreEpochLastBlockNumber(BigInteger blockNumber, BigInteger blockCountPerEpoch) throws BlockNumberException {
        BigInteger curEpoch = getEpoch(blockNumber,blockCountPerEpoch);
        if(BigInteger.ZERO.compareTo(curEpoch)>0) throw new BlockNumberException("当前周期为("+curEpoch+"),没有上一周期");
        if(BigInteger.ZERO.compareTo(curEpoch)==0) return curEpoch;
        BigInteger prevEpoch = curEpoch.subtract(BigInteger.ONE);
        return prevEpoch.multiply(blockCountPerEpoch);
    }

    /**
     * 取当前周期最后一个区块号
     * @param blockNumber 当前区块号
     * @param blockCountPerEpoch 每个周期的区块数量
     * @return
     */
    public static BigInteger getCurEpochLastBlockNumber(BigInteger blockNumber, BigInteger blockCountPerEpoch) throws BlockNumberException {
        BigInteger curEpoch = getEpoch(blockNumber,blockCountPerEpoch);
        return curEpoch.multiply(blockCountPerEpoch);
    }
}
