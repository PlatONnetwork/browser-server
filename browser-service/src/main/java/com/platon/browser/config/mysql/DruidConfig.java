package com.platon.browser.config.mysql;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.ResourceServlet;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Druid 配置
 *
 * @author huangyongpeng@matrixelements.com
 * @date 2021/4/1
 */
@Configuration
public class DruidConfig {

    @Value("${spring.datasource.monitor.enabled:false}")
    @Getter
    @Setter
    private boolean monitorEnabled;

    @Value("${spring.datasource.monitor.username:platon}")
    @Getter
    @Setter
    private String monitorUsername;

    @Value("${spring.datasource.monitor.password:platon}")
    @Getter
    @Setter
    private String monitorPassword;

    /**
     * 配置数据源
     *
     * @param
     * @return javax.sql.DataSource
     * @author huangyongpeng@matrixelements.com
     * @date 2021/4/7
     */
    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource druid() {
        return new DruidDataSource();
    }

    /**
     * 配置 Druid 监控管理后台的Servlet
     * 内置 Servlet 容器时没有web.xml文件，所以使用 Spring Boot 的注册 Servlet 方式
     * 访问URL：http://{ip}:{port}/browser-server/druid/login.html
     *
     * @param
     * @return org.springframework.boot.web.servlet.ServletRegistrationBean
     * @author huangyongpeng@matrixelements.com
     * @date 2021/4/1
     */
    @Bean
    public ServletRegistrationBean statViewServlet() {
        ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        // 这些参数可以在 com.alibaba.druid.support.http.StatViewServlet 的父类 com.alibaba.druid.support.http.ResourceServlet 中找到
        Map<String, String> initParams = new HashMap<>();
        initParams.put(ResourceServlet.PARAM_NAME_USERNAME, monitorUsername);
        initParams.put(ResourceServlet.PARAM_NAME_PASSWORD, monitorPassword);
        // 白名单，默认就是允许所有访问
        initParams.put(ResourceServlet.PARAM_NAME_ALLOW, ""); //默认就是允许所有访问
        // 黑名单
        // initParams.put(ResourceServlet.PARAM_NAME_DENY, "192.168.10.132");
        bean.setInitParameters(initParams);
        // 是否可以重置数据
        bean.addInitParameter("resetEnable", "false");
        // 是否开启druid monitor
        bean.setEnabled(monitorEnabled);
        return bean;
    }

    /**
     * 配置一个web监控的filter
     *
     * @param
     * @return org.springframework.boot.web.servlet.FilterRegistrationBean
     * @author huangyongpeng@matrixelements.com
     * @date 2021/4/1
     */
    @Bean
    public FilterRegistrationBean webStatFilter() {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new WebStatFilter());
        Map<String, String> initParams = new HashMap<>();
        initParams.put("exclusions", "*.js,*.css,/druid/*");
        bean.setInitParameters(initParams);
        bean.setUrlPatterns(Arrays.asList("/*"));
        return bean;
    }

}
