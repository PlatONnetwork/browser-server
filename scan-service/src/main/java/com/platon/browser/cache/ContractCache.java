package com.platon.browser.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.math.BigInteger;

public class ContractCache {
    /**
     * 缓存销毁的合约
     * key: contract address
     * value: destroyed block number，在进程本次运行中，发现此合约被销毁的块高（不一定就是合约销毁的块高）
     */
    public static Cache<String, BigInteger> destroyedContractCache = Caffeine.newBuilder()
            .maximumSize(5000)
            .build();

    /**
     * 缓存销毁的合约
     * key: contract address
     * value: contract bin code
     */
    public static Cache<String, String> contractBinCodeCache = Caffeine.newBuilder()
            .maximumSize(5000)
            .build();
}
