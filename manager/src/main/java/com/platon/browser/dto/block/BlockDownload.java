package com.platon.browser.dto.block;

import lombok.Data;

@Data
public class BlockDownload {
    private byte [] data;
    private String filename;
    private long length;
}
