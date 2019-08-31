package com.platon.browser.now.service;

import com.platon.browser.dto.account.AccountDownload;
import com.platon.browser.req.PageReq;
import com.platon.browser.req.newtransaction.TransactionDetailNavigateReq;
import com.platon.browser.req.newtransaction.TransactionDetailsReq;
import com.platon.browser.req.newtransaction.TransactionListByAddressRequest;
import com.platon.browser.req.newtransaction.TransactionListByBlockRequest;
import com.platon.browser.res.RespPage;
import com.platon.browser.res.transaction.TransactionDetailsResp;
import com.platon.browser.res.transaction.TransactionListResp;

/**
 * 交易模块接口方法定义
 *  @file TransactionService.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public interface TransactionService {

	/**
	 * 分页查询交易列表
	 * @method getTransactionList
	 * @param req
	 * @return
	 */
    RespPage<TransactionListResp> getTransactionList( PageReq req);

    /**
     * 根据区块号查询交易列表
     * @method getTransactionListByBlock
     * @param req
     * @return
     */
    RespPage<TransactionListResp> getTransactionListByBlock(TransactionListByBlockRequest req);

    /**
     * 根据地址查询交易列表
     * @method getTransactionListByAddress
     * @param req
     * @return
     */
    RespPage<TransactionListResp> getTransactionListByAddress(TransactionListByAddressRequest req);

    /**
     * 地址交易列表下载
     * @method transactionListByAddressDownload
     * @param address
     * @param date
     * @return
     */
    AccountDownload transactionListByAddressDownload(String address, String date);

    /**
     * 根据交易hash查询交易详情
     * @method transactionDetails
     * @param req
     * @return
     */
	TransactionDetailsResp transactionDetails( TransactionDetailsReq req);

	/**
	 * 切换交易详情上一个下一个
	 * @method transactionDetailNavigate
	 * @param req
	 * @return
	 */
	TransactionListResp transactionDetailNavigate( TransactionDetailNavigateReq req);
}
