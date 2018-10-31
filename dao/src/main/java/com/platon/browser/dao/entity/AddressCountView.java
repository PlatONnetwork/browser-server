package com.platon.browser.dao.entity;

public class AddressCountView {
    private String from;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from == null ? null : from.trim();
    }
}