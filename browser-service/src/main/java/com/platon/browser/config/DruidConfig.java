package com.platon.browser.config;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * 统计数据库性能
 * @Auther: Chendongming
 * @Date: 2019/4/28 11:53
 * @Description:
 */
//@Configuration
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DruidConfig {

    
	@Bean
    public ServletRegistrationBean viewServlet(){
        ServletRegistrationBean servletRegistrationBean=new ServletRegistrationBean(new StatViewServlet(),"/druid/*");
        /** IP白名单 */
        //servletRegistrationBean.addInitParameter("allow","192.168.1.12,127.0.0.1");
        /**IP黑名单    */
        //servletRegistrationBean.addInitParameter("deny","192.168.4.23");
        /**控制台用户 */
        servletRegistrationBean.addInitParameter("loginUsername","admin");
        servletRegistrationBean.addInitParameter("loginPassword","admin");
        //是否能够重置数据
        servletRegistrationBean.addInitParameter("resetEnable","false");
        return servletRegistrationBean;
    }
    @Bean
    public FilterRegistrationBean statFilter(){
        FilterRegistrationBean filterRegistrationBean=new FilterRegistrationBean(new WebStatFilter());
        //添加过滤规则
        filterRegistrationBean.addInitParameter("exclusions","*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }
}
