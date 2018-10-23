package com.platon.browserweb.common.constant;

/**
 * 操作日志操作类型变量
 * @author 89512
 *
 */
public interface OperatingRecordOpType {
	//增加
    public static final String ADD = "ADD";
    //编辑
    public static final String EDIT = "EDIT";
    //删除
    public static final String DETELE = "DETELE";
    //冻结
    public static final String LOCKED = "LOCKED";
    //解冻
    public static final String UNLOCK = "UNLOCK";
    //授权
    public static final String AUTHZ = "AUTHZ";
    //发布
    public static final String PUBLISH = "PUBLISH";
    //设置为计价货币
    public static final String SET_QUOTE = "SET_QUOTE";
    //设置为非计价货币
    public static final String UNSET_QUOTE = "UNSET_QUOTE";
    //设置为基准货币
    public static final String SET_BASE = "SET_BASE";
    //设置为非基准货币
    public static final String UNSET_BASE = "UNSET_BASE";
    //设置精度
    public static final String SET_ACCURACY = "SET_ACCURACY";
    // 启用
    String ENABLE = "ENABLE";
    // 禁用
    String DISABLE = "DISABLE";
    
}
