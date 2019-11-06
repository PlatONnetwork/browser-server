package com.platon.browser.persistence.service.rmdb;

import com.platon.browser.common.complement.dto.BusinessParam;
import com.platon.browser.persistence.dao.mapper.EpochBusinessMapper;
import com.platon.browser.persistence.dao.mapper.NewBlockMapper;
import com.platon.browser.persistence.dao.mapper.SlashBusinessMapper;
import com.platon.browser.persistence.dao.mapper.StakeBusinessMapper;
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
    private StakeBusinessMapper stakeBusinessMapper;
    @Autowired
    private SlashBusinessMapper slashBusinessMapper;
    @Autowired
    private EpochBusinessMapper epochBusinessMapper;
    @Autowired
    private NewBlockMapper newBlockMapper;

    public void insert(List<BusinessParam> params){
        params.forEach(param->{
            switch (param.getBusinessType()) {
                case STAKE_CREATE: // 1000 发起质押
//                    stakeBusinessMapper.create(param);
                    return;
                case STAKE_MODIFY: // 1001 修改质押信息
//                    stakeBusinessMapper.modify(param);
                    return;
                case STAKE_INCREASE: // 1002 增持质押
//                    stakeBusinessMapper.increase(param);
                    return;
                case STAKE_EXIT: // 1003 撤销质押
//                    stakeBusinessMapper.exit(param);
                    return;
                case DELEGATE_CREATE: // 1004 TODO: 发起委托

                    return;
                case DELEGATE_EXIT: // 1005 TODO: 减持/撤销委托

                    return;
                case PROPOSAL_TEXT: // 2000 TODO: 提交文本提案

                    return;
                case PROPOSAL_UPGRADE: // 2001 TODO: 提交升级提案

                    return;
                case PROPOSAL_CANCEL: // 2005 TODO: 提交取消提案

                    return;
                case PROPOSAL_VOTE: // 2003 TODO: 给提案投票

                    return;
                case VERSION_DECLARE: // 2004 TODO: 版本声明

                    return;
                case REPORT: // 3000 举报双签
//                    slashBusinessMapper.report(param);
                    return;
                case RESTRICTING_CREATE: // 4000 TODO: 创建锁仓计划

                    return;
                case NEW_BLOCK: // TODO: 新区块
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
                default:
                    return;
            }
        });
    }
}
