package com.platon.browser.config;

import com.platon.browser.constant.Browser;
import com.platon.browser.request.PageReq;
import org.apache.commons.lang3.StringUtils;
/**
 * 获取message的dto
 *  @file MessageDto.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年9月20日
 */
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
		 * 分隔符拼接
		 */
		if(this.getPageNo() != null) {
			sb.append(this.getPageNo());
		}
		sb.append(Browser.PEAD_SPILT);
		if(this.getPageSize() != null) {
			sb.append(this.getPageSize());
		}
		sb.append(Browser.PEAD_SPILT);
		if(StringUtils.isNotBlank(this.getKey())) {
			sb.append(this.getKey());
		}
		sb.append(Browser.PEAD_SPILT);
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
		String[] message = data.split(Browser.OPT_SPILT);
		/**
		 * 当key长度大于2才认为合法请求
		 */
		if(message.length > 2) {
			this.setPageNo(Integer.parseInt(message[0]));
			this.setPageSize(Integer.parseInt(message[1]));
			this.setKey(message[2]);
			this.setQueryStatus(message[3]);
		}
		return this;
	}
	
	/**
	 * 解析key
	 * @method analysisKey
	 * @param data  用户号|页码|页数|查询状态|模糊查询key
	 * @return
	 */
	public MessageDto analysisData (String data) {
		/**
		 * 根据分隔符设置参数值
		 */
		String[] message = data.split(Browser.HTTP_SPILT);
		if(message.length > 0) {
			this.setUserNo(message[0]);
		}
		if(message.length > 1) {
			this.setPageNo(Integer.parseInt(message[1]));
		}
		if(message.length > 2) {
			this.setPageSize(Integer.parseInt(message[2]));
		}
		if(message.length > 3) {
			this.setQueryStatus(message[3]);
		}
		if(message.length > 4) {
			this.setKey(message[4]);
		}
		return this;
	}

	public String getUserNo() {
		return userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getQueryStatus() {
		return queryStatus;
	}

	public void setQueryStatus(String queryStatus) {
		this.queryStatus = queryStatus;
	}
	
}
