package com.platon.browser.config;

import com.platon.browser.bean.CommonConstant;
import com.platon.browser.response.account.AccountDownload;
import com.platon.browser.utils.CommonUtil;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 下载文件统一封装类
 *
 * @author zhangrj
 * @file DownFileCommon.java
 * @description
 * @data 2019年8月31日
 */
@Slf4j
@Component
public class DownFileCommon {

    /**
     * 下载方法
     *
     * @throws Exception
     * @method download
     */
    public void download(HttpServletResponse response, String filename, long length, byte[] data) throws IOException {
        /** 返回设置头和type*/
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);
        response.setHeader(CommonConstant.TRACE_ID, CommonUtil.ofNullable(() -> CommonUtil.getTraceId()).orElse(""));
        response.setContentType("application/octet-stream");
        response.setContentLengthLong(length);
        response.getOutputStream().write(data);
    }


    public AccountDownload writeDate(String filename, List<Object[]> rows, String... headers) {
        AccountDownload accountDownload = new AccountDownload();
        /** 初始化输出流对象 */
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            /** 设置导出的csv头，防止乱码 */
            byteArrayOutputStream.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
        } catch (Exception e) {
            this.log.error("输出数据错误:", e);
            return accountDownload;
        }
        Writer outputWriter = new OutputStreamWriter(byteArrayOutputStream, StandardCharsets.UTF_8);
        CsvWriter writer = new CsvWriter(outputWriter, new CsvWriterSettings());
        /** 设置导出表的表头 */
        writer.writeHeaders(headers);
        writer.writeRowsAndClose(rows);
        /** 设置返回对象 */
        accountDownload.setData(byteArrayOutputStream.toByteArray());
        accountDownload.setFilename(filename);
        accountDownload.setLength(byteArrayOutputStream.size());
        return accountDownload;
    }

}
