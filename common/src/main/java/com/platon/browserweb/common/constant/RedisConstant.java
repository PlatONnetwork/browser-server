package com.platon.browserweb.common.constant;

/**
 * redis常量
 * @author cxf
 *
 */
public interface RedisConstant {

	/**
	 * 检测重复提交DB
	 */
	public static final String REDIS_DB = "exchange";
	
	/**
	 * 检测系统设置重复提交key
	 */
	public static final String KEY_LOCK_SYSTEM_CONFIG = "lock_system_config";

	/**
	 * 检测添加代理商重复提交key
	 */
	public static final String KEY_LOCK_ADDBROKER_CONFIG = "lock_system_addBroker_config";
	
	/**
	 * 检测提币风控重复提交key
	 */
	public static final String LOCK_WITHDRAW_RISK = "lock_withdraw_risk";
	
	/**
	 * 检测提币费率重复提交key
	 */
	public static final String LOCK_WITHDRAW_FEE = "lock_withdraw_fee";
	
	/**
	 * 检测交易风控重复提交key
	 */
	public static final String LOCK_TRADE_RISK = "lock_trade_risk";
	
	/**
	 * 检测交易费率重复提交key
	 */
	public static final String LOCK_TRADE_FEE = "lock_trade_fee";
	
	/**
	 * 检测风控折扣重复提交key
	 */
	public static final String LOCK_RISK_DISCOUNT = "lock_risk_discount";
	
	/**
	 * 检测费率折扣重复提交key
	 */
	public static final String LOCK_FEE_DISCOUNT = "lock_fee_discount";
	
	/**
	 * 检测充币风控重复提交key
	 */
	public static final String LOCK_DEPOSIT_RISK = "lock_deposit_risk";

	/**
	 * 检测添加黑名单重复提交key
	 */
	public static final String KEY_LOCK_BLACKLIST_CONFIG = "lock_black_list_config";

	/**
	 * 检测代理商配置重复提交key
	 */
	String KEY_LOCK_BROKER_CONFIG = "lock_broker_config";
}
