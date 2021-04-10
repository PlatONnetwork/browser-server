package com.platon.browser.bean;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * @Auther: Chendongming
 * @Date: 2019/9/4 13:53
 * @Description: 处罚信息Bean
 */
public class SlashInfo {
    private BigInteger blockNumber;
    private BigInteger slashBlockCount;
    private BigInteger blockCount;
    private BigDecimal slashAmount;
    private Boolean kickOut;
    private Date slashTime;

    public BigInteger getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(BigInteger blockNumber) {
        this.blockNumber = blockNumber;
    }

    public BigInteger getSlashBlockCount() {
        return slashBlockCount;
    }

    public void setSlashBlockCount(BigInteger slashBlockCount) {
        this.slashBlockCount = slashBlockCount;
    }

    public BigInteger getBlockCount() {
        return blockCount;
    }

    public void setBlockCount(BigInteger blockCount) {
        this.blockCount = blockCount;
    }

    public BigDecimal getSlashAmount() {
        return slashAmount;
    }

    public void setSlashAmount(BigDecimal slashAmount) {
        this.slashAmount = slashAmount;
    }

    public Boolean getKickOut() {
        return kickOut;
    }

    public void setKickOut(Boolean kickOut) {
        this.kickOut = kickOut;
    }

    public Date getSlashTime() {
        return slashTime;
    }

    public void setSlashTime(Date slashTime) {
        this.slashTime = slashTime;
    }
}
