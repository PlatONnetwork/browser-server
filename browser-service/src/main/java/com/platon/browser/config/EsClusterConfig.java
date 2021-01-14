package com.platon.browser.config;

import lombok.Data;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: Chendongming
 * @Date: 2019/10/25 15:12
 * @Description: ES配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix="spring.elasticsearch.high-level-client")
public class EsClusterConfig {
    private List<String> hosts; // 集群地址，多个用,隔开
    private int port; // 使用的端口号
    private String schema; // 使用的协议
    private String username; // 用户名
    private String password; // 密码

    private int connectTimeOut = 10000; // 连接超时时间
    private int socketTimeOut = 30000; // 连接超时时间
    private int connectionRequestTimeOut = 5000; // 获取连接的超时时间

    private int maxConnectNum = 200; // 最大连接数
    private int maxConnectPerRoute = 200; // 最大路由连接数

    @Bean(name = "restHighLevelClient")
    public RestHighLevelClient client() {
    	CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
        List<HttpHost> hostList = new ArrayList<>();
        hosts.forEach(host->hostList.add(new HttpHost(host,port,schema)));
        RestClientBuilder builder = RestClient.builder(hostList.toArray(new HttpHost[0]));
        // 异步httpclient连接延时配置
        builder.setRequestConfigCallback(requestConfigBuilder -> {
            requestConfigBuilder.setConnectTimeout(connectTimeOut);
            requestConfigBuilder.setSocketTimeout(socketTimeOut);
            requestConfigBuilder.setConnectionRequestTimeout(connectionRequestTimeOut);
            return requestConfigBuilder;
        });
        // 异步httpclient连接数配置
        builder.setHttpClientConfigCallback(httpClientBuilder -> {
            httpClientBuilder.setMaxConnTotal(maxConnectNum);
            httpClientBuilder.setMaxConnPerRoute(maxConnectPerRoute);
            httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            return httpClientBuilder;
        });
        return new RestHighLevelClient(builder);
    }
}