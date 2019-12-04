package com.platon.browser.req.newblock;

/**
 *  
 *  @file BlockDownload.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class BlockDownload {
    private byte [] data;
    private String filename;
    private long length;
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public long getLength() {
		return length;
	}
	public void setLength(long length) {
		this.length = length;
	}
    
}
