package com.platon.browser.utils;

import com.platon.browser.exception.BlockNumberException;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/26 10:37
 * @Description: 周期计算工具
 */
public class EpochUtil {
    /**
     * 取周期，向上取整
     * @param blockNumber 当前区块号
     * @param blockCountPerEpoch 每个周期的区块数量
     * @return
     */
    public static final Long getEpoch(Long blockNumber,Long blockCountPerEpoch) throws BlockNumberException {
        if(blockNumber<=0||blockCountPerEpoch<=0) throw new BlockNumberException("区块号或周期区块数必须大于0");
        BigDecimal epoch = BigDecimal.valueOf(blockNumber).divide(BigDecimal.valueOf(blockCountPerEpoch),0, RoundingMode.CEILING);
        return epoch.longValue();
    }
    /**
     * 取上一周期最后一个区块号
     * @param blockNumber 当前区块号
     * @param blockCountPerEpoch 每个周期的区块数量
     * @return
     */
    public static final Long getPreEpochLastBlockNumber(Long blockNumber,Long blockCountPerEpoch) throws BlockNumberException {
        Long curEpoch = getEpoch(blockNumber,blockCountPerEpoch);
        if(curEpoch<=0) throw new BlockNumberException("当前周期为("+curEpoch+"),没有上一周期");
        Long prevEpoch = curEpoch-1;
        Long prevEpochLastBlockNumber = prevEpoch*blockCountPerEpoch;
        return prevEpochLastBlockNumber;
    }

    /**
     * 取上一周期最后一个区块号
     * @param blockNumber 当前区块号
     * @param blockCountPerEpoch 每个周期的区块数量
     * @return
     */
    public static final Long getCurEpochLastBlockNumber(Long blockNumber,Long blockCountPerEpoch) throws BlockNumberException {
        Long curEpoch = getEpoch(blockNumber,blockCountPerEpoch);
        Long curEpochLastBlockNumber = curEpoch*blockCountPerEpoch;
        return curEpochLastBlockNumber;
    }
}
