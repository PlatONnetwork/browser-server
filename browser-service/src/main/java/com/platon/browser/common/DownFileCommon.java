package com.platon.browser.common;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 下载文件统一封装类
 *  @file DownFileCommon.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
@Service
public class DownFileCommon {

	private final Logger logger = LoggerFactory.getLogger(DownFileCommon.class);
	
	/**
	 * 下载方法
	 * @throws Exception 
	 * @method download
	 */
	public void download(HttpServletResponse response, String filename, long length, byte [] data) throws Exception{
		/** 返回设置头和type*/
        response.setHeader("Content-Disposition", "attachment; filename="+filename);
        response.setContentType("application/octet-stream");
        response.setContentLengthLong(length);
        try {
            response.getOutputStream().write(data);
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw e;
        }
    }
}
