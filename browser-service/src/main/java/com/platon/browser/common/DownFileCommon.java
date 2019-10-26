package com.platon.browser.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 下载文件统一封装类
 *  @file DownFileCommon.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
@Slf4j
@Service
public class DownFileCommon {

	/**
	 * 下载方法
	 * @throws Exception 
	 * @method download
	 */
	public void download(HttpServletResponse response, String filename, long length, byte [] data) throws IOException {
		/** 返回设置头和type*/
        response.setHeader("Content-Disposition", "attachment; filename="+filename);
        response.setContentType("application/octet-stream");
        response.setContentLengthLong(length);
        response.getOutputStream().write(data);
    }
}
