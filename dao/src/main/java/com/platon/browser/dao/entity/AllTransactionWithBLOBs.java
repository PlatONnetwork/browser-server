package com.platon.browser.dao.entity;

public class AllTransactionWithBLOBs extends AllTransaction {
    private String input;

    private String failReason;

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input == null ? null : input.trim();
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason == null ? null : failReason.trim();
    }
}