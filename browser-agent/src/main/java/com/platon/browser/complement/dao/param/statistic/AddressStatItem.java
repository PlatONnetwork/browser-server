package com.platon.browser.complement.dao.param.statistic;

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
	 * 地址类型 1:账号 2:合约 3:内置合约
	 */
    private Integer type;

	/**
	 * 交易总数
	 */
    private Integer txQty;

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
	 * 已领取委托奖励
	 */
	private BigDecimal haveReward;
}
