package com.platon.browser.config;

import org.apache.commons.lang3.StringUtils;

import com.platon.browser.common.BrowserConst;
import com.platon.browser.req.PageReq;

import lombok.Data;
/**
 * 获取message的dto
 *  @file MessageDto.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年9月20日
 */
@Data
public class MessageDto extends PageReq{

	private String userNo;
	private String key;
    private String queryStatus;
	
    /**
     * 组装key
     * @method getMessageKey
     * @return
     */
	public String getMessageKey () {
		StringBuilder sb = new StringBuilder();
		/**
		 * 如果为null则填空
		 */
		if(this.getPageNo() != null) {
			sb.append(this.getPageNo());
		}
		sb.append(BrowserConst.PEAD_SPILT);
		if(this.getPageSize() != null) {
			sb.append(this.getPageSize());
		}
		sb.append(BrowserConst.PEAD_SPILT);
		if(StringUtils.isNotBlank(this.getKey())) {
			sb.append(this.getKey());
		}
		sb.append(BrowserConst.PEAD_SPILT);
		if(StringUtils.isNotBlank(this.getQueryStatus())) {
			sb.append(this.getQueryStatus());
		}
		return sb.toString();
	}
	
	/**
	 * 解析key
	 * @method analysisKey
	 * @param data
	 * @return
	 */
	public MessageDto analysisKey (String data) {
		String[] message = data.split(BrowserConst.OPT_SPILT);
		if(message.length > 2) {
			this.setPageNo(Integer.parseInt(message[0]));
			this.setPageSize(Integer.parseInt(message[1]));
			this.setKey(message[2]);
			this.setQueryStatus(message[3]);
		}
		return this;
	}
	
}
