package com.platon.browser.service;

import com.platon.browser.dto.account.AccountDowload;
import com.platon.browser.dto.account.ContractDowload;
import com.platon.browser.req.account.AccountDownloadReq;
import com.platon.browser.req.account.ContractDownloadReq;

/**
 * 导出服务
 */
public interface ExportService {

    AccountDowload exportAccountCsv(AccountDownloadReq req);

    ContractDowload exportContractCsv(ContractDownloadReq req);
}
