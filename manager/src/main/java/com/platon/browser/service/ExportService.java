package com.platon.browser.service;

import com.platon.browser.dto.account.AccountDownload;
import com.platon.browser.dto.block.BlockDownload;
import com.platon.browser.req.account.AccountDownloadReq;
import com.platon.browser.req.block.BlockDownloadReq;

/**
 * 导出服务
 */
public interface ExportService {
    AccountDownload exportAccountCsv(AccountDownloadReq req);
    BlockDownload exportNodeBlockCsv(BlockDownloadReq req);
}
