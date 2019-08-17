package com.platon.browser.engine;

import com.platon.browser.dao.entity.Address;
import com.platon.browser.dao.mapper.AddressMapper;
import com.platon.browser.dto.CustomTransaction;
import com.platon.browser.engine.result.AddressExecuteResult;
import com.platon.browser.enums.AddressEnum;
import com.platon.browser.enums.InnerContractAddEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: dongqile
 * Date: 2019/8/14
 * Time: 17:14
 */
@Component
public class AddressExecute {
    private static Logger logger = LoggerFactory.getLogger(AddressExecute.class);

    @Autowired
    private AddressMapper addressMapper;

    private AddressExecuteResult addressExecuteResult = new AddressExecuteResult();

    private Map <String, Address> addressMap = new HashMap <>();

    @PostConstruct
    private void init () {
        // 初始化全量数据
        List <Address> addresseList = addressMapper.selectByExample(null);
        addresseList.forEach(address -> addressMap.put(address.getAddress(), address));
    }

    public void execute ( CustomTransaction tx ) {
        //入库前对address进行数据分析统计
        if (addressMap.get(tx.getFrom()).equals(null) || addressMap.get(tx.getTo()).equals(null)) {
            //【全量记录】中未查询到，则新增
            Address address = new Address();
            if (addressMap.get(tx.getFrom()).equals(null)) {
                //todo：主动发起交易的都认为是账户地址因为当前川陀版本无wasm
                address.setAddress(tx.getFrom());
                address.setType(AddressEnum.ACCOUNT.getCode());
            }
            if (addressMap.get(tx.getTo()).equals(null)) {
                address.setAddress(tx.getTo());
                if (InnerContractAddEnum.innerContractList.contains(tx.getTo())) {
                    address.setType(AddressEnum.INNERCONTRACT.getCode());
                }
                address.setType(AddressEnum.ACCOUNT.getCode());
            }
            address.setTxQty(1);
            switch (tx.getTypeEnum()) {
                case TRANSFER:
                    address.setTransferQty(1);
                    break;
                case CREATE_PROPOSAL_PARAMETER:// 创建参数提案
                case CREATE_PROPOSAL_TEXT:// 创建文本提案
                case CREATE_PROPOSAL_UPGRADE:// 创建升级提案
                case DECLARE_VERSION:// 版本声明
                case VOTING_PROPOSAL:// 提案投票
                    address.setProposalQty(1);
                    break;
                case DELEGATE:// 发起委托
                    address.setCandidateCount(1);
                    address.setDelegateQty(1);
                    break;
                case UN_DELEGATE:// 撤销委托
                    address.setDelegateQty(1);
                    break;
                case INCREASE_STAKING:// 增加自有质押
                case CREATE_VALIDATOR:// 创建验证人
                case EXIT_VALIDATOR:// 退出验证人
                case REPORT_VALIDATOR:// 举报验证人
                case EDIT_VALIDATOR:// 编辑验证人
                    address.setStakingQty(1);
                    break;
            }
            //若为新增address，添加到全量数据map记录中
            addressMap.put(address.getAddress(), address);
            //若为新增address，添加到新增Set记录中
            addressExecuteResult.getAddAddress().add(address);
        } else {
            Address address = new Address();
            //【全量记录】中查询到，则更新
            if (!addressMap.get(tx.getFrom()).equals(null)) {
                address = addressMap.get(tx.getFrom());
            }
            if (!addressMap.get(tx.getTo()).equals(null)) {
                address = addressMap.get(tx.getTo());
            }
            address.setTxQty(address.getTxQty() + 1);
            switch (tx.getTypeEnum()) {
                case TRANSFER:
                    address.setTransferQty(address.getTransferQty() + 1);
                    break;
                case CREATE_PROPOSAL_PARAMETER:// 创建参数提案
                case CREATE_PROPOSAL_TEXT:// 创建文本提案
                case CREATE_PROPOSAL_UPGRADE:// 创建升级提案
                case DECLARE_VERSION:// 版本声明
                case VOTING_PROPOSAL:// 提案投票
                    address.setProposalQty(address.getProposalQty() + 1);
                    break;
                case DELEGATE:// 发起委托
                    address.setCandidateCount(address.getCandidateCount() + 1);
                    address.setDelegateQty(address.getDelegateQty() + 1);
                    break;
                case UN_DELEGATE:// 撤销委托
                    address.setDelegateQty(address.getDelegateQty() + 1);
                    break;
                case INCREASE_STAKING:// 增加自有质押
                case CREATE_VALIDATOR:// 创建验证人
                case EXIT_VALIDATOR:// 退出验证人
                case REPORT_VALIDATOR:// 举报验证人
                case EDIT_VALIDATOR:// 编辑验证人
                    address.setStakingQty(address.getStakingQty() + 1);
                    break;
            }
            //若为存在address，更新全量数据map记录中
            addressMap.put(address.getAddress(), address);
            //若为存在address，添加到更新Set记录中
            addressExecuteResult.getUpdateAddress().add(address);
        }


    }

    public AddressExecuteResult exportResult () {
        return addressExecuteResult;
    }

    public void commitResult () {
        addressExecuteResult.getAddAddress().clear();
        addressExecuteResult.getUpdateAddress().clear();
    }
/*
    public void statisticsAddressInfo ( TreeMap <String, Staking> stakingCache, TreeMap <String, Delegation> delegationCache ) {
        //新增
        addressExecuteResult.getAddAddress().forEach(address -> {
            //统计质押
            //地址的质押金 = 该地址质押过的全部节点的质押金（犹豫期+锁定期）
            Map.Entry <String, Staking> lastEntry = stakingCache.lastEntry();
            BigInteger stakingValue = new BigInteger(lastEntry.getValue().getStakingHas()).add(new BigInteger(lastEntry.getValue().getStakingLocked()));
            address.setStakingValue(new BigInteger(address.getStakingValue()).add(stakingValue).toString());

            //统计委托
            //地址的委托金 = 该地址全部委托的的金额汇总（犹豫期+锁定期）
            delegationCache.forEach(( k, v ) -> {
                if (k.equals(address)) {

                });
                BigInteger delegationValue = new BigInteger("");
            });
            //更新

        }*/
    }
