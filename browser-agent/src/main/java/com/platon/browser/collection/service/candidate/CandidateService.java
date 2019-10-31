package com.platon.browser.collection.service.candidate;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.web3j.platon.bean.Node;

import java.util.List;

/**
 * 候选人(包括结算周期验证人和共识周期验证人)服务
 *
 * 1、根据区块号计算周期切换相关值：
 *      名称/含义                                                                   变量名称
 * a、当前共识周期验证人列表                                                       curValidators
 *    前一共识周期验证人列表                                                       preValidators
 * b、当前结算周期验证人列表                                                       curVerifiers
 *    前一结算周期验证人列表                                                       preVerifiers
 */
@Slf4j
@Service
public class CandidateService {

    @Getter private List<Node> curValidators; // 当前共识周期验证人列表
    @Getter private List<Node> preValidators; // 前一共识周期验证人列表
    @Getter private List<Node> curVerifiers; // 当前结算周期验证人列表
    @Getter private List<Node> preVerifiers; // 前一结算周期验证人列表


}
