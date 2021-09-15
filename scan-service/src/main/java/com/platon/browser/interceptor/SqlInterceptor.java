package com.platon.browser.interceptor;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Matcher;

/**
 * mybatis拦截打印异常的SQL--针对update类型的SQL(delete、insert、update)
 *
 * @author huangyongpeng@matrixelements.com
 * @date 2021/4/24
 */
@Slf4j
@Intercepts(value = {@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class SqlInterceptor implements Interceptor {

    public SqlInterceptor() {
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Object parameterObject = args[1];
        // id为执行的mapper方法的全路径名，如com.mapper.UserMapper
        String id = ms.getId();
        // sql语句类型 select、delete、insert、update
        String sqlCommandType = ms.getSqlCommandType().toString();
        BoundSql boundSql = ms.getBoundSql(parameterObject);
        // 获取节点的配置
        Configuration configuration = ms.getConfiguration();
        Object obj;
        try {
            obj = invocation.proceed();
        } catch (Exception e) {
            log.error("异常SQL:类型为{},路径为{},语句为{};", sqlCommandType, id, showSql(configuration, boundSql));
            throw e;
        }
        return obj;
    }

    /**
     * 生成拦截对象的代理
     *
     * @param target 目标对象
     * @return java.lang.Object 对象
     * @author huangyongpeng@matrixelements.com
     * @date 2021/4/25
     */
    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    /**
     * mybatis配置的属性
     *
     * @param properties mybatis配置的属性
     * @return void
     * @author huangyongpeng@matrixelements.com
     * @date 2021/4/25
     */
    @Override
    public void setProperties(Properties properties) {

    }

    /**
     * 打印SQL
     *
     * @param configuration
     * @param boundSql
     * @return java.lang.String
     * @author huangyongpeng@matrixelements.com
     * @date 2021/4/24
     */
    public String showSql(Configuration configuration, BoundSql boundSql) {
        String sql = "";
        try {
            // 获取参数
            Object parameterObject = boundSql.getParameterObject();
            List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
            // sql语句中多个空格都用一个空格代替
            sql = boundSql.getSql().replaceAll("[\\s]+", " ");
            if (CollUtil.isNotEmpty(parameterMappings) && parameterObject != null) {
                // 获取类型处理器注册器，类型处理器的功能是进行java类型和数据库类型的转换　　　　　　　
                // 如果根据parameterObject.getClass(）可以找到对应的类型，则替换
                TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
                if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                    sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(parameterObject)));
                } else {
                    //MetaObject主要是封装了originalObject对象，
                    // 提供了get和set的方法用于获取和设置originalObject的属性值,
                    // 主要支持对JavaBean、Collection、Map三种类型对象的操作
                    MetaObject metaObject = configuration.newMetaObject(parameterObject);
                    for (ParameterMapping parameterMapping : parameterMappings) {
                        String propertyName = parameterMapping.getProperty();
                        if (metaObject.hasGetter(propertyName)) {
                            Object obj = metaObject.getValue(propertyName);
                            sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(obj)));
                        } else if (boundSql.hasAdditionalParameter(propertyName)) {
                            // 该分支是动态sql
                            Object obj = boundSql.getAdditionalParameter(propertyName);
                            sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(obj)));
                        } else {
                            //打印出缺失，提醒该参数缺失并防止错位
                            sql = sql.replaceFirst("\\?", "缺失");
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("解析SQL异常", e);
        }
        return sql;
    }

    /**
     * 获取参数值
     *
     * @param obj
     * @return java.lang.String
     * @author huangyongpeng@matrixelements.com
     * @date 2021/4/24
     */
    private String getParameterValue(Object obj) {
        String value = null;
        try {
            if (obj instanceof String) {
                value = "'" + obj.toString() + "'";
            } else if (obj instanceof Date) {
                DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
                value = "'" + formatter.format(new Date()) + "'";
            } else {
                if (obj != null) {
                    value = obj.toString();
                } else {
                    value = "";
                }
            }
        } catch (Exception e) {
            log.error("获取参数值异常", e);
        }
        return value;
    }

}
