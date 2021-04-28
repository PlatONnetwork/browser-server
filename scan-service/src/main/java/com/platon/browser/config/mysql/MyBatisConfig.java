package com.platon.browser.config.mysql;

import com.github.pagehelper.autoconfigure.PageHelperAutoConfiguration;
import com.platon.browser.interceptor.SqlInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * mybatis配置
 *
 * @author huangyongpeng@matrixelements.com
 * @date 2021/4/25
 */
@Configuration
@AutoConfigureAfter(PageHelperAutoConfiguration.class)
public class MyBatisConfig {

    @Autowired
    private List<SqlSessionFactory> sqlSessionFactoryList;

    /**
     * 添加SQL拦截器
     *
     * @param
     * @return void
     * @author huangyongpeng@matrixelements.com
     * @date 2021/4/25
     */
    @PostConstruct
    public void sqlInterceptor() {
        SqlInterceptor interceptor = new SqlInterceptor();
        for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
            sqlSessionFactory.getConfiguration().addInterceptor(interceptor);
        }
    }

}
