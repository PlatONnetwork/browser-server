package com.platon.browser.dto.account;

import lombok.Data;

@Data
public class AccountDowload {
    private byte [] data;
    private String filename;
    private long length;
}
