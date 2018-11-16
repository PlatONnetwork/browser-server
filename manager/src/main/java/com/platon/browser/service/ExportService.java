package com.platon.browser.service;

import com.platon.browser.dto.account.AccountDowload;
import com.platon.browser.req.account.AccountDownloadReq;

/**
 * 导出服务
 */
public interface ExportService {
    AccountDowload exportAccountCsv(AccountDownloadReq req);
}
