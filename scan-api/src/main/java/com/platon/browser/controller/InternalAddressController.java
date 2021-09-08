package com.platon.browser.controller;

import com.platon.browser.request.PageReq;
import com.platon.browser.response.RespPage;
import com.platon.browser.response.address.InternalAddrResp;
import com.platon.browser.response.address.InternalAddressResp;
import com.platon.browser.service.InternalAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 内置地址
 *
 * @date 2021/5/31
 */
@Slf4j
@RestController
public class InternalAddressController {

    @Resource
    private InternalAddressService internalAddressService;

    /**
     * 获取基金会账户列表（有地址余额）
     *
     * @param req
     * @return reactor.core.publisher.Mono<com.platon.browser.response.RespPage < com.platon.browser.response.address.InternalAddressResp>>
     * @date 2021/5/31
     */
    @PostMapping("internalAddress/foundationInfo")
    public Mono<RespPage<InternalAddressResp>> getFoundationInfo(@Valid @RequestBody PageReq req) {
        return Mono.just(internalAddressService.getFoundationInfo(req));
    }

    /**
     * 获取基金会地址
     *
     * @param req:
     * @return: reactor.core.publisher.Mono<com.platon.browser.response.RespPage < com.platon.browser.response.address.InternalAddrResp>>
     * @date: 2021/9/8
     */
    @PostMapping("internalAddress/list")
    public Mono<RespPage<InternalAddrResp>> getInternalAddressList(@Valid @RequestBody PageReq req) {
        return Mono.just(internalAddressService.getInternalAddressList(req));
    }

}
