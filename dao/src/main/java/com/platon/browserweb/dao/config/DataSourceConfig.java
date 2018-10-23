package com.platon.browserweb.dao.config;

import java.util.Properties;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.PageHelper;

@Configuration  
// 扫描 Mapper 接口并容器管理  
@MapperScan(basePackages = DataSourceConfig.PACKAGE, sqlSessionFactoryRef = "sqlSessionFactory")  
public class DataSourceConfig {  
    // 精确到 具体 目录，以便跟其他数据源隔离  
    static final String PACKAGE = "com.juzix.exchange.dao";  
    static final String MAPPER_LOCATION = "classpath:mapper/*Mapper.xml";  
  
    @Value("${datasource.url}")  
    private String url;  
  
    @Value("${datasource.username}")  
    private String user;  
  
    @Value("${datasource.password}")  
    private String password;  
  
    @Value("${datasource.driverClassName}")  
    private String driverClass;  
  
    @Bean(name = "dataSource")  
    public DruidDataSource dataSource() {  
        DruidDataSource dataSource = new DruidDataSource();  
        dataSource.setDriverClassName(driverClass);  
        dataSource.setUrl(url);  
        dataSource.setUsername(user);  
        dataSource.setPassword(password);  
        return dataSource;  
    }  
  
    @Bean(name = "transactionManager")  
    public DataSourceTransactionManager transactionManager() {  
        return new DataSourceTransactionManager(dataSource());  
    }  
  
    @Bean(name = "sqlSessionFactory")  
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DruidDataSource dataSource)  
            throws Exception {  
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();  
        sessionFactory.setDataSource(dataSource);  
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()  
                .getResources(MAPPER_LOCATION));  
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("reasonable", "false");
        properties.setProperty("supportMethodsArguments", "true");
        properties.setProperty("returnPageInfo", "check");
        properties.setProperty("pageSizeZero","true");
        properties.setProperty("params", "count=countSql");
        pageHelper.setProperties(properties);

        //添加插件
        sessionFactory.setPlugins(new Interceptor[]{pageHelper});
        return sessionFactory.getObject();  
    }  
}  