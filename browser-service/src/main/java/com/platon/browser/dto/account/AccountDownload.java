package com.platon.browser.dto.account;

import lombok.Data;

@Data
public class AccountDownload {
    private byte [] data;
    private String filename;
    private long length;
}
