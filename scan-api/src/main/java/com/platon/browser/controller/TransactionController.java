package com.platon.browser.controller;

import com.platon.browser.config.CommonMethod;
import com.platon.browser.config.DownFileCommon;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.request.PageReq;
import com.platon.browser.request.newtransaction.TransactionDetailsReq;
import com.platon.browser.request.newtransaction.TransactionListByAddressRequest;
import com.platon.browser.request.newtransaction.TransactionListByBlockRequest;
import com.platon.browser.request.staking.QueryClaimByStakingReq;
import com.platon.browser.response.BaseResp;
import com.platon.browser.response.RespPage;
import com.platon.browser.response.account.AccountDownload;
import com.platon.browser.response.staking.QueryClaimByStakingResp;
import com.platon.browser.response.transaction.QueryClaimByAddressResp;
import com.platon.browser.response.transaction.TransactionDetailsResp;
import com.platon.browser.response.transaction.TransactionListResp;
import com.platon.browser.service.TransactionService;
import com.platon.browser.utils.I18nUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 交易模块Contract。定义使用方法
 *
 * @author zhangrj
 * @file AppDocTransactionController.java
 * @description
 * @data 2019年8月31日
 */
@Slf4j
@RestController
public class TransactionController {

    @Resource
    private I18nUtil i18n;

    @Resource
    private TransactionService transactionService;

    @Resource
    private DownFileCommon downFileCommon;

    @Resource
    private CommonMethod commonMethod;

    /**
     * 交易列表
     *
     * @param req
     * @return reactor.core.publisher.Mono<com.platon.browser.response.RespPage < com.platon.browser.response.transaction.TransactionListResp>>
     * @date 2021/5/25
     */
    @PostMapping("transaction/transactionList")
    public Mono<RespPage<TransactionListResp>> transactionList(@Valid @RequestBody PageReq req) {
        return Mono.just(transactionService.getTransactionList(req));
    }

    /**
     * 区块的交易列表
     *
     * @param req
     * @return reactor.core.publisher.Mono<com.platon.browser.response.RespPage < com.platon.browser.response.transaction.TransactionListResp>>
     * @date 2021/5/25
     */
    @PostMapping("transaction/transactionListByBlock")
    public Mono<RespPage<TransactionListResp>> transactionListByBlock(@Valid @RequestBody TransactionListByBlockRequest req) {
        return Mono.just(transactionService.getTransactionListByBlock(req));
    }

    /**
     * 地址的交易列表
     *
     * @param req
     * @return reactor.core.publisher.Mono<com.platon.browser.response.RespPage < com.platon.browser.response.transaction.TransactionListResp>>
     * @date 2021/5/25
     */
    @PostMapping("transaction/transactionListByAddress")
    public Mono<RespPage<TransactionListResp>> transactionListByAddress(@Valid @RequestBody TransactionListByAddressRequest req) {
        return Mono.just(transactionService.getTransactionListByAddress(req));
    }

    /**
     * 导出地址交易列表
     *
     * @param address
     * @param date
     * @param local
     * @param timeZone
     * @param token
     * @param response
     * @return void
     * @date 2021/5/25
     */
    @GetMapping("transaction/addressTransactionDownload")
    public void addressTransactionDownload(
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "date", required = true) Long date,
            @RequestParam(value = "local", required = true) String local,
            @RequestParam(value = "timeZone", required = true) String timeZone,
            @RequestParam(value = "token", required = false) String token,
            HttpServletResponse response
    ) {
        /**
         * 鉴权
         */
        this.commonMethod.recaptchaAuth(token);
        /**
         * 对地址进行补充前缀
         */
        address = address.toLowerCase();
        AccountDownload accountDownload =
                transactionService.transactionListByAddressDownload(address, date, local, timeZone);
        try {
            downFileCommon.download(response, accountDownload.getFilename(), accountDownload.getLength(),
                    accountDownload.getData());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BusinessException(i18n.i(I18nEnum.DOWNLOAD_EXCEPTION));
        }
    }

    /**
     * 交易详情
     *
     * @param req
     * @return reactor.core.publisher.Mono<com.platon.browser.response.BaseResp < com.platon.browser.response.transaction.TransactionDetailsResp>>
     * @date 2021/5/25
     */
    @PostMapping("transaction/transactionDetails")
    public Mono<BaseResp<TransactionDetailsResp>> transactionDetails(@Valid @RequestBody TransactionDetailsReq req) {
        return Mono.create(sink -> {
            TransactionDetailsResp resp = transactionService.transactionDetails(req);
            sink.success(BaseResp.build(RetEnum.RET_SUCCESS.getCode(), i18n.i(I18nEnum.SUCCESS), resp));
        });
    }

    /**
     * 地址的领取奖励列表
     *
     * @param req
     * @return reactor.core.publisher.Mono<com.platon.browser.response.RespPage < com.platon.browser.response.transaction.QueryClaimByAddressResp>>
     * @date 2021/5/25
     */
    @PostMapping("transaction/queryClaimByAddress")
    public Mono<RespPage<QueryClaimByAddressResp>> queryClaimByAddress(@Valid @RequestBody TransactionListByAddressRequest req) {
        return Mono.just(transactionService.queryClaimByAddress(req));
    }

    /**
     * 节点相关的领取奖励列表
     *
     * @param req
     * @return reactor.core.publisher.Mono<com.platon.browser.response.RespPage < com.platon.browser.response.staking.QueryClaimByStakingResp>>
     * @date 2021/5/25
     */
    @PostMapping("transaction/queryClaimByStaking")
    public Mono<RespPage<QueryClaimByStakingResp>> queryClaimByStaking(@Valid @RequestBody QueryClaimByStakingReq req) {
        return Mono.just(transactionService.queryClaimByStaking(req));
    }

}
