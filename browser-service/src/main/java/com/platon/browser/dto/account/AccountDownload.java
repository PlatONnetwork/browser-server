package com.platon.browser.dto.account;

import lombok.Data;

/**
 * 地址文件下载对象，用来传递下载参数
 *  @file AccountDownload.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
@Data
public class AccountDownload {
    private byte [] data;
    private String filename;
    private long length;
}
