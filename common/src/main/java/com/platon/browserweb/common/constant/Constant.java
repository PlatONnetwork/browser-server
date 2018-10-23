package com.platon.browserweb.common.constant;

public interface Constant {

    public static final String REDIS_DB = "canal";
    
    public static final String New_Time_Key = "updatetime";
    
    public static final String VERSION = "version";

    public static final String VERIFICATIONCODE_PREFIX = "ADMIN_";

    /**
     * K线下标 db
     */
    public static final String KLINE_INDEX = "kline_index";
    /**
     * K线 db
     */
    public static final String KLINE = "kline";

    public static final String DOT = ".";

    public static final String SCORE_DB = "score";

    public static final String TRADE_AMOUNT = "trade_amount";

    /**
     * 行情数据（所有基准币的当日行情） db
     */
    public static final String TICKER = "ticker";

    public static final String TICKER_TRADE = "trade";

    public static final String updatetime = "updatetime";

    public static final String REDIS_DB_NAME = "exchange";

    public static final String TRADE_ZONE = "trade_zone";


    public static final String REDIS_DB2 = "broker_control";

    // 提币
    public static final String WITHDRAW = "W";

    // 充币
    public static final String DEPOSIT = "D";

    // 交易
    public static final String TRADE = "T";

    public static final String EXCHANGE_RATE = "exchange_rate";

    public static final String USDT = "USDT";

    public static final String USD = "USD";

    public static final String BTC = "BTC";

    public static final String MARKET_CAP = "https://api.coinmarketcap.com/v2/ticker/%s/?structure=array";
    public static final String MARKET_CAP_PRICE = "https://api.coinmarketcap.com/v2/ticker/%s/?convert=%s&structure=array";
    public static final String ALL_COIN_MSG = "https://api.coinmarketcap.com/v2/listings/";

    public static final String UNLOCK_PASS = "unlock_pass";

    public static final String UNLOCK_REJECT = "unlock_reject";

    public static final String email = "email";

    public static final String exchange = "exchange";

    public static final String SystemCode = "exchange_admin";

    public static final String USER_UNFREEZE = "userstatus_unfreeze";

    public static final String USER_FROZEN = "userstatus_frozen";


    public static final String ACCOUNT_UNFREEZE = "accountstatus_unfreeze";

    public static final String ACCOUNT_FROZEN = "accountstatus_frozen";
}
