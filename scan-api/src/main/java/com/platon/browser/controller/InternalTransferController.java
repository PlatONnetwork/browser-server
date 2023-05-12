package com.platon.browser.controller;

import com.platon.browser.request.internaltransfer.QueryByAddressRequest;
import com.platon.browser.response.RespPage;
import com.platon.browser.response.transaction.InternalTransferParam;
import com.platon.browser.service.InternalTransferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 *  内部转账
 */
@Slf4j
@RestController
public class InternalTransferController {

    @Resource
    private InternalTransferService internalTransferService;

    @PostMapping("internalTransfer/listByOwnerAddress")
    public Mono<RespPage<InternalTransferParam>> listByOwnerAddress(@Valid @RequestBody QueryByAddressRequest req) {
        return Mono.just(internalTransferService.listByOwnerAddress(req));
    }
}
