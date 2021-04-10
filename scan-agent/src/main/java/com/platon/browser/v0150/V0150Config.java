package com.platon.browser.v0150;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigInteger;

/**
 * v0.15.0.0版本配置
 */

@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix="v0150")
public class V0150Config {
    // 锁仓最小释放金额参数生效版本
    private BigInteger restrictingMinimumReleaseActiveVersion;
    // 调账生效版本
    private BigInteger adjustmentActiveVersion;
    // 调账提案ID
    private String adjustmentPipId;
    // 调账日志输出文件（绝对路径）
    private String adjustLogFilePath;
}