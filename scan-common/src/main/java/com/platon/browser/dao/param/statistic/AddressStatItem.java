package com.platon.browser.dao.param.statistic;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Builder
@Accessors(chain = true)
public class AddressStatItem {
   	/**
	 * 地址
	 */
	private String address;

	/**
	 * 地址类型 1账号,2内置合约 ,3EVM合约,4WASM合约
	 */
    private Integer type;

	/**
	 * 交易总数
	 */
    private Integer txQty;

    /**
     * erc20交易数
     */
    private Integer erc20TxQty;
	/**
	 * erc721交易数
	 */
	private Integer erc721TxQty;

	/**
	 * 转账交易总数
	 */
    private Integer transferQty;

	/**
	 * 委托交易总数
	 */
    private Integer delegateQty;

	/**
	 * 质押交易总数
	 */
    private Integer stakingQty;

	/**
	 * 治理交易总数
	 */
    private Integer proposalQty;

	/**
	 * 合约名称
	 */
    private String contractName;

	/**
	 * 合约创建者地址
	 */
    private String contractCreate;

	/**
	 * 创建合约的交易Hash
	 */
    private String contractCreatehash;
	/**
	 * 合约的销毁hash
	 */
	private String contractDestroyHash;
	/**
	 * 合约代码数据
	 */
	private String contractBin;

	/**
	 * 已领取委托奖励
	 */
	private BigDecimal haveReward;
}
