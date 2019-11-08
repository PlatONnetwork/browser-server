package com.platon.browser.persistence.service.rmdb;

import com.platon.browser.common.complement.dto.BusinessParam;
import com.platon.browser.persistence.dao.mapper.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @Auther: dongqile
 * @Date: 2019/8/17 20:09
 * @Description: 批量入库服务
 */
@Slf4j
@Service
public class DbService {

    @Autowired
    private NewBlockMapper newBlockMapper;
    @Autowired
    private StakeBusinessMapper stakeBusinessMapper;
    @Autowired
    private SlashBusinessMapper slashBusinessMapper;
    @Autowired
    private EpochBusinessMapper epochBusinessMapper;
    @Autowired
    private StatisticBusinessMapper statisticBusinessMapper;
    @Autowired
    private DelegateBusinessMapper delegateBusinessMapper;
    @Autowired
    private ProposalBusinessMapper proposalBusinessMapper;

    public void insert(List<BusinessParam> params){
        params.forEach(param->{
            switch (param.getBusinessType()) {
                /*------------------区块、周期切换相关业务 START------------------*/
                case NEW_BLOCK: // 新区块
                    newBlockMapper.newBlock(param);
                    return;
                case ELECTION_EPOCH: // 选举事件
                    epochBusinessMapper.election(param);
                    return;
                case CONSENSUS_EPOCH: // 共识周期切换
                    epochBusinessMapper.consensus(param);
                    return;
                case SETTLE_EPOCH: // 结算周期切换
//                    epochBusinessMapper.settle(param);
                    return;
                /*------------------统计相关业务 START------------------*/
                case NETWORK_STATISTIC: // 网络统计
                    statisticBusinessMapper.networkChange(param);
                    return;
                case ADDRESS_STATISTIC: // 地址统计
                    statisticBusinessMapper.addressChange(param);
                    return;
                /*------------------质押相关业务 START------------------*/
                case STAKE_CREATE: // 1000 发起质押
                    stakeBusinessMapper.create(param);
                    return;
                case STAKE_MODIFY: // 1001 修改质押信息
                    stakeBusinessMapper.modify(param);
                    return;
                case STAKE_INCREASE: // 1002 增持质押
                    stakeBusinessMapper.increase(param);
                    return;
                case STAKE_EXIT: // 1003 撤销质押
//                    stakeBusinessMapper.exit(param);
                    return;
                /*------------------委托相关业务 START------------------*/
                case DELEGATE_CREATE: // 1004 发起委托
                    delegateBusinessMapper.create(param);
                    return;
                case DELEGATE_EXIT: // 1005 减持/撤销委托
                    delegateBusinessMapper.exit(param);
                    return;
                /*------------------提案相关业务 START------------------*/
                case PROPOSAL_TEXT: // 2000 提交文本
                    proposalBusinessMapper.text(param);
                    return;
                case PROPOSAL_UPGRADE: // 2001 提交升级提案
                    proposalBusinessMapper.upgrade(param);
                    return;
                case PROPOSAL_CANCEL: // 2005 提交取消提案
                    proposalBusinessMapper.cancel(param);
                    return;
                case PROPOSAL_VOTE: // 2003 给提案投票
                    proposalBusinessMapper.vote(param);
                    return;
//                /*------------------处罚、其它业务 START------------------*/
//                case VERSION_DECLARE: // 2004 TODO: 版本声明
//
//                    return;
                case REPORT: // 3000 举报双签
                    slashBusinessMapper.report(param);
                    return;
//                case RESTRICTING_CREATE: // 4000 TODO: 创建锁仓计划
//
//                    return;
                default:
                    return;
            }
        });
    }
    
    
    
}
