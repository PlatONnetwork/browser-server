package com.platon.browser.v015;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigInteger;

/**
 * v0.15.0.0版本配置
 */

@Slf4j
@Configuration
@ConfigurationProperties(prefix="v015")
public class V015Config {
    // 锁仓最小释放金额参数生效版本
    private BigInteger restrictingMinimumReleaseActiveVersion;
    // 调账生效版本
    private BigInteger adjustmentActiveVersion;
    // 调账提案ID
    private String adjustmentPipId;
    // 调账日志输出文件（绝对路径）
    private String adjustLogFilePath;

    public BigInteger getAdjustmentActiveVersion() {
        return adjustmentActiveVersion;
    }
    public String getAdjustmentPipId() {
        return adjustmentPipId;
    }
    public void setAdjustmentActiveVersion(BigInteger version) {
        adjustmentActiveVersion = version;
    }
    public void setAdjustmentPipId(String pipid) {
        adjustmentPipId = pipid;
    }
    public String getAdjustLogFilePath(){return adjustLogFilePath;}
    public void setAdjustLogFilePath(String filePath){adjustLogFilePath=filePath;}
    public BigInteger getRestrictingMinimumReleaseActiveVersion() {
        return restrictingMinimumReleaseActiveVersion;
    }
    public void setRestrictingMinimumReleaseActiveVersion(BigInteger version) {
        restrictingMinimumReleaseActiveVersion = version;
    }
}