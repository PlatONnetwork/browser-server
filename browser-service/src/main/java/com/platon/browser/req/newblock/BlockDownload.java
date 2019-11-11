package com.platon.browser.req.newblock;

import lombok.Data;

/**
 *  
 *  @file BlockDownload.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
@Data
public class BlockDownload {
    private byte [] data;
    private String filename;
    private long length;
}
